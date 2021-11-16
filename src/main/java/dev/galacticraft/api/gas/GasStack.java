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

package dev.galacticraft.api.gas;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GasStack {
    public static final GasStack EMPTY = new GasStack(null, 0);

    private final @Nullable Gas gas;
    private long amount;

    public GasStack(@Nullable Gas gas, long amount) {
        StoragePreconditions.notNegative(amount);
        this.gas = gas;
        this.amount = amount;
    }

    public @Nullable Gas gas() {
        if (this.amount == 0) return null;
        return this.gas;
    }

    public long amount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        StoragePreconditions.notNegative(amount);
        this.amount = amount;
    }

    public boolean isEmpty() {
        return this.amount == 0 || this.gas == null;
    }

    public GasStack copy() {
        return new GasStack(this.gas, this.amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GasStack) obj;
        return Objects.equals(this.gas, that.gas) &&
                this.amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gas, amount);
    }

    @Override
    public String toString() {
        return "GasStack[" +
                "gas=" + gas + ", " +
                "amount=" + amount + ']';
    }
}
