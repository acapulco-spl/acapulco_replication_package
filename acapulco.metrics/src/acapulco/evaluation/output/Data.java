package acapulco.evaluation.output;

import java.util.Arrays;

public class Data {
	private int nfe;
	private double elapsedTime;
	private double[][] approximationSet;
	private double[][] population;
	private int populationSize;
	private int numberOfInvalidSolutions;
	
	public Data(int nfe, double elapsedTime, double[][] approximationSet, double[][] population, int populationSize, int invalidSolutions) {
		super();
		this.nfe = nfe;
		this.elapsedTime = elapsedTime;
		this.approximationSet = approximationSet;
		this.population = population;
		this.populationSize = populationSize;
		this.numberOfInvalidSolutions = invalidSolutions;
	}

	public int getNfe() {
		return nfe;
	}

	public void setNfe(int nfe) {
		this.nfe = nfe;
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public double[][] getApproximationSet() {
		return approximationSet;
	}

	public void setApproximationSet(double[][] approximationSet) {
		this.approximationSet = approximationSet;
	}

	public double[][] getPopulation() {
		return population;
	}

	public void setPopulation(double[][] population) {
		this.population = population;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	
	public int getNumberOfInvalidSolutions() {
		return numberOfInvalidSolutions;
	}

	public void setNumberOfInvalidSolutions(int numberOfInvalidSolutions) {
		this.numberOfInvalidSolutions = numberOfInvalidSolutions;
	}

	@Override
	public String toString() {
		return "Data [nfe=" + nfe + ", elapsedTime=" + elapsedTime + ", approximationSet="
				+ Arrays.toString(approximationSet) + ", population=" + Arrays.toString(population)
				+ ", populationSize=" + populationSize + numberOfInvalidSolutions + "]";
	}
}
