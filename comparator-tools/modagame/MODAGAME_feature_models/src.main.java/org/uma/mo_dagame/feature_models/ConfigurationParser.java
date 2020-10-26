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

public class ConfigurationParser {

    public static Configuration parse(String csvFile, FeatureModel fm) {
        Configuration configuration = new Configuration(fm);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(csvFile));
            // Two first lines are not interesting
            reader.readLine();
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    configuration.add(fm.getID(line));
                }
            }

        } catch (java.io.IOException e) {
            return null;
        }

        return configuration;
    }
}
