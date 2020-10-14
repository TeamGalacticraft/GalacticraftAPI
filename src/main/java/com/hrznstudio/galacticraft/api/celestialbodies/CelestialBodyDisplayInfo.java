/*
 * Copyright (c) 2020 HRZN LTD
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

package com.hrznstudio.galacticraft.api.celestialbodies;

import net.minecraft.util.Identifier;

public class CelestialBodyDisplayInfo {

    private final double orbitTime;
    private final double dayLength;
    private final float orbitOffsetX;
    private final float orbitOffsetY;
    private final float orbitRadX;
    private final float orbitRadY;
    private final float orbitRot;
    private final float scale;

    private final Identifier iconTexture;
    private final int iconX;
    private final int iconY;
    private final int iconW;
    private final int iconH;

    /**
     * Display information for celestial bodies
     * @param orbitTime The time it takes for the body to complete a single orbit (in ticks)
     * @param dayLength The time it takes for a full day/night cycle on this planet (in ticks)
     * @param orbitOffsetX The distance on the X axis from the parent planet
     * @param orbitOffsetY The distance on the Y axis from the parent planet
     * @param orbitRadX The planet's orbit radius on the X axis
     * @param orbitRadY The planet's orbit radius on the Y axis
     * @param orbitRot The planet's orbit rotation on the Z axis
     * @param scale The planet's size, relative to earth
     * @param iconTexture Identifier for body's icon texture
     * @param iconX X chord in icon texture
     * @param iconY Y chord in icon texture
     * @param iconW Width of icon in texture
     * @param iconH Height of icon in texture
     */
    public CelestialBodyDisplayInfo(double orbitTime, double dayLength, float orbitOffsetX, float orbitOffsetY, float orbitRadX, float orbitRadY, float orbitRot, float scale, Identifier iconTexture, int iconX, int iconY, int iconW, int iconH) {
        this.orbitTime = orbitTime;
        this.dayLength = dayLength;
        this.orbitOffsetX = orbitOffsetX;
        this.orbitOffsetY = orbitOffsetY;
        this.orbitRadX = orbitRadX;
        this.orbitRadY = orbitRadY;
        this.orbitRot = orbitRot;
        this.scale = scale;

        this.iconTexture = iconTexture;
        this.iconX = iconX;
        this.iconY = iconY;
        this.iconW = iconW;
        this.iconH = iconH;
    }

    public double getOrbitTime() {
        return orbitTime;
    }

    public double getDayLength() {
        return dayLength;
    }

    public float getOrbitOffsetX() {
        return orbitOffsetX;
    }

    public float getOrbitOffsetY() {
        return orbitOffsetY;
    }

    public float getOrbitRadX() {
        return orbitRadX;
    }

    public float getOrbitRadY() {
        return orbitRadY;
    }

    public float getOrbitRot() {
        return orbitRot;
    }

    public float getScale() {
        return scale;
    }

    public Identifier getIconTexture() {
        return iconTexture;
    }

    public int getIconX() {
        return iconX;
    }

    public int getIconY() {
        return iconY;
    }

    public int getIconW() {
        return iconW;
    }

    public int getIconH() {
        return iconH;
    }


    public static class Builder {
        private double orbitTime = 1536000.0d; // 64 day obit default
        private double dayLength = 24000d;
        private float orbitOffsetX = 0.0f;
        private float orbitOffsetY = 0.0f;
        private float orbitRadX = 25.0f;
        private float orbitRadY = 25.0f;
        private float orbitRot = 0.0f;
        private float scale = 1.0f;

        private Identifier iconTexture = null;
        private int iconX = 0;
        private int iconY = 0;
        private int iconW = 16;
        private int iconH = 16;

        public Builder time(double time) {
            this.orbitTime = time;
            return this;
        }

        public Builder dayLength(double dayLength) {
            this.dayLength = dayLength;
            return this;
        }

        public Builder offsetX(float offX) {
            this.orbitOffsetX = offX;
            return this;
        }

        public Builder offsetY(float offY) {
            this.orbitOffsetY = offY;
            return this;
        }

        public Builder radX(float radX) {
            this.orbitRadX = radX;
            return this;
        }

        public Builder radY(float radY) {
            this.orbitRadY = radY;
            return this;
        }

        public Builder rot(float rot) {
            this.orbitRot = rot;
            return this;
        }

        public Builder scale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder texture(Identifier texture) {
            this.iconTexture = texture;
            return this;
        }

        public Builder x(int x) {
            this.iconX = x;
            return this;
        }

        public Builder y(int y) {
            this.iconY = y;
            return this;
        }

        public Builder w(int w) {
            this.iconW = w;
            return this;
        }

        public Builder h(int h) {
            this.iconH = h;
            return this;
        }

        public CelestialBodyDisplayInfo build() {
            if (this.iconTexture == null) throw new NullPointerException("Tried to create display info without icon!");
            return new CelestialBodyDisplayInfo(this.orbitTime, this.dayLength, this.orbitOffsetX, this.orbitOffsetY, this.orbitRadX, this.orbitRadY, this.orbitRot, this.scale, this.iconTexture, this.iconX, this.iconY, this.iconW, this.iconH);
        }
    }
}
