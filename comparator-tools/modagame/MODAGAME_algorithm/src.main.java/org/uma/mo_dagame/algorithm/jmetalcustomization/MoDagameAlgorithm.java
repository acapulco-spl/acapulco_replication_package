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

import org.uma.mo_dagame.algorithm.MoFmProblem;
import org.uma.mo_dagame.feature_models.Configuration;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public abstract class MoDagameAlgorithm extends Algorithm {

    public MoDagameAlgorithm(Problem problem) {
        super(problem);
    }

    abstract public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront)
            throws JMException, ClassNotFoundException;

    public MoDagameAlgorithmResult advancedExecute() throws JMException, ClassNotFoundException {
        return advancedExecute(true);
    }

    public SolutionSet execute() throws jmetal.util.JMException, java.lang.ClassNotFoundException {
        long[] data = new long[1];
        MoDagameAlgorithmResult result = advancedExecute(false);
        return result.getFinalFront();
    }

    protected SolutionSet copySolutionSet(SolutionSet solutionSet) {
        int size = solutionSet.size();
        SolutionSet copy = new SolutionSet(size);
        for (int i = 0; i < size; i++) {
            copy.add(new Solution(solutionSet.get(i)));
        }
        return copy;
    }

    protected Configuration getConfigurationFromSeed(MoFmProblem problem,
                                                     Operator mutationOperator) {
        Configuration seed = problem.getSeed();
        Solution seedSolution = problem.getSolution(seed);
        try {
            mutationOperator.execute(seedSolution);
        } catch (JMException e) {
            e.printStackTrace();
            return null;
        }
        return problem.getConfiguration(seedSolution);
    }

}
