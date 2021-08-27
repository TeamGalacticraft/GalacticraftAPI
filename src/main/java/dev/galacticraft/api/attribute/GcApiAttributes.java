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

package dev.galacticraft.api.attribute;

import alexiil.mc.lib.attributes.Attributes;
import alexiil.mc.lib.attributes.DefaultedAttribute;
import dev.galacticraft.api.attribute.oxygen.EmptyOxygenTank;
import dev.galacticraft.api.attribute.oxygen.OxygenTank;
import dev.galacticraft.api.attribute.oxygen.extractable.EmptyOxygenExtractable;
import dev.galacticraft.api.attribute.oxygen.extractable.OxygenExtractable;
import dev.galacticraft.api.attribute.oxygen.insertable.OxygenInsertable;
import dev.galacticraft.api.attribute.oxygen.insertable.RejectingOxygenInsertable;
import dev.galacticraft.api.attribute.oxygen.transferable.EmptyOxygenTransferable;
import dev.galacticraft.api.attribute.oxygen.transferable.OxygenTransferable;

public class GcApiAttributes {
    public static final DefaultedAttribute<OxygenTank> OXYGEN_TANK = Attributes.createDefaulted(OxygenTank.class, EmptyOxygenTank.NULL);
    public static final DefaultedAttribute<OxygenInsertable> OXYGEN_INSERTABLE = Attributes.createDefaulted(OxygenInsertable.class, RejectingOxygenInsertable.NULL);
    public static final DefaultedAttribute<OxygenExtractable> OXYGEN_EXTRACTABLE = Attributes.createDefaulted(OxygenExtractable.class, EmptyOxygenExtractable.NULL);
    public static final DefaultedAttribute<OxygenTransferable> OXYGEN_TRANSFERABLE = Attributes.createDefaulted(OxygenTransferable.class, EmptyOxygenTransferable.NULL);
}
