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
import org.uma.mo_dagame.algorithm.MoDagamePAES;
import org.uma.mo_dagame.algorithm.MoFmProblem;
import org.uma.mo_dagame.feature_models.Configuration;
import org.uma.mo_dagame.feature_models.FeatureModel;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.experiments.Settings;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;

/**
 * Settings class of algorithm PAES
 */
public class MoDagamePAESsettings extends Settings {

    public int maxEvaluations_;
    public int archiveSize_;
    public int biSections_;
    public double mutationProbability_;

    /**
     * Constructor
     */
    public MoDagamePAESsettings(String problem, FeatureModel fm, Configuration seed) {
        super(problem);
        System.out.println("PROBLEM: " + problem);

        problem_ = new MoFmProblem(fm, new boolean[]{false, true, true}, false, seed);

        // Default experiments.settings
        maxEvaluations_ = 5000;
        archiveSize_ = 100;
        biSections_ = 5;
        mutationProbability_ = 0.1;
    } // PAES_Settings

    /**
     * Configure the MOCell algorithm with default parameter experiments.settings
     *
     * @return an algorithm object
     * @throws jmetal.util.JMException
     */
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Mutation mutation;
        Operator fix;

        HashMap parameters; // Operator parameters

        // Creating the problem
        algorithm = new MoDagamePAES((MoFmProblem) problem_);

        // Algorithm parameters
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
        algorithm.setInputParameter("biSections", biSections_);
        algorithm.setInputParameter("archiveSize", archiveSize_);

        // Mutation (Real variables)
        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

        // Fix operator
        parameters = new HashMap();
        parameters
                .put(BasicFix.PARAMETER_FEATURE_MODEL, ((MoFmProblem) problem_).getFeatureModel());
        fix = new BasicFix(parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("fix", fix);

        return algorithm;
    } // configure
} // PAES_Settings
