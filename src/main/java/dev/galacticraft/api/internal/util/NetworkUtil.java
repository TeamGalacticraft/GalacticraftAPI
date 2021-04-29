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

package dev.galacticraft.api.internal.util;

public class NetworkUtil {
    public static byte packBooleans(boolean b, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7) {
        byte out = -128;
        out += b ? 1 : 0;
        out += b1 ? 2 : 0;
        out += b2 ? 4 : 0;
        out += b3 ? 8 : 0;
        out += b4 ? 16 : 0;
        out += b5 ? 32 : 0;
        out += b6 ? 64 : 0;
        out += b7 ? 128 : 0;
        return out;
    }

    public static byte packBooleans(boolean b, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6) {
        return packBooleans(b, b1, b2, b3, b4, b5, b6, false);
    }

    public static byte packBooleans(boolean b, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return packBooleans(b, b1, b2, b3, b4, b5, false);
    }

    public static byte packBooleans(boolean b, boolean b1, boolean b2, boolean b3, boolean b4) {
        return packBooleans(b, b1, b2, b3, b4, false);
    }

    public static byte packBooleans(boolean b, boolean b1, boolean b2, boolean b3) {
        return packBooleans(b, b1, b2, b3, false);
    }

    public static byte packBooleans(boolean b, boolean b1, boolean b2) {
        return packBooleans(b, b1, b2, false);
    }

    public static byte packBooleans(boolean b, boolean b1) {
        return packBooleans(b, b1, false);
    }

    public static byte packBooleans(boolean b) {
        return packBooleans(b, false);
    }
    
    public static boolean unpackBoolean(byte b, int index) {
        assert index >= 0 && index < 8;
        return (b >> index & 0x1) == 1;
    }
}
