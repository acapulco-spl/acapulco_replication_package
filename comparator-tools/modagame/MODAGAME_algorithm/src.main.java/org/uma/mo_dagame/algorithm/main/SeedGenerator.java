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

package org.uma.mo_dagame.algorithm.main;

import com.carrotsearch.hppc.IntArrayList;

import org.uma.mo_dagame.algorithm.BasicFix;
import org.uma.mo_dagame.feature_models.Configuration;
import org.uma.mo_dagame.feature_models.FeatureModel;
import org.uma.mo_dagame.feature_models.SxfmParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Random;

public class SeedGenerator {

    public static void main(String args[]) throws Exception {
        String model1 = "A-x264";
        String model2 = "B-Wget";
        String model3 = "C-BerkeleyDBMemory";
        String model4 = "D-SensorNetwork";
        String model5 = "E-mobile_game";
        String model6 = "F-TankWar";
        String model7 = "G-mobile_media2";
        String model8 = "H-mobile_guide";
        String model9 = "I-SPLOT-3CNF-FM-500-50-1.00-SAT-1";
        String model10 = "J-SPLOT-3CNF-FM-1000-100-1.00-SAT-1";
        String model11 = "K-SPLOT-3CNF-FM-2000-200-0.50-SAT-1";
        String model12 = "L-SPLOT-3CNF-FM-5000-500-0.30-SAT-1";

        String model = model12;
        FeatureModel fm =
                SxfmParser.parse("/home/gustavo/study/JSS-SBSE-2014/models/" + model + ".xml");
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(BasicFix.PARAMETER_FEATURE_MODEL, fm);
        BasicFix fix = new BasicFix(parameters);

        Configuration seed = new Configuration(fm);
        IntArrayList features = fm.getFeatures();
        final int[] buffer = features.buffer;
        final int size = features.size();
        Random rand = new Random();
        boolean success = false;
        int iterations = 1;
        do {
            Configuration randomConfig = new Configuration(fm);
            for (int i = 0; i < size; i++) {
                if (rand.nextDouble() < 0.25) {
                    randomConfig.add(buffer[i]);
                }
            }
            success = fix.fix(randomConfig, seed);
            System.out.println(Integer.toString(iterations++));
        } while (!success);

        IntArrayList seedFeatures = seed.getConfigurationByID();
        final int[] seedBuffer = seedFeatures.buffer;
        final int seedSize = seedFeatures.size();

        BufferedWriter writer =
                new BufferedWriter(new FileWriter("/home/gustavo/seeds/" + model + ".csv"));
        for (int i = 0; i < seedSize; i++) {
            String name = fm.getName(seedBuffer[i]);
            writer.write(name);
            writer.newLine();
        }
        writer.close();
    }
}
