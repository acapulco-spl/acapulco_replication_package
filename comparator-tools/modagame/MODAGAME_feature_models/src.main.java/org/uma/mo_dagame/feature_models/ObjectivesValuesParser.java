/*
 * Copyright 2014 Gustavo García Pascual, Mónica Pinto and Lidia Fuentes
 *
 * This file is part of MO-DAGAME
 * *
 * MO-DAGAME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MO-DAGAME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MO-DAGAME.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.uma.mo_dagame.feature_models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectivesValuesParser {

	private static final Pattern PATTERN_LINE =
			Pattern.compile("^\\s*\\((.+)\\)\\s*(.*)\\s*");

	public static void parse(String objFile, FeatureModel fm) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(objFile));
		String line;
		boolean numberSet = false;
		while ((line = reader.readLine()) != null) {
			Matcher m = PATTERN_LINE.matcher(line); 
			if (m.matches()) {
				String name = m.group(1);
				String[] objectives = m.group(2).split("\\s+");
				if (!numberSet) {
					fm.setNumberOfObjectives(objectives.length);
					numberSet = true;
				}
				double[] values = new double[objectives.length];
				for (int i = 0; i < values.length; i++) {
					values[i] = Double.parseDouble(objectives[i]);
				}
				fm.setObjectivesValues(fm.getID(name), values);
			}
		}
		reader.close();
	}
}
