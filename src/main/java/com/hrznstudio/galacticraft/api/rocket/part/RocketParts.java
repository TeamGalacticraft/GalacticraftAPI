/*
 * Copyright (c) 2019-2021 HRZN LTD
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

package com.hrznstudio.galacticraft.api.rocket.part;

import com.hrznstudio.galacticraft.api.internal.fabric.GalacticraftAPI;
import net.minecraft.block.Blocks;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class RocketParts {
    public static final RocketPart INVALID = RocketPart.Builder.create(new Identifier(GalacticraftAPI.MOD_ID, "invalid"))
            .name(new TranslatableText("tooltip.galacticraft-api.something_went_wrong"))
            .type(RocketPartType.UPGRADE)
            .tier(-1)
            .research(new Identifier(GalacticraftAPI.MOD_ID, "unobtainable"))
            .recipe(false)
            .build();
}
