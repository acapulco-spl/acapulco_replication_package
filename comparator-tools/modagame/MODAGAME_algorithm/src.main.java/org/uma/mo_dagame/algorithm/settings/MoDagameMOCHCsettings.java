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

package org.uma.mo_dagame.algorithm.settings;

import org.uma.mo_dagame.algorithm.BasicFix;
import org.uma.mo_dagame.algorithm.MoDagameMOCHC;
import org.uma.mo_dagame.algorithm.MoFmProblem;
import org.uma.mo_dagame.feature_models.Configuration;
import org.uma.mo_dagame.feature_models.FeatureModel;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.experiments.Settings;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

public class MoDagameMOCHCsettings extends Settings {
    public int populationSize_;
    public int maxEvaluations_;

    public double crossoverProbability_;
    public double mutationProbability_;
    public double initialConvergenceCount_;
    public double preservedPopulation_;
    public int convergenceValue_;

    public MoDagameMOCHCsettings(String problemName, FeatureModel fm, Configuration seed) {
        super(problemName);

        problem_ = new MoFmProblem(fm, new boolean[]{false, true, true}, false, seed);

        // Default experiments.settings
        populationSize_ = 100;
        maxEvaluations_ = 5000;
        initialConvergenceCount_ = 0.25;
        preservedPopulation_ = 0.05;
        convergenceValue_ = 3;
        crossoverProbability_ = 0.8;
        mutationProbability_ = 0.1;
    }

    /**
     * Configure MOCHC with user-defined parameter experiments.settings
     *
     * @return A MOCHC algorithm object
     * @throws jmetal.util.JMException
     */
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Operator crossoverOperator;
        Operator mutationOperator;
        Operator parentsSelection;
        Operator newGenerationSelection;
        Operator fix;

        HashMap parameters; // Operator parameters

        // Creating the problem
        algorithm = new MoDagameMOCHC((MoFmProblem) problem_);

        // Algorithm parameters
        algorithm.setInputParameter("initialConvergenceCount", initialConvergenceCount_);
        algorithm.setInputParameter("preservedPopulation", preservedPopulation_);
        algorithm.setInputParameter("convergenceValue", convergenceValue_);
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

        // Crossover operator
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        crossoverOperator =
                CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

        parameters = null;
        parentsSelection = SelectionFactory.getSelectionOperator("RandomSelection", parameters);

        parameters = new HashMap();
        parameters.put("problem", problem_);
        newGenerationSelection =
                SelectionFactory.getSelectionOperator("RankingAndCrowdingSelection", parameters);

        // Mutation operator
        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        mutationOperator = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

        // Fix operator
        parameters = new HashMap();
        parameters
                .put(BasicFix.PARAMETER_FEATURE_MODEL, ((MoFmProblem) problem_).getFeatureModel());
        fix = new BasicFix(parameters);

        algorithm.addOperator("crossover", crossoverOperator);
        algorithm.addOperator("cataclysmicMutation", mutationOperator);
        algorithm.addOperator("parentSelection", parentsSelection);
        algorithm.addOperator("newGenerationSelection", newGenerationSelection);
        algorithm.addOperator("fix", fix);

        return algorithm;
    } // configure
}
