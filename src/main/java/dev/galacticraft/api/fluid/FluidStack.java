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

package dev.galacticraft.api.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FluidStack {
    public static final FluidStack EMPTY = new FluidStack(FluidVariant.blank(), 0);

    private final @NotNull FluidVariant fluid;
    private long amount;

    public FluidStack(@NotNull FluidVariant fluid, long amount) {
        StoragePreconditions.notNegative(amount);
        this.fluid = fluid;
        this.amount = amount;
    }

    public @Nullable FluidVariant fluid() {
        if (this.amount == 0) return FluidVariant.blank();
        return this.fluid;
    }

    public long amount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        StoragePreconditions.notNegative(amount);
        this.amount = amount;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        if (this.fluid.isBlank()) {
            nbt.putBoolean("Empty", true);
        } else {
            nbt.put("Fluid", this.fluid.toNbt());
            nbt.putLong("Amount", this.amount);
        }
        return nbt;
    }

    public static FluidStack readNbt(NbtCompound nbt) {
        if (nbt.getBoolean("Empty")) return EMPTY;
        return new FluidStack(FluidVariant.fromNbt(nbt.getCompound("Fluid")), nbt.getLong("Amount"));
    }

    public boolean isEmpty() {
        return this.amount == 0 || this.fluid.isBlank();
    }

    public FluidStack copy() {
        return new FluidStack(this.fluid, this.amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FluidStack) obj;
        return Objects.equals(this.fluid, that.fluid) &&
                this.amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fluid, this.amount);
    }

    @Override
    public String toString() {
        return "FluidStack[" +
                "fluid=" + this.fluid + ", " +
                "amount=" + this.amount + ']';
    }
}
