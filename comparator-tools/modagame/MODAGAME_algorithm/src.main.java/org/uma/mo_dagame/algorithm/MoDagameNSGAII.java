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
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;


public class MoDagameNSGAII extends MoDagameAlgorithm {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public MoDagameNSGAII(MoFmProblem problem) {
        super(problem);
    } // NSGAII

    /**
     * Runs the MO-DAGAME algorithm (NSGA-II version).
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions
     * as a result of the algorithm execution
     * @throws JMException
     */
    public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront)
            throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;
        int evaluations;
        MoDagameAlgorithmResult result = new MoDagameAlgorithmResult();

        QualityIndicator indicators; // QualityIndicator object
        int requiredEvaluations; // Use in the example of use of the
        // indicators object (see below)

        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;

        Operator mutationOperator;
        Operator crossoverOperator;
        Operator selectionOperator;
        Operator fixOperator;

        Distance distance = new Distance();

        long startTime;
        long fixTime;

        //Read the parameters
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        indicators = (QualityIndicator) getInputParameter("indicators");

        // Initialize the variables
        population = new SolutionSet(populationSize);
        evaluations = 0;
        fixTime = 0;
        requiredEvaluations = 0;

        // Read the operators
        mutationOperator = operators_.get("mutation");
        crossoverOperator = operators_.get("crossover");
        selectionOperator = operators_.get("selection");
        fixOperator = operators_.get("fix");

        MoFmProblem problem = (MoFmProblem) problem_;

        // Create the initial solutionSet
        Solution newSolution;
        Configuration newConfiguration;
        int j = 0;
        do {
            newConfiguration = getConfigurationFromSeed((MoFmProblem) problem_, mutationOperator);
            startTime = System.nanoTime();
            newConfiguration = (Configuration) fixOperator.execute(newConfiguration);
            fixTime += System.nanoTime() - startTime;
            if (newConfiguration != null) {
                newSolution = problem.getSolution(newConfiguration);
                problem_.evaluate(newSolution);
                problem_.evaluateConstraints(newSolution);
                population.add(newSolution);
                j++;
            }
            evaluations++;
        } while (j < populationSize && evaluations < maxEvaluations);

        if (j == 0) {
            // No solutions found, return empty solution set
            result.setInitialFront(population);
            result.setFinalFront(population);
            result.setFixOperatorTime(fixTime);
            return result;
        }

        // Copy the initial front to the result
        if (saveInitialFront) {
            long copyStartTime = System.nanoTime();
            SolutionSet copy = copySolutionSet(population);
            Ranking ranking = new Ranking(copy);
            result.setInitialFront(ranking.getSubfront(0));
            result.setTimeToCopyInitialFront(System.nanoTime() - copyStartTime);
        }

        // Generations
        while (evaluations < maxEvaluations) {

            // Create the offSpring solutionSet
            offspringPopulation = new SolutionSet(populationSize);
            Solution[] parents = new Solution[2];
            for (int i = 0; i < (populationSize / 2); i++) {
                if (evaluations < maxEvaluations) {
                    //obtain parents
                    parents[0] = (Solution) selectionOperator.execute(population);
                    parents[1] = (Solution) selectionOperator.execute(population);
                    Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                    mutationOperator.execute(offSpring[0]);
                    mutationOperator.execute(offSpring[1]);
                    startTime = System.nanoTime();
                    Configuration config0 = (Configuration) fixOperator
                            .execute(problem.getConfiguration(offSpring[0]));
                    Configuration config1 = (Configuration) fixOperator
                            .execute(problem.getConfiguration(offSpring[1]));
                    fixTime += System.nanoTime() - startTime;
                    if (config0 != null) {
                        offSpring[0] = problem.getSolution(config0);
                        problem_.evaluate(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[0]);
                        offspringPopulation.add(offSpring[0]);
                    }
                    if (config1 != null) {
                        offSpring[1] = problem.getSolution(config1);
                        problem_.evaluate(offSpring[1]);
                        problem_.evaluateConstraints(offSpring[1]);
                        offspringPopulation.add(offSpring[1]);
                    }
                    evaluations += 2;
                } // if
            } // for

            // Create the solutionSet union of solutionSet and offSpring
            union = ((SolutionSet) population).union(offspringPopulation);

            // Ranking the union
            Ranking ranking = new Ranking(union);

            int remain = populationSize;
            int index = 0;
            SolutionSet front = null;
            population.clear();

            // Obtain the next front
            front = ranking.getSubfront(index);

            while ((remain > 0) && (remain >= front.size())) {
                //Assign crowding distance to individuals
                distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                //Add the individuals of this front
                for (int k = 0; k < front.size(); k++) {
                    population.add(front.get(k));
                } // for

                //Decrement remain
                remain = remain - front.size();

                //Obtain the next front
                index++;
                if (remain > 0) {
                    front = ranking.getSubfront(index);
                } // if
            } // while

            // Remain is less than front(index).size, insert only the best one
            if (remain > 0) {  // front contains individuals to insert
                distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                front.sort(new CrowdingComparator());
                for (int k = 0; k < remain; k++) {
                    population.add(front.get(k));
                } // for

                remain = 0;
            } // if

            // This piece of code shows how to use the indicator object into the code
            // of NSGA-II. In particular, it finds the number of evaluations required
            // by the algorithm to obtain a Pareto front with a hypervolume higher
            // than the hypervolume of the true Pareto front.
            if ((indicators != null) && (requiredEvaluations == 0)) {
                double HV = indicators.getHypervolume(population);
                if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                    requiredEvaluations = evaluations;
                } // if
            } // if
        } // while

        // Return as output parameter the required evaluations
        setOutputParameter("evaluations", requiredEvaluations);

        // Return the first non-dominated front
        Ranking ranking = new Ranking(population);

        SolutionSet firstFront = null;
        if (ranking.getNumberOfSubfronts() > 0) {
            firstFront = ranking.getSubfront(0);
        } else {
            System.err.println("Could not find valid solutions");
        }

        result.setFinalFront(firstFront);
        result.setFixOperatorTime(fixTime);
        return result;
    } // execute
} // NSGA-II
