package mdeoptimiser4efm.algorithm.instrumentation.collector;

import java.util.ArrayList;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.collector.AttachPoint;
import org.moeaframework.analysis.collector.Collector;
import org.moeaframework.core.EvolutionaryAlgorithm;
import org.moeaframework.core.Population;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import mdeoptimiser4efm.algorithm.instrumentation.MoeaOptimisationSolution;

/** Collects the population from an {@link EvolutionaryAlgorithm}. */
public class PopulationCollector {

	protected String key;
	private Integer numberOfObjectives;
	private Integer numberOfConstraints;

	public PopulationCollector(Integer numberOfObjectives, Integer numberOfConstraints, String key) {
		this.numberOfObjectives = numberOfObjectives;
		this.numberOfConstraints = numberOfConstraints;
		this.key = key;
	}

	public void collect(Accumulator accumulator, SolutionSet solutions) {
		ArrayList<org.moeaframework.core.Solution> list = new ArrayList<org.moeaframework.core.Solution>();

		solutions.iterator().forEachRemaining(solution -> {
			org.moeaframework.core.Solution lightCopy = new MoeaOptimisationSolution(
					this.numberOfObjectives, 0);
			lightCopy.setConstraints(new double[this.numberOfConstraints]);
			
			//TODO In some JMetal versions there is no API to read the number of constraints and their values for a solution. 
			//Only the number of invalidated constraints is available. 
			double[] objectives = new double[this.numberOfObjectives];

			for (int i = 0; i < this.numberOfObjectives; i++) {
				objectives[i] = solution.getObjective(i);
			}

			lightCopy.setObjectives(objectives);

			list.add(lightCopy);
		});

		accumulator.add(key, list);
	}

}
