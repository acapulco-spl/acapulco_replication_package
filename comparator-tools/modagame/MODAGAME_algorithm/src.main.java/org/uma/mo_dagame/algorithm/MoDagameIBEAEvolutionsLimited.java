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

import acapulco.algorithm.instrumentation.ToolInstrumenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.DominanceComparator;

/**
 * This class implementing the IBEA algorithm
 */
public class MoDagameIBEAEvolutionsLimited extends AbstractMoDagameIBEA {

	private long stoppingValue;
	private ToolInstrumenter toolInstrumenter;

	public MoDagameIBEAEvolutionsLimited(MoFmProblem problem, long stoppingValue, ToolInstrumenter toolInstrumenter) {
		super(problem);
		this.stoppingValue = stoppingValue;
		this.toolInstrumenter = toolInstrumenter;
	}

    /**
     * Runs of the IBEA algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions
     * as a result of the algorithm execution
     * @throws JMException
     */
    public MoDagameAlgorithmResult advancedExecute(boolean saveInitialFront) throws JMException, ClassNotFoundException {
    	//System.err.println("EVOLUTIONS");
    	
        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator crossoverOperator, mutationOperator, selectionOperator, fixOperator;
        SolutionSet solutionSet, archive, offSpringSolutionSet;
        MoDagameAlgorithmResult result = new MoDagameAlgorithmResult();
        long startTime;
        long fixTime;

        //Read the params
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
        //maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        maxEvaluations = (int) stoppingValue * populationSize;
        
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
        Configuration newConfiguration;
        int i = 0;
        
        while (i < populationSize && evaluations < maxEvaluations) {
            newConfiguration = getConfigurationFromSeed((MoFmProblem) problem_, mutationOperator);
            startTime = System.nanoTime();
            newConfiguration = (Configuration)
                    fixOperator.execute(newConfiguration);
            fixTime += System.nanoTime() - startTime;

            if (newConfiguration != null) {
                newSolution = problem.getSolution(newConfiguration);
                problem_.evaluate(newSolution);
                problem_.evaluateConstraints(newSolution);
                solutionSet.add(newSolution);
                i++;
            }
            evaluations++;
        }

        //this.toolInstrumenter.collectStep(evaluations, archive, solutionSet);
        
        if (evaluations == maxEvaluations) {
            if (i > 0) {
                // Return just the generated population
                calculateFitness(solutionSet);
                archive = solutionSet;
            } else {
                // No solutions found, return empty archive
                result.setInitialFront(archive);
                result.setFinalFront(archive);
                result.setFixOperatorTime(fixTime);
                return result;
            }
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
            calculateFitness(union);
            archive = union;

            while (archive.size() > populationSize) {
                removeWorst(archive);
            }

            // Create a new offspringPopulation
            offSpringSolutionSet = new SolutionSet(populationSize);
            Solution[] parents = new Solution[2];
            while (offSpringSolutionSet.size() < populationSize && evaluations < maxEvaluations) {
                int j = 0;
                do {
                    j++;
                    parents[0] = (Solution) selectionOperator.execute(archive);
                } while (j < MoDagameIBEAEvolutionsLimited.TOURNAMENTS_ROUNDS); // do-while
                int k = 0;
                do {
                    k++;
                    parents[1] = (Solution) selectionOperator.execute(archive);
                } while (k < MoDagameIBEAEvolutionsLimited.TOURNAMENTS_ROUNDS); // do-while

                //make the crossover
                Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                mutationOperator.execute(offSpring[0]);

                // Fix the offspring
                startTime = System.nanoTime();
                Configuration config0 = (Configuration)
                        fixOperator.execute(problem.getConfiguration(offSpring[0]));
                fixTime += System.nanoTime() - startTime;

                if (config0 != null) {
                    offSpring[0] = problem.getSolution(config0);
                    problem_.evaluate(offSpring[0]);
                    problem_.evaluateConstraints(offSpring[0]);
                    offSpringSolutionSet.add(offSpring[0]);
                }
                evaluations++;
                if (evaluations % populationSize == 0) {
                	this.toolInstrumenter.collectStep(evaluations, archive, offSpringSolutionSet);
                }
            } // while

            solutionSet = offSpringSolutionSet;
            //System.out.println("TOOLS_INSTRUMENTER EVOLUTIONS> " + this.toolInstrumenter);
            if (evaluations % populationSize == 0) {
            	this.toolInstrumenter.collectStep(evaluations, archive, offSpringSolutionSet);
            }
        } // while

        this.toolInstrumenter.serialiseAccumulator();
        Ranking ranking = new Ranking(archive);
        result.setFinalFront(ranking.getSubfront(0));
        result.setFixOperatorTime(fixTime);
        return result;
    } // execute
}
