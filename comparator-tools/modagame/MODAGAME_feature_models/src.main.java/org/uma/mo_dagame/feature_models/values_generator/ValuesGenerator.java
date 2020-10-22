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

package org.uma.mo_dagame.feature_models.values_generator;

import org.uma.mo_dagame.feature_models.FeatureModel;
import org.uma.mo_dagame.feature_models.ObjectivesValuesGenerator;
import org.uma.mo_dagame.feature_models.SxfmParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Automatically adds random objectives values to feature models
 */
public class ValuesGenerator {

	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		String path;
		String line;
		BufferedReader br;
		ObjectivesValuesGenerator valuesGenerator;

		if (args.length < 2) {
			System.err.println("Usage: ValuesGenerator MODELS_FOLDER VALUES_INPUT_FILE HEADER[optional]");
			return;
		}

		path = args[0];

		// Parse input file
		br = new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));

		valuesGenerator = new ObjectivesValuesGenerator();
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\\s");
			if (parts.length == 3) {
				if (parts[0].compareToIgnoreCase("double") == 0) {
					int valueType = ObjectivesValuesGenerator.TYPE_DOUBLE;
					double min = Double.valueOf(parts[1]);
					double max = Double.valueOf(parts[2]);
					valuesGenerator.addObjective(valueType, min, max);
				} else if (parts[0].compareToIgnoreCase("integer") == 0) {
					int valueType = ObjectivesValuesGenerator.TYPE_INTEGER;
					int min = Integer.valueOf(parts[1]);
					int max = Integer.valueOf(parts[2]);
					valuesGenerator.addObjective(valueType, min, max);
				} else if (parts[0].compareToIgnoreCase("boolean") == 0) {
					int valueType = ObjectivesValuesGenerator.TYPE_BOOLEAN;
					valuesGenerator.addObjective(valueType, 0, 1);
				} else {
					// Unexpected type
					System.err.println("Unexpected value type: " + parts[0]);
					break;
				}
			}
		}

		br.close();

		String header = null;
		if (args.length > 2)
			header = args[2];

		File f = new File(path);
		if (f.isDirectory()) {
			String[] files = f.list();
			for (String file : files) {
				if (file.endsWith(".xml")) {
					generateQAsForFile(path, valuesGenerator, file, header);
				}
			}
		} else {
			generateQAsForFile(path, valuesGenerator, "", header);
		}
		System.out.println("Finished!");
	}

	private static void generateQAsForFile(String path, ObjectivesValuesGenerator valuesGenerator, String file,
			String header) {

		String featureModelFile = path + File.separator + file;
		String withHeader = (header==null) ? "":"-header";
		String objectivesValuesFile = featureModelFile.substring(0, featureModelFile.lastIndexOf(".")) + withHeader + ".obj";

		System.out.println("Parsing " + file + "...");
		System.out.println(objectivesValuesFile);

		// Parse feature model
		FeatureModel fm;
		try {
			fm = SxfmParser.parse(featureModelFile);
			valuesGenerator.generate(fm, objectivesValuesFile, header);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
