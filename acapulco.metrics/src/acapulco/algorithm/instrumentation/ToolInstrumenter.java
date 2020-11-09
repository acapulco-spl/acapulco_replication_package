package acapulco.algorithm.instrumentation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.collector.Collector;
import org.moeaframework.analysis.collector.ElapsedTimeCollector;

import acapulco.algorithm.instrumentation.collector.PopulationCollector;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;

public class ToolInstrumenter {

	private Accumulator accumulator;
	private Collector elapsedTimeCollector;
	private PopulationCollector approximationSetCollector;
	private PopulationCollector populationCollector;

	private int batchNumber;
	private String outputPath;
	private String toolName;
	String serializedOutputFileName = "data-steps.csv";
	Path batchOutputPath;

	public ToolInstrumenter(Integer numberOfObjectives, Integer numberOfConstraints, String toolName, String outputPath, int batchNumber) {
		this.accumulator = new Accumulator();
		this.elapsedTimeCollector = new ElapsedTimeCollector();
		this.approximationSetCollector = new PopulationCollector(numberOfObjectives, numberOfConstraints, "Approximation Set");
		this.populationCollector = new PopulationCollector(numberOfObjectives, numberOfConstraints, "Population");

		this.toolName = toolName;
		this.outputPath = outputPath;
		this.batchNumber = batchNumber;
		this.batchOutputPath = Paths.get(outputPath, "" + batchNumber);
	}

	public void collectStep(int nfe, SolutionSet archive, SolutionSet population) {
		this.collectNFE(nfe);
		this.collectApproximationSet(archive);
		this.collectPopulation(population);
		this.collectPopulationSize(population.size());
		this.collectElapsedTime();
	}

	public void collectNFE(Integer nfe) {
		this.accumulator.add("NFE", nfe);
	}

	public void collectElapsedTime() {
		this.elapsedTimeCollector.collect(accumulator);
	}

	public void collectApproximationSet(SolutionSet solutions) {
		this.approximationSetCollector.collect(this.accumulator, solutions);
	}

	public void collectPopulation(SolutionSet solutions) {
		this.populationCollector.collect(this.accumulator, solutions);
	}

	public void collectPopulationSize(Integer populationSize) {
		this.accumulator.add("Population Size", populationSize);
	}

	public void serialiseAccumulator() {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		File batchRootDirectory = this.batchOutputPath.toFile();

		File serializedOutputFile = new File(
				String.format("output/%s/%s/batch-%s-%s", timestamp, batchRootDirectory, batchNumber, this.serializedOutputFileName));

		try {

			if (!serializedOutputFile.getParentFile().exists()) {
				serializedOutputFile.getParentFile().mkdirs();
			}

			this.accumulator.saveCSV(serializedOutputFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					String.format("Unable to serialise batch csv file: %s", serializedOutputFile.getAbsolutePath()));
		}
	}

}
