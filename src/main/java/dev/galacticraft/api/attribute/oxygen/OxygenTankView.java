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

package dev.galacticraft.api.attribute.oxygen;

import alexiil.mc.lib.attributes.Convertible;
import alexiil.mc.lib.attributes.ListenerToken;
import dev.galacticraft.api.attribute.oxygen.extractable.OxygenExtractable;
import dev.galacticraft.api.attribute.oxygen.insertable.OxygenInsertable;
import dev.galacticraft.api.attribute.oxygen.transferable.EmptyOxygenTransferable;
import dev.galacticraft.api.attribute.oxygen.transferable.OxygenTransferable;
import org.jetbrains.annotations.Nullable;

/**
 * @author <a href="https://github.com/TeamGalacticraft">TeamGalacticraft</a>
 */
public interface OxygenTankView extends Convertible {
    int getCapacity();

    int getAmount();

    @Nullable ListenerToken listen(OxygenTankChangedListener listener);

    void removeListeners();

    default OxygenInsertable getInsertable() {
        return this.getTransferable();
    }

    default OxygenExtractable getExtractable() {
        return this.getTransferable();
    }

    default OxygenTransferable getTransferable() {
        return EmptyOxygenTransferable.NULL;
    }

    @Nullable
    @Override
    default <T> T convertTo(Class<T> otherType) {
        return Convertible.offer(otherType, this.getTransferable());
    }

    @FunctionalInterface
    interface OxygenTankChangedListener {
        void onChanged(OxygenTankView tank, int previous);
    }
}
