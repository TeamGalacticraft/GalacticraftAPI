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
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleViewIterator;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.Iterator;

public class SingleTypeSlotGasStorage extends SnapshotParticipant<Long> implements Storage<Gas>, StorageView<Gas> {
    private final long capacity;
    private final Gas gas;
    private long amount;

    public SingleTypeSlotGasStorage(long capacity, Gas gas) {
        this(capacity, gas, 0L);
    }

    public SingleTypeSlotGasStorage(long capacity, Gas gas, long amount) {
        Preconditions.checkNotNull(gas);
        StoragePreconditions.notNegative(amount);
        this.capacity = capacity;
        this.gas = gas;
        this.amount = amount;
    }

    @Override
    public long insert(Gas resource, long maxAmount, TransactionContext transaction) {
        Preconditions.checkNotNull(resource);
        StoragePreconditions.notNegative(maxAmount);
        if (!resource.equals(this.gas) || !this.supportsInsertion()) return 0;
        updateSnapshots(transaction);
        maxAmount = Math.min(maxAmount, this.capacity);
        this.amount += maxAmount;
        return maxAmount;
    }

    @Override
    public long extract(Gas resource, long maxAmount, TransactionContext transaction) {
        Preconditions.checkNotNull(resource);
        StoragePreconditions.notNegative(maxAmount);

        if (!resource.equals(this.gas) || !this.supportsExtraction()) return 0;
        updateSnapshots(transaction);
        long amount1 = this.amount;
        this.amount = Math.max(this.amount - maxAmount, 0);
        return amount1 - this.amount;
    }

    @Override
    public boolean isResourceBlank() {
        return this.amount == 0;
    }

    @Override
    public Gas getResource() {
        return this.gas;
    }

    @Override
    public long getAmount() {
        return this.amount;
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
    protected Long createSnapshot() {
        return this.amount;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        this.amount = snapshot;
    }
}
