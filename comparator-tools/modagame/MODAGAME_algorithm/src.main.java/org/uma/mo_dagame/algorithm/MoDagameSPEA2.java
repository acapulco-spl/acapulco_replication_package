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

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;

/**
 * This class representing the SPEA2 algorithm
 */
public class MoDagameSPEA2 extends MoDagameAlgorithm {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Defines the number of tournaments for creating the mating pool
     */
    public static final int TOURNAMENTS_ROUNDS = 1;

    /**
     * Constructor.
     * Create a new SPEA2 instance
     *
     * @param problem Problem to solve
     */
    public MoDagameSPEA2(Problem problem) {
        super(problem);
    } // Spea2

    /**
     * Runs of the Spea2 algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions
     * as a result of the algorithm execution
     * @throws JMException
     */
    public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront)
            throws JMException, ClassNotFoundException {
        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator crossoverOperator, mutationOperator, selectionOperator, fixOperator;
        SolutionSet solutionSet, archive, offSpringSolutionSet;
        MoDagameAlgorithmResult result = new MoDagameAlgorithmResult();
        long startTime;
        long fixTime;

        //Read the params
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

        //Read the operators
        crossoverOperator = operators_.get("crossover");
        mutationOperator = operators_.get("mutation");
        selectionOperator = operators_.get("selection");
        fixOperator = operators_.get("fix");

        //Initialize the variables
        solutionSet = new SolutionSet(populationSize);
        archive = new SolutionSet(archiveSize);
        evaluations = 0;
        fixTime = 0;

        MoFmProblem problem = (MoFmProblem) problem_;
        //-> Create the initial solutionSet
        Solution newSolution;
        Configuration newConfig;
        int i = 0;
        while (i < populationSize && evaluations < maxEvaluations) {
            newConfig = getConfigurationFromSeed((MoFmProblem) problem_, mutationOperator);
            startTime = System.nanoTime();
            newConfig = (Configuration) fixOperator.execute(newConfig);
            fixTime += System.nanoTime() - startTime;
            if (newConfig != null) {
                newSolution = problem.getSolution(newConfig);
                problem_.evaluate(newSolution);
                problem_.evaluateConstraints(newSolution);
                solutionSet.add(newSolution);
                i++;
            }
            evaluations++;
        }

        // Copy the initial front to the result
        if (saveInitialFront) {
            long copyStartTime = System.nanoTime();
            SolutionSet copy = copySolutionSet(solutionSet);
            Ranking ranking = new Ranking(copy);
            result.setInitialFront(ranking.getSubfront(0));
            result.setTimeToCopyInitialFront(System.nanoTime() - copyStartTime);
        }

        while (evaluations < maxEvaluations) {
            SolutionSet union = solutionSet.union(archive);
            Spea2Fitness spea = new Spea2Fitness(union);
            spea.fitnessAssign();
            archive = spea.environmentalSelection(archiveSize);
            // Create a new offspringPopulation
            offSpringSolutionSet = new SolutionSet(populationSize);
            Solution[] parents = new Solution[2];
            while (offSpringSolutionSet.size() < populationSize && evaluations < maxEvaluations) {
                int j = 0;
                do {
                    j++;
                    parents[0] = (Solution) selectionOperator.execute(archive);
                } while (j < MoDagameSPEA2.TOURNAMENTS_ROUNDS); // do-while
                int k = 0;
                do {
                    k++;
                    parents[1] = (Solution) selectionOperator.execute(archive);
                } while (k < MoDagameSPEA2.TOURNAMENTS_ROUNDS); // do-while

                //make the crossover
                Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                mutationOperator.execute(offSpring[0]);
                startTime = System.nanoTime();
                Configuration config =
                        (Configuration) fixOperator.execute(problem.getConfiguration(offSpring[0]));
                fixTime += System.nanoTime() - startTime;
                if (config != null) {
                    offSpring[0] = problem.getSolution(config);
                    problem_.evaluate(offSpring[0]);
                    problem_.evaluateConstraints(offSpring[0]);
                    offSpringSolutionSet.add(offSpring[0]);
                }
                evaluations++;
            } // while
            // End Create a offSpring solutionSet
            solutionSet = offSpringSolutionSet;
        } // while

        if (archive.size() == 0) {
            // Didn't go into the loop
            Spea2Fitness spea = new Spea2Fitness(solutionSet);
            spea.fitnessAssign();
            archive = spea.environmentalSelection(archiveSize);
        }

        Ranking ranking = new Ranking(archive);
        result.setFinalFront(ranking.getSubfront(0));
        result.setFixOperatorTime(fixTime);
        return result;
    } // execute
} // SPEA2
