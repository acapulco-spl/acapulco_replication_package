package acapulco.evaluation.output;

import java.util.Collection;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class Stats {
	private double[] data;
	private DescriptiveStatistics ds;
	
	public Stats(double[] data) {
		this.data = data;
		this.ds = new DescriptiveStatistics(data);
	}
	
	public Stats(Collection<Double> data) {
		this(data.stream().mapToDouble(d -> d).toArray());
	}
	
	public double getMedian() {
		return ds.getPercentile(50);
	}
	
	public double getPercentile(double p) {
		return ds.getPercentile(p);
	}
	
	public double getMean() {
		return ds.getMean();
	}
	
	public double getVariance() {
		return ds.getVariance();
	}
	
	public double getStandardDeviation() {
		return ds.getStandardDeviation();
	}
	
	public double[] getData() {
		return this.data;
	}
	
	public static double[] normalize(double[] data) {
		return StatUtils.normalize(data);
	}
	
	public static double[] normalize(Collection<Double> data) {
		return StatUtils.normalize(data.stream().mapToDouble(d -> d).toArray());
	}
}
