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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import com.carrotsearch.hppc.DoubleArrayList;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.ObjectArrayList;

public class ObjectivesValuesGenerator {
  
	public static final int TYPE_INTEGER = 1;
	public static final int TYPE_DOUBLE = 2;
	public static final int TYPE_BOOLEAN = 3;
	
	private Random mRandom;
	private ObjectArrayList<DoubleArrayList> mObjectivesSpecs;
	
	public ObjectivesValuesGenerator() {
		mRandom = new Random();
		mObjectivesSpecs = new ObjectArrayList<DoubleArrayList>();
	}
	
	public void addObjective(int type, double min, double max) {
		DoubleArrayList specs = new DoubleArrayList();
		specs.add(type, min, max);
		mObjectivesSpecs.add(specs);
	}
	
	public void generate(FeatureModel featureModel, String objectivesValuesFile, String header) throws IOException {
		
		if (mObjectivesSpecs.size() > 0) {
			// Get file writer
			PrintWriter writer = new PrintWriter(new FileWriter(objectivesValuesFile));
			if (header != null && !header.isEmpty()) {
				writer.append(header);
				writer.append("\n");
			}

			// Traverse features
			IntArrayList features = featureModel.getFeatures();
			int[] buffer = features.buffer;
			int size = features.size();
			for (int i = 0; i < size; i++) {
				StringBuffer line = new StringBuffer();
				
				line.append("(" + featureModel.getName(buffer[i]) + ")");
				Object[] objBuffer = mObjectivesSpecs.buffer;
				int numberOfObjectives = mObjectivesSpecs.size();
				//double pval = 0;
				for (int j = 0; j < numberOfObjectives; j++) {
					DoubleArrayList objectiveSpec = (DoubleArrayList) objBuffer[j];
					int type = (int) objectiveSpec.get(0);
					double min = objectiveSpec.get(1);
					double max = objectiveSpec.get(2);
					double value = 0;
					switch (type) {
					case TYPE_INTEGER:
						value = min + mRandom.nextInt((int) (max - min + 1));
						break;
					case TYPE_DOUBLE:
						value = min + mRandom.nextDouble() * (max - min);
						break;
					case TYPE_BOOLEAN:
						value = mRandom.nextBoolean() ? 1 : 0;
						break;
					}
					// if (j == 2 && pval == 0) value = 0; // Hardcoded modification for a particular case study
					//pval = value; 
					line.append(" " + value);
				}
				writer.println(line);
			}
			
			writer.close();			
		}
	}
}
