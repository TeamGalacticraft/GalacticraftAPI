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
    private final double orbitOffsetX;
    private final double orbitOffsetY;
    private final double orbitRadX;
    private final double orbitRadY;
    private final double scale;

    private final Identifier iconTexture;
    private final int iconX;
    private final int iconY;
    private final int iconW;
    private final int iconH;

    /**
     * Display information for celestial bodies
     * @param orbitTime The time it takes for the body to complete a single orbit (in ticks)
     * @param orbitOffsetX The distance on the X axis from the parent planet
     * @param orbitOffsetY The distance on the Y axis from the parent planet
     * @param orbitRadX The planet's orbit radius on the X axis
     * @param orbitRadY The planet's orbit radius on the Y axis
     * @param scale The planet's scale, relative to earth
     * @param iconTexture Identifier for body's icon texture
     * @param iconX X chord in icon texture
     * @param iconY Y chord in icon texture
     * @param iconW Width of icon in texture
     * @param iconH Height of icon in texture
     */
    public CelestialBodyDisplayInfo(double orbitTime, double orbitOffsetX, double orbitOffsetY, double orbitRadX, double orbitRadY, double scale, Identifier iconTexture, int iconX, int iconY, int iconW, int iconH) {
        this.orbitTime = orbitTime;
        this.orbitOffsetX = orbitOffsetX;
        this.orbitOffsetY = orbitOffsetY;
        this.orbitRadX = orbitRadX;
        this.orbitRadY = orbitRadY;
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

    public double getOrbitOffsetX() {
        return orbitOffsetX;
    }

    public double getOrbitOffsetY() {
        return orbitOffsetY;
    }

    public double getOrbitRadX() {
        return orbitRadX;
    }

    public double getOrbitRadY() {
        return orbitRadY;
    }

    public double getScale() {
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
        private double orbitTime = 0d;
        private double orbitOffsetX = 0.0d;
        private double orbitOffsetY = 0.0d;
        private double orbitRadX = 25.0d;
        private double orbitRadY = 25.0d;
        private double scale = 1.0d;

        private Identifier iconTexture = null;
        private int iconX = 0;
        private int iconY = 0;
        private int iconW = 16;
        private int iconH = 16;

        public Builder time(double time) {
            this.orbitTime = time;
            return this;
        }

        public Builder offsetX(int offX) {
            this.orbitOffsetX = offX;
            return this;
        }

        public Builder offsetY(int offY) {
            this.orbitOffsetY = offY;
            return this;
        }

        public Builder radX(int radX) {
            this.orbitRadX = radX;
            return this;
        }

        public Builder radY(int radY) {
            this.orbitRadY = radY;
            return this;
        }

        public Builder scale(double scale) {
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
            return new CelestialBodyDisplayInfo(this.orbitTime, this.orbitOffsetX, this.orbitOffsetY, this.orbitRadX, this.orbitRadY, this.scale, this.iconTexture, this.iconX, this.iconY, this.iconW, this.iconH);
        }
    }
}
