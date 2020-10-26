/*
 * Copyright 2014 Gustavo García Pascual, Mónica Pinto and Lidia Fuentes
 * Copyright 2011 Antonio J. Nebro, Juan J. Durillo
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
 * along with MO-DAGAME.  If not, see <http://www.gnu.org/licenses/>
 */

package org.uma.mo_dagame.algorithm;

import org.uma.mo_dagame.algorithm.jmetalcustomization.MoDagameAlgorithm;
import org.uma.mo_dagame.algorithm.jmetalcustomization.MoDagameAlgorithmResult;
import org.uma.mo_dagame.feature_models.Configuration;

import java.util.Comparator;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.util.JMException;
import jmetal.util.archive.AdaptiveGridArchive;
import jmetal.util.comparators.DominanceComparator;

/**
 * This class implements the PAES algorithm.
 */
public class MoDagamePAES extends MoDagameAlgorithm {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new PAES instance for resolve a problem
     *
     * @param problem Problem to solve
     */
    public MoDagamePAES(MoFmProblem problem) {
        super(problem);
    } // Paes

    /**
     * Tests two solutions to determine which one becomes be the guide of PAES
     * algorithm
     *
     * @param solution        The actual guide of PAES
     * @param mutatedSolution A candidate guide
     */
    public Solution test(Solution solution, Solution mutatedSolution, AdaptiveGridArchive archive) {

        int originalLocation = archive.getGrid().location(solution);
        int mutatedLocation = archive.getGrid().location(mutatedSolution);

        if (originalLocation == -1) {
            return new Solution(mutatedSolution);
        }

        if (mutatedLocation == -1) {
            return new Solution(solution);
        }

        if (archive.getGrid().getLocationDensity(mutatedLocation) <
                archive.getGrid().getLocationDensity(originalLocation)) {
            return new Solution(mutatedSolution);
        }

        return new Solution(solution);
    } // test

    /**
     * Runs of the Paes algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions
     * as a result of the algorithm execution
     * @throws JMException
     */
    public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront)
            throws JMException, ClassNotFoundException {
        int bisections, archiveSize, maxEvaluations, evaluations;
        AdaptiveGridArchive archive;
        Operator mutationOperator;
        Operator fixOperator;
        Comparator dominance;
        MoDagameAlgorithmResult result = new MoDagameAlgorithmResult();
        long startTime;
        long fixTime;

        //Read the params
        bisections = ((Integer) this.getInputParameter("biSections")).intValue();
        archiveSize = ((Integer) this.getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();

        //Read the operators
        mutationOperator = this.operators_.get("mutation");
        fixOperator = operators_.get("fix");

        //Initialize the variables
        evaluations = 0;
        fixTime = 0;
        archive =
                new AdaptiveGridArchive(archiveSize, bisections, problem_.getNumberOfObjectives());
        dominance = new DominanceComparator();

        MoFmProblem problem = (MoFmProblem) problem_;
        //-> Create the initial solution and evaluate it and his constraints
        Configuration configuration = null;
        Solution solution = null;
        do {
            configuration = getConfigurationFromSeed((MoFmProblem) problem_, mutationOperator);
            startTime = System.nanoTime();
            configuration = (Configuration) fixOperator.execute(configuration);
            fixTime += System.nanoTime() - startTime;
            if (configuration != null) {
                solution = problem.getSolution(configuration);
                problem_.evaluate(solution);
                problem_.evaluateConstraints(solution);
            }
            evaluations++;
        } while (configuration == null && evaluations < maxEvaluations);

        if (configuration == null) {
            // Could not find a valid solution, return empty set
            result.setInitialFront(archive);
            result.setFinalFront(archive);
            result.setFixOperatorTime(fixTime);
            return result;
        }

        // Add it to the archive
        archive.add(new Solution(solution));

        // Copy the initial front to the result
        if (saveInitialFront) {
            long copyStartTime = System.nanoTime();
            result.setInitialFront(copySolutionSet(archive));
            result.setTimeToCopyInitialFront(System.nanoTime() - copyStartTime);
        }

        //Iterations....
        do {
            // Create the mutate one
            Solution mutatedIndividual;
            Configuration mutatedConfiguration;
            do {
                mutatedIndividual = new Solution(solution);
                mutationOperator.execute(mutatedIndividual);
                startTime = System.nanoTime();
                mutatedConfiguration = (Configuration) fixOperator
                        .execute(problem.getConfiguration(mutatedIndividual));
                fixTime += System.nanoTime() - startTime;
                if (mutatedConfiguration != null) {
                    mutatedIndividual = problem.getSolution(mutatedConfiguration);
                    problem_.evaluate(mutatedIndividual);
                    problem_.evaluateConstraints(mutatedIndividual);
                }
                evaluations++;
            } while (mutatedConfiguration == null && evaluations < maxEvaluations);

            if (mutatedConfiguration != null) {
                // Check dominance
                int flag = dominance.compare(solution, mutatedIndividual);

                if (flag == 1) { //If mutate solution dominate
                    solution = new Solution(mutatedIndividual);
                    archive.add(mutatedIndividual);
                } else if (flag == 0) { //If none dominate the other
                    if (archive.add(mutatedIndividual)) {
                        solution = test(solution, mutatedIndividual, archive);
                    }
                }
            }
        } while (evaluations < maxEvaluations);

        result.setFinalFront(archive);
        result.setFixOperatorTime(fixTime);
        return result;
    }  // execute
} // PAES
