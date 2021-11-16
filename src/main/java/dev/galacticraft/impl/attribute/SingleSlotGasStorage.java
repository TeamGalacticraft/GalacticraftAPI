/*
 * Copyright (c) 2019-2021 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.impl.attribute;

import com.google.common.base.Preconditions;
import dev.galacticraft.api.gas.Gas;
import dev.galacticraft.api.gas.GasStack;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleViewIterator;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.Iterator;

public class SingleSlotGasStorage extends SnapshotParticipant<GasStack> implements Storage<Gas>, StorageView<Gas> {
    private final long capacity;
    private GasStack stack;

    public SingleSlotGasStorage(long capacity) {
        this(capacity, GasStack.EMPTY);
    }

    public SingleSlotGasStorage(long capacity, GasStack stack) {
        this.capacity = capacity;
        this.stack = stack;
    }

    @Override
    public long insert(Gas resource, long maxAmount, TransactionContext transaction) {
        Preconditions.checkNotNull(resource);
        StoragePreconditions.notNegative(maxAmount);
        if (this.supportsInsertion()) {
            if (this.stack.isEmpty()) {
                maxAmount = Math.min(maxAmount, this.capacity);
                updateSnapshots(transaction);
                this.stack = new GasStack(resource, maxAmount);
                return maxAmount;
            } else {
                if (this.stack.gas().equals(resource)) {
                    updateSnapshots(transaction);
                    long amount = this.stack.amount();
                    this.stack.setAmount(Math.min(maxAmount + amount, this.capacity));
                    return this.stack.amount() - amount;
                }
            }
        }
        return 0;
    }

    @Override
    public long extract(Gas resource, long maxAmount, TransactionContext transaction) {
        Preconditions.checkNotNull(resource);
        StoragePreconditions.notNegative(maxAmount);

        if (this.supportsExtraction() && resource.equals(this.stack.gas())) {
            updateSnapshots(transaction);
            long amount1 = this.stack.amount();
            long amount = Math.max(amount1 - maxAmount, 0);
            this.stack.setAmount(amount);
            return amount1 - amount;
        }
        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return this.stack.isEmpty();
    }

    @Override
    public Gas getResource() {
        return this.stack.gas();
    }

    @Override
    public long getAmount() {
        return this.stack.amount();
    }

    @Override
    public long getCapacity() {
        return this.capacity;
    }

    @Override
    public Iterator<StorageView<Gas>> iterator(TransactionContext transaction) {
        return SingleViewIterator.create(this, transaction);
    }

    @Override
    protected GasStack createSnapshot() {
        GasStack stack = this.stack;
        this.stack = stack.copy();
        return stack;
    }

    @Override
    protected void readSnapshot(GasStack snapshot) {
        this.stack = snapshot;
    }
}
