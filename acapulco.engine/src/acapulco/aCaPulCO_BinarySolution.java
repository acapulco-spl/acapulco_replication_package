/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/03/14
 * Copyright 2013 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package acapulco;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.henshin.model.Rule;

import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.ArrayInt;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

public class aCaPulCO_BinarySolution extends BinarySolutionType {

	private static SATIBEA_Mutation mutationOfInitialSolution;

	private int nFeat;
	private List<Integer> mandatoryFeaturesIndices, deadFeaturesIndices;
	int n = 0;
	private int[] seed;
	private List<Rule> appliedRules;

	public List<Rule> getAppliedRules() {
		return appliedRules;
	}

	public List<Integer> getFirmVariables() {
		return firmVariables;
	}

	private List<Integer> firmVariables;
	private static Random r = new Random();

	public aCaPulCO_BinarySolution(Problem problem, int nFeat, String fm, List<Integer> mandatoryFeaturesIndices,
			List<Integer> deadFeaturesIndices, int[] seed, List<Rule> appliedRules, List<Integer> firmVariables,
			List<List<Integer>> constraints) throws ClassNotFoundException {
		super(problem);
		this.nFeat = nFeat;
		this.mandatoryFeaturesIndices = mandatoryFeaturesIndices;
		this.deadFeaturesIndices = deadFeaturesIndices;
		this.seed = seed;

		if (mutationOfInitialSolution == null) {
			mutationOfInitialSolution = new SATIBEA_Mutation(new HashMap<String, Object>(), fm, nFeat, constraints);
		}
	}

	public Variable[] createVariables() {
		Variable[] vars = new Variable[2];

		Binary bin = new Binary(nFeat);

		if (r.nextBoolean()) {
			for (int j = 0; j < bin.getNumberOfBits(); j++) {
//				bin.setIth(j, seed.contains(j+1));
				bin.setIth(j, seed[j]==1);
			}

//			for (Integer f : this.mandatoryFeaturesIndices) {
//				bin.setIth(f, true);
//			}
//
//			for (Integer f : this.deadFeaturesIndices) {
//				bin.setIth(f, false);
//			}
		} else {
			try {
				mutationOfInitialSolution.doMutation(bin);
			} catch (JMException e) {
				e.printStackTrace();
			}

		}

		vars[0] = bin;

		vars[1] = new ArrayInt(0);

		return vars;
	}

}
