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
