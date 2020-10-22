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

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.archive.CrowdingArchive;
import jmetal.util.comparators.CrowdingComparator;

/**
 * This class executes the MOCHC algorithm described in:
 * A.J. Nebro, E. Alba, G. Molina, F. Chicano, F. Luna, J.J. Durillo
 * "Optimal antenna placement using a new multi-objective chc algorithm".
 * GECCO '07: Proceedings of the 9th annual conference on Genetic and
 * evolutionary computation. London, England. July 2007.
 */
public class MoDagameMOCHC extends MoDagameAlgorithm {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * Creates a new instance of MOCHC
     */
    public MoDagameMOCHC(MoFmProblem problem) {
        super(problem);
    }

    /**
     * Compares two solutionSets to determine if both are equals
     *
     * @param solutionSet    A <code>SolutionSet</code>
     * @param newSolutionSet A <code>SolutionSet</code>
     * @return true if both are cotains the same solutions, false in other case
     */
    public boolean equals(SolutionSet solutionSet, SolutionSet newSolutionSet) {
        boolean found;
        for (int i = 0; i < solutionSet.size(); i++) {

            int j = 0;
            found = false;
            while (j < newSolutionSet.size()) {

                if (solutionSet.get(i).equals(newSolutionSet.get(j))) {
                    found = true;
                }
                j++;
            }
            if (!found) {
                return false;
            }
        }
        return true;
    } // equals

    /**
     * Calculate the hamming distance between two solutions
     *
     * @param solutionOne A <code>Solution</code>
     * @param solutionTwo A <code>Solution</code>
     * @return the hamming distance between solutions
     */
    public int hammingDistance(Solution solutionOne, Solution solutionTwo) {
        int distance = 0;
        for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
            distance += ((Binary) solutionOne.getDecisionVariables()[i]).
                    hammingDistance((Binary) solutionTwo.getDecisionVariables()[i]);
        }

