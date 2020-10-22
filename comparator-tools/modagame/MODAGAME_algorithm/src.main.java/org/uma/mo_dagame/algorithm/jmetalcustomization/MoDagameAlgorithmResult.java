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

package org.uma.mo_dagame.algorithm.jmetalcustomization;

import java.util.Vector;

import jmetal.core.SolutionSet;
import jmetal.experiments.util.Statistics;

public class MoDagameAlgorithmResult {

    private SolutionSet mInitialFront;
    private SolutionSet mFinalFront;
    private long mTimeToCopyInitialFront;
    private long mFixOperatorTime;

    public MoDagameAlgorithmResult() {

    }

    public SolutionSet getInitialFront() {
        return mInitialFront;
    }

    public void setInitialFront(SolutionSet initialFront) {
        mInitialFront = initialFront;
    }

    public SolutionSet getFinalFront() {
        return mFinalFront;
    }

    public void setFinalFront(SolutionSet finalFront) {
        mFinalFront = finalFront;
    }

    public long getTimeToCopyInitialFront() {
        return mTimeToCopyInitialFront;
    }

    public void setTimeToCopyInitialFront(long time) {
        mTimeToCopyInitialFront = time;
    }

    public long getFixOperatorTime() {
        return mFixOperatorTime;
    }

    public void setFixOperatorTime(long fixOperatorTime) {
        mFixOperatorTime = fixOperatorTime;
    }

    public double[] getObjectivesMedian(SolutionSet front) {
        double[] median = null;
        double[][] objectives = front.writeObjectivesToMatrix();
        int frontSize = front.size();
        if (objectives != null) {
            //int numberOfObjectives = front.get(0).getNumberOfObjectives();
            int numberOfObjectives = front.get(0).numberOfObjectives();
            median = new double[numberOfObjectives];
            for (int i = 0; i < numberOfObjectives; i++) {
                Vector<Double> objValues = new Vector<Double>();
                for (int j = 0; j < frontSize; j++) {
                    objValues.add(objectives[j][i]);
                }
                median[i] = Statistics.calculateMedian(objValues, 0, frontSize - 1);
            }
        }

        return median;
    }

    public double[] getObjectivesIQR(SolutionSet front) {
        double[] iqr = null;
        double[][] objectives = front.writeObjectivesToMatrix();
        int frontSize = front.size();
        if (objectives != null) {
            //int numberOfObjectives = front.get(0).getNumberOfObjectives();
        	int numberOfObjectives = front.get(0).numberOfObjectives();
            iqr = new double[numberOfObjectives];
            for (int i = 0; i < numberOfObjectives; i++) {
                Vector<Double> objValues = new Vector<Double>();
                for (int j = 0; j < frontSize; j++) {
                    objValues.add(objectives[j][i]);
                }
                iqr[i] = Statistics.calculateIQR(objValues);
            }
        }

        return iqr;
    }
}
