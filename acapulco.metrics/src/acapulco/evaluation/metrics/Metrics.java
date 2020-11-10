package acapulco.evaluation.metrics;

import java.util.ArrayList;
import java.util.List;

import org.moeaframework.core.Solution;

import jmetal.qualityIndicator.GenerationalDistance;
import jmetal.qualityIndicator.Hypervolume;

/**
 * Calculation of metrics: hypervolume (HD), generational distance (GD),...
 *
 */
public class Metrics {

	public static double gd(double[][] paretoFront, double[][] paretoTrueFront, int numberOfObjectives) {
		GenerationalDistance gd = new GenerationalDistance();
		return gd.generationalDistance(paretoFront, paretoTrueFront, numberOfObjectives);
	}
	
	public static double hypervolume(double[][] paretoFront, double[][] paretoTrueFront, int numberOfObjectives) throws Exception {
		Hypervolume hv = new Hypervolume();
		return hv.hypervolume(paretoFront, paretoTrueFront, numberOfObjectives);
	}
	
	public static double hypervolumeMOEA(double[][] paretoFront, int numberOfSolutions, int numberOfObjectives) {
		List<Solution> pfs = new ArrayList<Solution>();
		for (double[] sol : paretoFront) {
			pfs.add(new Solution(sol));
		}
		return org.moeaframework.core.indicator.Hypervolume.calculateHypervolume(pfs, numberOfSolutions, numberOfObjectives);
	}
}
