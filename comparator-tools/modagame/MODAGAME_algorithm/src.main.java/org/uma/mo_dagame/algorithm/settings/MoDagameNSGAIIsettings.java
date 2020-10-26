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
import org.uma.mo_dagame.algorithm.MoDagameNSGAII;
import org.uma.mo_dagame.algorithm.MoFmProblem;
import org.uma.mo_dagame.feature_models.Configuration;
import org.uma.mo_dagame.feature_models.FeatureModel;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

/**
 * Settings class of algorithm NSGA-II (real encoding)
 */
public class MoDagameNSGAIIsettings extends Settings {

    public int populationSize_;
    public int maxEvaluations_;
    public double mutationProbability_;
    public double crossoverProbability_;

    /**
     * Constructor
     */
    public MoDagameNSGAIIsettings(String problem, FeatureModel fm, Configuration seed) {
        super(problem);

        problem_ = new MoFmProblem(fm, new boolean[]{false, true, true}, false, seed);

        // Default experiments.settings
        populationSize_ = 100;
        maxEvaluations_ = 5000;
        mutationProbability_ = 0.1;
        crossoverProbability_ = 0.8;
    } // NSGAII_Settings


    /**
     * Configure NSGAII with user-defined parameter experiments.settings
     *
     * @return A NSGAII algorithm object
     * @throws jmetal.util.JMException
     */
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Selection selection;
        Crossover crossover;
        BasicFix fix;
        Mutation mutation;

        HashMap parameters; // Operator parameters

        // Creating the algorithm. There are two choices: NSGAII and its steady-
        // state variant ssNSGAII
        algorithm = new MoDagameNSGAII((MoFmProblem) problem_);
        //algorithm = new ssNSGAII(problem_) ;

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

        // Mutation and Crossover for Real codification
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

        // Selection Operator
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

        // Fix operator
        parameters = new HashMap();
        parameters
                .put(BasicFix.PARAMETER_FEATURE_MODEL, ((MoFmProblem) problem_).getFeatureModel());
        fix = new BasicFix(parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);
        algorithm.addOperator("fix", fix);

        return algorithm;
    } // configure
} // NSGAII_Settings
