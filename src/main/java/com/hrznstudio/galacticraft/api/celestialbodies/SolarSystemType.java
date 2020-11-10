package com.hrznstudio.galacticraft.api.celestialbodies;

import java.util.Objects;

public class SolarSystemType {
    public static final SolarSystemType SOL = new SolarSystemType.Builder()
            .translationKey("galacticraft-api.solar_system.sol")
            .galaxyTranslationKey("galacticraft-api.galaxy.milky_way")
            .build();

    private final float x;
    private final float y;
    private final String translationKey;
    private final String galaxyTranslationKey;

    private SolarSystemType(float x, float y, String translationKey, String galaxyTranslationKey) {
        this.x = x;
        this.y = y;
        this.translationKey = translationKey;
        this.galaxyTranslationKey = galaxyTranslationKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolarSystemType that = (SolarSystemType) o;
        return Float.compare(that.x, x) == 0 &&
                Float.compare(that.y, y) == 0 &&
                translationKey.equals(that.translationKey) &&
                galaxyTranslationKey.equals(that.galaxyTranslationKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, translationKey, galaxyTranslationKey);
    }

    @Override
    public String toString() {
        return "SolarSystemType{" +
                "x=" + x +
                ", y=" + y +
                ", translationKey='" + translationKey + '\'' +
                ", galaxyTranslationKey='" + galaxyTranslationKey + '\'' +
                '}';
    }

    public static class Builder {
        private float x = 0;
        private float y = 0;
        private String translationKey = null;
        private String galaxyTranslationKey = null;

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder translationKey(String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        public Builder galaxyTranslationKey(String galaxyTranslationKey) {
            this.galaxyTranslationKey = galaxyTranslationKey;
            return this;
        }

        public SolarSystemType build() {
            if (translationKey == null || galaxyTranslationKey == null) throw new RuntimeException("Tried to build solar system without name!");
            return new SolarSystemType(x, y, translationKey, galaxyTranslationKey);
        }

        @Override
        public String toString() {
            return "SolarSystemTypeBuilder{" +
                    "x=" + x +
                    ", y=" + y +
                    ", translationKey='" + translationKey + '\'' +
                    ", galaxyTranslationKey='" + galaxyTranslationKey + '\'' +
                    '}';
        }
    }
}
