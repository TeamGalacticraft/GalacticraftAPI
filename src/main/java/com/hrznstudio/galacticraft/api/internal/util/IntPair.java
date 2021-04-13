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

package com.hrznstudio.galacticraft.api.internal.util;

import com.mojang.datafixers.util.Pair;

import java.util.Objects;

public class IntPair extends Pair<Integer, Integer> {
    private final int first;
    private final int second;

    public IntPair(int first, int second) {
        super(null, null);
        this.first = first;
        this.second = second;
    }

    @Override
    public Integer getFirst() {
        return this.getFirstInt();
    }

    @Override
    public Integer getSecond() {
        return this.getSecondInt();
    }

    public Integer getFirstInt() {
        return this.first;
    }

    public Integer getSecondInt() {
        return this.second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair intPair = (IntPair) o;
        return getFirstInt() == intPair.getFirstInt() && getSecondInt() == intPair.getSecondInt();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstInt(), getSecondInt());
    }

    @Override
    public String toString() {
        return "IntPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