        return distance;
    } // hammingDistance

    /**
     * Runs of the MOCHC algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions
     * as a result of the algorithm execution
     */
    public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront)
            throws JMException, ClassNotFoundException {
        int populationSize;
        int convergenceValue;
        int maxEvaluations;
        int minimumDistance;
        int evaluations;

        Comparator crowdingComparator = new CrowdingComparator();

        Operator crossover;
        Operator parentSelection;
        Operator newGenerationSelection;
        Operator cataclysmicMutation;
        Operator fixOperator;

        double preservedPopulation;
        double initialConvergenceCount;
        boolean end_condition = false;
        SolutionSet solutionSet, offspringPopulation, newPopulation;
        MoDagameAlgorithmResult result = new MoDagameAlgorithmResult();
        long startTime;
        long fixTime;

        // Read parameters
        initialConvergenceCount =
                ((Double) getInputParameter("initialConvergenceCount")).doubleValue();
        preservedPopulation = ((Double) getInputParameter("preservedPopulation")).doubleValue();
        convergenceValue = ((Integer) getInputParameter("convergenceValue")).intValue();
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();


        // Read operators
        crossover = (Operator) getOperator("crossover");
        cataclysmicMutation = (Operator) getOperator("cataclysmicMutation");
        parentSelection = (Operator) getOperator("parentSelection");
        newGenerationSelection = (Operator) getOperator("newGenerationSelection");
        fixOperator = (Operator) getOperator("fix");

        evaluations = 0;
        fixTime = 0;

        //Calculate the maximum problem sizes
        Solution aux = new Solution(problem_);
        int size = 0;
        for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
            size += ((Binary) aux.getDecisionVariables()[var]).getNumberOfBits();
        }
        minimumDistance = (int) Math.floor(initialConvergenceCount * size);

        MoFmProblem problem = (MoFmProblem) problem_;
        solutionSet = new SolutionSet(populationSize);
        int j = 0;
        while (j < populationSize && evaluations < maxEvaluations) {
            Configuration config =
                    getConfigurationFromSeed((MoFmProblem) problem_, cataclysmicMutation);
            startTime = System.nanoTime();
            config = (Configuration) fixOperator.execute(config);
            fixTime += System.nanoTime() - startTime;
            if (config != null) {
                Solution solution = problem.getSolution(config);
                problem_.evaluate(solution);
                problem_.evaluateConstraints(solution);
                solutionSet.add(solution);
                j++;
            }
            evaluations++;
        }

        end_condition = evaluations == maxEvaluations;
        if (j == 0) {
            // No solutions found, return empty solution set
            result.setInitialFront(solutionSet);
            result.setFinalFront(solutionSet);
            result.setFixOperatorTime(fixTime);
            return result;
        }

        // Copy the initial front to the result
        if (saveInitialFront) {
            long copyStartTime = System.nanoTime();
            SolutionSet copy = copySolutionSet(solutionSet);
            CrowdingArchive archive =
                    new CrowdingArchive(populationSize, problem_.getNumberOfObjectives());
            for (int i = 0; i < copy.size(); i++) {
                archive.add(copy.get(i));
            }
            result.setInitialFront(archive);
            result.setTimeToCopyInitialFront(System.nanoTime() - copyStartTime);
        }

        while (!end_condition) {
            offspringPopulation = new SolutionSet(populationSize);
            for (int i = 0; i < solutionSet.size() / 2; i++) {
                Solution[] parents = (Solution[]) parentSelection.execute(solutionSet);

                //Equality condition between solutions
                if (hammingDistance(parents[0], parents[1]) >= (minimumDistance)) {
                    Solution[] offspring = (Solution[]) crossover.execute(parents);

                    startTime = System.nanoTime();
                    Configuration config0 = (Configuration) fixOperator
                            .execute(problem.getConfiguration(offspring[0]));
                    Configuration config1 = (Configuration) fixOperator
                            .execute(problem.getConfiguration(offspring[1]));
                    fixTime += System.nanoTime() - startTime;
                    if (config0 != null) {
                        offspring[0] = problem.getSolution(config0);
                        problem_.evaluate(offspring[0]);
                        problem_.evaluateConstraints(offspring[0]);
                        offspringPopulation.add(offspring[0]);
                    }
                    if (config1 != null) {
                        offspring[1] = problem.getSolution(config1);
                        problem_.evaluate(offspring[1]);
                        problem_.evaluateConstraints(offspring[1]);
                        offspringPopulation.add(offspring[1]);
                    }
                    evaluations += 2;
                }
            }

            SolutionSet union = solutionSet.union(offspringPopulation);
            newGenerationSelection.setParameter("populationSize", populationSize);
            newPopulation = (SolutionSet) newGenerationSelection.execute(union);

            if (equals(solutionSet, newPopulation)) {
                minimumDistance--;
            }
            if (minimumDistance <= -convergenceValue) {

                minimumDistance = (int) (1.0 / size * (1 - 1.0 / size) * size);
                //minimumDistance = (int) (0.35 * (1 - 0.35) * size);

                int preserve = (int) Math.floor(preservedPopulation * populationSize);
                newPopulation = new SolutionSet(populationSize);
                solutionSet.sort(crowdingComparator);
                for (int i = 0; i < preserve; i++) {
                    newPopulation.add(new Solution(solutionSet.get(i)));
                }
                for (int i = preserve; i < populationSize; i++) {
                    Solution solution = new Solution(solutionSet.get(i));
                    cataclysmicMutation.execute(solution);
                    Configuration config = null;
                    while (config == null &&
                            evaluations < maxEvaluations) { // Be careful - Possible infinite loop
                        startTime = System.nanoTime();
                        config = (Configuration) fixOperator
                                .execute(problem.getConfiguration(solution));
                        fixTime += System.nanoTime() - startTime;
                        evaluations++;
                    }
                    if (config != null) {
                        solution = problem.getSolution(config);
                        problem_.evaluate(solution);
                        problem_.evaluateConstraints(solution);
                        newPopulation.add(solution);
                    }
                }
            }

            solutionSet = newPopulation;
            if (evaluations >= maxEvaluations) {
                end_condition = true;
            }
        }

        CrowdingArchive archive;
        archive = new CrowdingArchive(populationSize, problem_.getNumberOfObjectives());
        for (int i = 0; i < solutionSet.size(); i++) {
            archive.add(solutionSet.get(i));
        }

        result.setFinalFront(archive);
        result.setFixOperatorTime(fixTime);
        return result;
    } // execute
}  // MOCHC
