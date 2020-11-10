package acapulco.evaluation.output;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.primitives.Doubles;

import acapulco.evaluation.metrics.Metrics;

public class Results {
	private Map<Integer, Map<Integer, Data>> results; // Run -> (NFE -> Data).
	
	public Results() {
		results = new HashMap<Integer, Map<Integer, Data>>();
	}
	
	public void addRun(Integer run, Map<Integer, Data> result) {
		results.put(run,  new TreeMap<Integer, Data>(result));
	}
	
	public double getTime(int run, int nfe) {
		Map<Integer, Data> nfeData = results.get(run);
		if (!nfeData.containsKey(nfe)) {
			return 0.0;
		}
		return nfeData.get(nfe).getElapsedTime();
	}
	
	public double[] getTimes(int nfe) {
		List<Double> times = new ArrayList<Double>(results.size());
		for (Integer run : results.keySet()) {
			times.add(getTime(run, nfe));
		}
		return Doubles.toArray(times);
	}
	
	public double[][] getParetoFront(int run, int nfe) {
		Map<Integer, Data> nfeData = results.get(run);
		if (!nfeData.containsKey(nfe)) {
			return null;
		}
		return nfeData.get(nfe).getApproximationSet();
	}
	
	public int getNumberOfSolutions(int run, int nfe) {
		return getParetoFront(run, nfe).length;
	}

	public int getNumberOfObjectives(int run, int nfe) {
		return getParetoFront(run, nfe)[0].length;
	}
	
	public double getHypervolume(int run, int nfe) {
		double[][] pf = getParetoFront(run, nfe);
		if (pf == null || pf.length == 0) {
			return 0.0;
		}
		//System.out.println("PF: " + Metrics.getParetoFrontRepresentation(pf));
		return Metrics.hypervolumeMOEA(pf, pf.length, pf[0].length);
	}
	
	public double[] getHypervolumes(int nfe) {
		List<Double> hvs = new ArrayList<Double>(results.size());
		for (Integer run : results.keySet()) {
			hvs.add(getHypervolume(run, nfe));
		}
		return Doubles.toArray(hvs);
	}
	
	public double getHypervolume(int run, int nfe, double[][] paretotruefront) throws Exception {
		double[][] pf = getParetoFront(run, nfe);
		if (pf == null || pf.length == 0) {
			return 0.0;
		}
		//System.out.println("PF: " + run + "," + nfe + ", " + Metrics.getParetoFrontRepresentation(pf));
		return Metrics.hypervolume(pf, paretotruefront, pf[0].length);
	}
	
	public double[] getHypervolumes(int nfe, double[][] paretotruefront) throws Exception {
		List<Double> hvs = new ArrayList<Double>(results.size());
		for (Integer run : results.keySet()) {
			double[][] pf = getParetoFront(run, nfe);
			/*if (pf != null && pf.length > 0) {
				hvs.add(getHypervolume(run, nfe, paretotruefront));
			}*/
			if (pf == null || pf.length == 0) {
				hvs.add(0.0);
			} else {
				hvs.add(getHypervolume(run, nfe, paretotruefront));
			}
			/*if (Metrics.checkIfParetoTrueFrontValid(pf, paretotruefront)) {
				hvs.add(getHypervolume(run, nfe, paretotruefront));
			}*/
		}
		return Doubles.toArray(hvs);
	}
	
	public double getGenerationalDistance(int run, int nfe, double[][] paretotruefront) {
		double[][] pf = getParetoFront(run, nfe);
		if (pf == null || pf.length == 0) {
			return 0.0;
		}
		return Metrics.gd(pf, paretotruefront, pf[0].length);
	}
	
	public double[] getGenerationalDistances(int nfe, double[][] paretotruefront) {
		List<Double> gds = new ArrayList<Double>(results.size());
		for (Integer run : results.keySet()) {
			gds.add(getGenerationalDistance(run, nfe, paretotruefront));
		}
		return Doubles.toArray(gds);
	}

