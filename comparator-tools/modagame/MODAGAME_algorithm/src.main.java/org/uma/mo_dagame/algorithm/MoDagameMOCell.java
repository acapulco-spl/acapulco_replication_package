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
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.Ranking;
import jmetal.util.archive.CrowdingArchive;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.DominanceComparator;

/**
 * This class represents an asynchronous version of MOCell algorithm, combining
 * aMOCell2 and aMOCell3. It is the aMOCell4 variant described in:
 * A.J. Nebro, J.J. Durillo, F. Luna, B. Dorronsoro, E. Alba
 * "Design Issues in a Multiobjective Cellular Genetic Algorithm."
 * Evolutionary Multi-Criterion Optimization. 4th International Conference,
 * EMO 2007. Sendai/Matsushima, Japan, March 2007.
 */
public class MoDagameMOCell extends MoDagameAlgorithm {

    private static final long serialVersionUID = 1L;

    public MoDagameMOCell(MoFmProblem problem) {
        super(problem);
    }

    /**
     * Execute the algorithm
     *
     * @throws JMException
     */
    public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront)
            throws JMException, ClassNotFoundException {
        //Init the parameters
        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator mutationOperator, crossoverOperator, selectionOperator, fixOperator;
        SolutionSet currentPopulation;
        MoDagameAlgorithmResult result = new MoDagameAlgorithmResult();
        CrowdingArchive archive;
        SolutionSet[] neighbors;
        Neighborhood neighborhood;
        Comparator dominance = new DominanceComparator();
        Comparator crowdingComparator = new CrowdingComparator();
        Distance distance = new Distance();
        long startTime;
        long fixTime;

        // Read the parameters
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

        // Read the operators
        mutationOperator = operators_.get("mutation");
        crossoverOperator = operators_.get("crossover");
        selectionOperator = operators_.get("selection");
        fixOperator = operators_.get("fix");

        // Initialize the variables
        currentPopulation = new SolutionSet(populationSize);
        archive = new CrowdingArchive(archiveSize, problem_.getNumberOfObjectives());
        evaluations = 0;
        neighborhood = new Neighborhood(populationSize);
        neighbors = new SolutionSet[populationSize];
        fixTime = 0;

        MoFmProblem problem = (MoFmProblem) problem_;
        // Create the initial population
        Solution individual;
        Configuration configuration;
        int i = 0;
        do {
            configuration = getConfigurationFromSeed((MoFmProblem) problem_, mutationOperator);
            startTime = System.nanoTime();
            configuration = (Configuration) fixOperator.execute(configuration);
            fixTime += System.nanoTime() - startTime;

            if (configuration != null) {
                individual = problem.getSolution(configuration);
                problem_.evaluate(individual);
                problem_.evaluateConstraints(individual);
                currentPopulation.add(individual);
                individual.setLocation(i);
                i++;
            }
            evaluations++;
        } while (i < populationSize && evaluations < maxEvaluations);


        if (evaluations == maxEvaluations) {
            SolutionSet firstFront;
            if (i > 0) {
                Ranking ranking = new Ranking(currentPopulation);
                firstFront = ranking.getSubfront(0);
            } else {
                firstFront = currentPopulation;
            }
            result.setInitialFront(firstFront);
            result.setFinalFront(firstFront);
            result.setFixOperatorTime(fixTime);
            return result;
        }

        // Copy the initial front to the result
        if (saveInitialFront) {
            long copyStartTime = System.nanoTime();
            SolutionSet copy = copySolutionSet(currentPopulation);
            Ranking ranking = new Ranking(copy);
            result.setInitialFront(ranking.getSubfront(0));
            result.setTimeToCopyInitialFront(System.nanoTime() - copyStartTime);
        }

        // Main loop
        while (evaluations < maxEvaluations) {
            for (int ind = 0; ind < currentPopulation.size(); ind++) {
                individual = new Solution(currentPopulation.get(ind));

                Solution[] parents = new Solution[2];
                Solution[] offSpring;

                //neighbors[ind] = neighborhood.getFourNeighbors(currentPopulation,ind);

                neighbors[ind] = neighborhood.getEightNeighbors(currentPopulation, ind);
                neighbors[ind].add(individual);

                // parents
                parents[0] = (Solution) selectionOperator.execute(neighbors[ind]);
                if (archive.size() > 0) {
                    parents[1] = (Solution) selectionOperator.execute(archive);
                } else {
                    parents[1] = (Solution) selectionOperator.execute(neighbors[ind]);
                }

                // Create a new individual, using genetic operators mutation and crossover
                offSpring = (Solution[]) crossoverOperator.execute(parents);
                mutationOperator.execute(offSpring[0]);

                do {
                    startTime = System.nanoTime();
                    configuration = (Configuration) fixOperator
                            .execute(problem.getConfiguration(offSpring[0]));
                    fixTime += System.nanoTime() - startTime;
                    if (configuration != null) {
                        // Evaluate individual an his constraints
                        offSpring[0] = problem.getSolution(configuration);
                        problem_.evaluate(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[0]);
                    }
                    evaluations++;
                } while (configuration == null && evaluations < maxEvaluations);

                if (configuration != null) {
                    int flag = dominance.compare(individual, offSpring[0]);

                    if (flag == 1) { //The new individual dominates
                        offSpring[0].setLocation(individual.getLocation());
                        currentPopulation.replace(offSpring[0].getLocation(), offSpring[0]);
                        archive.add(new Solution(offSpring[0]));
                    } else if (flag == 0) { //The new individual is non-dominated
                        neighbors[ind].add(offSpring[0]);
                        offSpring[0].setLocation(-1);
                        Ranking rank = new Ranking(neighbors[ind]);
                        for (int j = 0; j < rank.getNumberOfSubfronts(); j++) {
                            distance.crowdingDistanceAssignment(rank.getSubfront(j),
                                    problem_.getNumberOfObjectives());
                        }
                        Solution worst = neighbors[ind].worst(crowdingComparator);

                        if (worst.getLocation() == -1) { // The worst is the offspring
                            archive.add(new Solution(offSpring[0]));
                        } else {
                            offSpring[0].setLocation(worst.getLocation());
                            currentPopulation.replace(offSpring[0].getLocation(), offSpring[0]);
                            archive.add(new Solution(offSpring[0]));
                        }
                    }
                }

                if (evaluations == maxEvaluations)
                    break;
            }
        }

        result.setFixOperatorTime(fixTime);
        // If we have not been able to add nothing to the archive, return the first front
        if (archive.size() == 0) {
            Ranking ranking = new Ranking(currentPopulation);
            result.setFinalFront(ranking.getSubfront(0));
        } else {
            result.setFinalFront(archive);
        }

        return result;
    } // while
} // MOCell

