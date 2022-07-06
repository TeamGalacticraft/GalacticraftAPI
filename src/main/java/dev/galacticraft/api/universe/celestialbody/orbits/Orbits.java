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

package dev.galacticraft.api.universe.celestialbody.orbits;

import dev.galacticraft.api.universe.celestialbody.CelestialBodyConfig;

/**
 * Represents a {@link dev.galacticraft.api.universe.celestialbody.CelestialBodyType<C> celestial body type} that orbits a celestial boy.
 *
 * @param <C> the type of configuration
 */
public interface Orbits<C extends CelestialBodyConfig>
{
	/**
	 * Returns the approximate temperature on this celestial body during the day (in Celsius)
	 *
	 * @param config the celestial body configuration to be queried
	 * @return the approximate temperature on this celestial body during the day
	 */
	int dayTemperature(C config);

	/**
	 * Returns the approximate temperature on this celestial body during the night (in Celsius)
	 *
	 * @param config the celestial body configuration to be queried
	 * @return the approximate temperature on this celestial body during the night
	 */
	int nightTemperature(C config);


}
