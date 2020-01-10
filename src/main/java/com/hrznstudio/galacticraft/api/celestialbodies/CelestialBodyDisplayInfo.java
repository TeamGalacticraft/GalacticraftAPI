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

    private final double orbitDistance;
    private final double orbitTime;
    private final Identifier iconTexture;
    private final int iconX;
    private final int iconY;
    private final int iconW;
    private final int iconH;

    /**
     * Display information for celestial bodies
     * @param orbitDistance Distance away from parent body {@link CelestialBodyType#getParent()}
     * @param orbitTime The time it takes for the body to complete a single orbit (in ticks)
     * @param iconTexture Identifier for body's icon texture
     * @param iconX X chord in icon texture
     * @param iconY Y chord in icon texture
     * @param iconW Width of icon in texture
     * @param iconH Height of icon in texture
     */
    public CelestialBodyDisplayInfo(double orbitDistance, double orbitTime, Identifier iconTexture, int iconX, int iconY, int iconW, int iconH) {
        this.orbitDistance = orbitDistance;
        this.orbitTime = orbitTime;
        this.iconTexture = iconTexture;
        this.iconX = iconX;
        this.iconY = iconY;
        this.iconW = iconW;
        this.iconH = iconH;
    }

    public double getOrbitDistance() {
        return orbitDistance;
    }

    public double getOrbitTime() {
        return orbitTime;
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
        private Identifier iconTexture = null;
        private double orbitDistance = 0d;
        private double orbitTime = 0d;
        private int iconX = 0;
        private int iconY = 0;
        private int iconW = 16;
        private int iconH = 16;

        public Builder texture(Identifier texture) {
            this.iconTexture = texture;
            return this;
        }

        public Builder distance(double orbitDistance) {
            this.orbitDistance = orbitDistance;
            return this;
        }

        public Builder time(double time) {
            this.orbitTime = time;
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
            return new CelestialBodyDisplayInfo(this.orbitDistance, this.orbitTime, this.iconTexture, this.iconX, this.iconY, this.iconW, this.iconH);
        }
    }
}
