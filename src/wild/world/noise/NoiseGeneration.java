/*
 *  Copyright (C) 2016 Marccspro
 *
 *     This file is part of "Procedural Map Generation".
 *
 *         "Procedural Map Generation" is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         "Procedural Map Generation" is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with "Procedural Map Generation".  If not, see http://www.gnu.org/licenses/.
 */

package wild.world.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoiseGeneration
{
	private long 			seed;
	private List<NoisePass>	noisePasses;

	public NoiseGeneration(long seed)
	{
		this.seed = seed;
		this.noisePasses = new ArrayList<>();

		addNoisePasses(seed);
		addNoisePasses(seed + 1);
		addNoisePasses(seed - 1);
	}

	private void addNoisePasses(long seed)
	{
		add(new NoisePass(seed, 50, 5f, 0, 1));
	}

	public void add(NoisePass pass)
	{
		noisePasses.add(pass);
	}

	public float getExactNoise(float x, float y)
	{
		float n = -10000;
		for (NoisePass p : noisePasses)
		{
			float nn = p.getNoisePass(x, y);
			if (nn > n) n = nn;
		}
		return n;
	}
	


	public long getSeed()
	{
		return seed;
	}
}
