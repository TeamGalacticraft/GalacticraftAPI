/*
 * Copyright (c) 2019-2022 Team Galacticraft
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

package dev.galacticraft.api.rocket.recipe;

import java.util.Collections;
import java.util.List;
// todo: design?
public class RocketPartRecipe {
    public static final RocketPartRecipe EMPTY = new RocketPartRecipe(Collections.emptyList());

    private final int width;
    private final int height;
    private final List<RocketRecipeSlot> slots;

    public RocketPartRecipe(List<RocketRecipeSlot> slots) {
        this.slots = slots;
        int maxX = 0;
        int maxY = 0;
        for (RocketRecipeSlot slot : slots) {
            maxX = Math.max(maxX, slot.x() + 18);
            maxY = Math.max(maxY, slot.y() + 18);
        }
        this.width = maxX;
        this.height = maxY;
    }
}