	/**
	 * Serialize the results for each NFE (evolutions).
	 * 
	 * @param filepath
	 */
	public void saveResults(String filepath) {
		Path path = Paths.get(filepath);
		
		try {
			Files.deleteIfExists(path);
		
			if (path.getParent() != null) {
				Files.createDirectories(path.getParent());	
			}
			Files.createFile(path);
			
			FileOutputStream fos = new FileOutputStream(path.toFile());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			String[] headers = {"NFE", "Runs", 
								"Time Median (s)", "Time Mean (s)", "Time SD (s)", 
								"Hypervolume Median", "Hypervolume Mean", "Hypervolume SD",
								"Solutions", "ParetoFront"};
			
			String headerLine = String.join(",", headers);
			bw.write(headerLine);
			bw.newLine();
			
			int maxRun = Collections.max(results.keySet());
			for (int nfe : results.get(maxRun).keySet()) {
				double[] times = getTimes(nfe);
				double[] hvs = getHypervolumes(nfe);
				double[][] pf = getParetoFront(maxRun, nfe);
				int sols = getNumberOfSolutions(maxRun, nfe);
			
				Stats timeStats = new Stats(times);
				Stats hvStats = new Stats(hvs);
				
				// CSV Line
				String line = String.join(",", 
										  String.valueOf(nfe), 
										  String.valueOf(maxRun),
										  String.valueOf(timeStats.getMedian()),
										  String.valueOf(timeStats.getMean()),
										  String.valueOf(timeStats.getStandardDeviation()),
										  String.valueOf(hvStats.getMedian()),
										  String.valueOf(hvStats.getMean()),
										  String.valueOf(hvStats.getStandardDeviation()),		
										  String.valueOf(sols),
										  getParetoFrontRepresentation(pf));
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Serialize the results for each NFE (evolutions) with a provided paretotruefront.
	 * It includes the calculation of the generation distance.
	 * 
	 * @param filepath
	 * @param paretotruefront
	 * @throws Exception 
	 */
	public void saveResults(String filepath, double[][] paretotruefront) throws Exception {
		Path path = Paths.get(filepath);
		
		try {
			Files.deleteIfExists(path);
		
			if (path.getParent() != null) {
				Files.createDirectories(path.getParent());	
			}
			Files.createFile(path);
			
			FileOutputStream fos = new FileOutputStream(path.toFile());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			String[] headers = {"NFE", "Runs", 
								"Time Median (s)", "Time Mean (s)", "Time SD (s)", 
								"HV Median", "HV Mean", "HV SD",
								"GD Median", "GD Mean", "GD SD",
								"Solutions", "ParetoFront"};
			
			String headerLine = String.join(",", headers);
			bw.write(headerLine);
			bw.newLine();
			
			int maxRun = Collections.max(results.keySet());
			for (int nfe : results.get(maxRun).keySet()) {
				double[][] pf = getParetoFront(maxRun, nfe);
				//if (Metrics.checkIfParetoTrueFrontValid(pf, paretotruefront)) {
					double[] times = getTimes(nfe);
					double[] hvs = getHypervolumes(nfe, paretotruefront);
					double[] gds = getGenerationalDistances(nfe, paretotruefront);
					int sols = getNumberOfSolutions(maxRun, nfe);
				
					Stats timeStats = new Stats(times);
					Stats hvStats = new Stats(hvs);
					Stats gdStats = new Stats(gds);
					
					// CSV Line
					String line = String.join(",", 
											  String.valueOf(nfe), 
											  String.valueOf(maxRun),
											  String.valueOf(timeStats.getMedian()),
											  String.valueOf(timeStats.getMean()),
											  String.valueOf(timeStats.getStandardDeviation()),
	//										  String.valueOf(""),
	//										  String.valueOf(""),
	//										  String.valueOf(""),
	//										  String.valueOf(""),
	//										  String.valueOf(""),
	//										  String.valueOf(""),
											  String.valueOf(hvStats.getMedian()),
											  String.valueOf(hvStats.getMean()),
											  String.valueOf(hvStats.getStandardDeviation()),		
											  String.valueOf(gdStats.getMedian()),
											  String.valueOf(gdStats.getMean()),
											  String.valueOf(gdStats.getStandardDeviation()),	
											  String.valueOf(sols),
											  getParetoFrontRepresentation(pf));
					bw.write(line);
					bw.newLine();
				//}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Serialize the results for each runs, with the latest number of NFE (evolutions).
	 * 
	 * @param filepath
	 */
	public void saveRunsResults(String filepath) {
		Path path = Paths.get(filepath);
		
		try {
			Files.deleteIfExists(path);
		
			if (path.getParent() != null) {
				Files.createDirectories(path.getParent());	
			}
			Files.createFile(path);
			
			FileOutputStream fos = new FileOutputStream(path.toFile());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			String[] headers = {"Run", "NFE", 
								"Time (s)", 
								"Hypervolume",
								"Solutions", "ParetoFront"};
			
			String headerLine = String.join(",", headers);
			bw.write(headerLine);
			bw.newLine();
			
			for (int run : results.keySet()) {
				int nfe = Collections.max(results.get(run).keySet());
				double time = getTime(run, nfe);
				double hv = getHypervolume(run, nfe);
				double[][] pf = getParetoFront(run, nfe);
				int sols = getNumberOfSolutions(run, nfe);
				
				// CSV Line
				String line = String.join(",", 
										  String.valueOf(run),
										  String.valueOf(nfe), 
										  String.valueOf(time),
										  String.valueOf(hv),
										  String.valueOf(sols),
										  getParetoFrontRepresentation(pf));
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Serialize the results for each runs, with the latest number of NFE (evolutions).
	 * It includes the calculation of the generation distance.
	 * 
	 * @param filepath
	 * @throws Exception 
	 */
	public void saveRunsResults(String filepath, double[][] paretotruefront) throws Exception {
		Path path = Paths.get(filepath);
		
		try {
			Files.deleteIfExists(path);
		
			if (path.getParent() != null) {
				Files.createDirectories(path.getParent());	
			}
			Files.createFile(path);
			
			FileOutputStream fos = new FileOutputStream(path.toFile());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			String[] headers = {"Run", "NFE", 
								"Time (s)", 
								"HV",
								"GD",
								"Solutions", "ParetoFront"};
			
			String headerLine = String.join(",", headers);
			bw.write(headerLine);
			bw.newLine();
			
			for (int run : results.keySet()) {
				int nfe = Collections.max(results.get(run).keySet());
				double[][] pf = getParetoFront(run, nfe);
				//if (Metrics.checkIfParetoTrueFrontValid(pf, paretotruefront)) {
					double time = getTime(run, nfe);
					double hv = getHypervolume(run, nfe, paretotruefront);
					double gd = getGenerationalDistance(run, nfe, paretotruefront);
					
					int sols = getNumberOfSolutions(run, nfe);
					
					// CSV Line
					String line = String.join(",", 
											  String.valueOf(run),
											  String.valueOf(nfe), 
											  String.valueOf(time),
	//										  String.valueOf(""),
	//										  String.valueOf(""),
											  String.valueOf(hv),
											  String.valueOf(gd),
											  String.valueOf(sols),
											  getParetoFrontRepresentation(pf));
					bw.write(line);
					bw.newLine();
				//}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getParetoFrontRepresentation(double[][] pf) {
		String sPF = "\"[]\"";
		if (pf != null) {
			sPF = "\"[";
			for (int r = 0; r < pf.length; r++) {
				sPF += "[";	
				for (int c = 0; c < pf[r].length; c++) {
					sPF += pf[r][c] + ",";
				}
				sPF = sPF.substring(0, sPF.length()-1) + "],";
			}
			if (sPF.length() > 2) {
				sPF = sPF.substring(0, sPF.length()-1) + "]\"";
			} else {
				sPF += "]\"";
			}
			
		}
		return sPF;
	}
	
	public static double[][] solutionSetToArray(String solutionSet) {
		if (solutionSet.equals("[]")) {
			return null;
		}
		String[] sols = solutionSet.split(", ");
		double[][] results = new double[sols.length][];
		for (int s = 0; s < sols.length; s++) { 
			String[] values = sols[s].split(",");
			results[s] = new double[values.length];
			for (int v = 0; v < values.length; v++) {
				String val = values[v].replaceAll("\\[", "").replaceAll("\\]", "");
				results[s][v] = Double.parseDouble(val);
			}
		}
		return results;
	}
}
