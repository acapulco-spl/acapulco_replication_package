package acapulco.evaluation.output;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import au.com.bytecode.opencsv.CSVReader;

public class ParseResults {

	public static void main(String[] args) {
		String path = "";				// input folder of the case study
		int runs = 30;					// number of runs performed
		String toolName = "MODAGAME";	// name of the tool
		String ptfFile = "...";			// filepath for the pareto true front (or null to parse results for the first time).
		
		/**********************************************************************/
		
		Results results = new Results();
		List<String> resultFilepaths = getAllResultsFilepath(path, toolName, runs);
    	for (int i = 1; i <= runs; i++) {
    		results.addRun(i, getResults(resultFilepaths.get(i-1), toolName));
    	}
    	
        // Serialize the results
        System.out.println("Saving results...");
        String fmName = new File(path).getName();
        String statsResultsFilename = toolName + "_" + fmName + "_statsResults.dat";
        String runsResultsFilename = toolName + "_" + fmName + "_" + runs + "runs_results.dat"; 
        if (ptfFile == null) {
        	results.saveResults(statsResultsFilename);
            results.saveRunsResults(runsResultsFilename);
        } else {
			try {
				String ptfSolutionSet = Files.readString(Paths.get(ptfFile));
				double[][] ptf = Results.solutionSetToArray(ptfSolutionSet);
				results.saveResults(statsResultsFilename, ptf);
	            results.saveRunsResults(runsResultsFilename, ptf);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        System.out.println("Stats results saved in " + statsResultsFilename);
        System.out.println("Runs results saved in " + runsResultsFilename);
    	
	}
	
	/**
	 * Return all .csv files with the output results. It returns only the latest X files where X is the number of runs.
	 *  
	 * @param path
	 * @param tool
	 * @param runs
	 * @return
	 */
	public static List<String> getAllResultsFilepath(String path, String tool, int runs) {
		// Get all files of results (.csv)
		List<File> files = finder(path, ".csv", true);
		// Filter the results files with the appropriate name
		files = files.stream().filter(f -> f.getName().contains("data-steps")).collect(Collectors.toList());
		// Filter by the tool
		files = files.stream().filter(f -> f.getPath().contains(tool)).collect(Collectors.toList());
		// Get the newest files of results
		List<File> newestFiles = findNewestFiles(files);
		
		return newestFiles.subList(0, runs).stream().map(f -> f.getPath()).collect(Collectors.toList());
	}
	
	public static Map<Integer, Data> getResults(String filepath, String tool) {
		Map<Integer, Data> results = new HashMap<Integer, Data>();
		try {
			FileReader fr = new FileReader(new File(filepath));
			CSVReader csvReader = new CSVReader(fr, ',', '"');
			String[] line = csvReader.readNext();
			while ((line = csvReader.readNext()) != null) {
				int nfe = Integer.parseInt(line[0].trim());
				double elapsedTime = Double.parseDouble(line[1].trim());
				double[][] approximationSet = null;
				double[][] population = null;
				int invalidSolutions = 0;
				if (tool.equalsIgnoreCase("modagame")) {
					approximationSet = solutionSetToArraySimple(line[2].trim());
					population = solutionSetToArraySimple(line[3].trim());
				} else {
					approximationSet = solutionSetToArray(line[2].trim());
					population = solutionSetToArray(line[3].trim());	
					invalidSolutions = getNumberOfInvalidSolutions(line[2].trim());
				}
				
				int populationSize = Integer.parseInt(line[4].trim());
			
				Data data = new Data(nfe, elapsedTime, approximationSet, population, populationSize, invalidSolutions);
				results.put(nfe, data);
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public static double[][] solutionSetToArraySimple(String solutionSet) {
		if (solutionSet.equals("[]")) {
			return null;
		}
		solutionSet = solutionSet.replaceAll("\"", "");
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
	
	public static double[][] solutionSetToArray(String solutionSet) {
		if (solutionSet.equals("[]")) {
			return null;
		}
		String[] sols = solutionSet.split(", ");
		ArrayList<double[]> results = new ArrayList<>();
		for (int s = 0; s < sols.length; s++) {
			String[] values = sols[s].split(",");
			if (values[0].equals("[0.0")) {
				double[] entry = new double[values.length-2];
				for (int v = 2; v < values.length; v++) {
					String val = values[v].replaceAll("\\[", "").replaceAll("\\]", "");
					entry[v-2] = Double.parseDouble(val);
				}
				results.add(entry);
			}
		}
		return results.toArray(new double[results.size()][]);
	}
	
	public static int getNumberOfInvalidSolutions(String solutionSet) {
		if (solutionSet.equals("[]")) {
			return 0;
		}
		int invalidSolutions = 0;
		String[] sols = solutionSet.split(", ");
		for (int s = 0; s < sols.length; s++) {
			String[] values = sols[s].split(",");
			if (!values[0].equals("[0.0")) {
				invalidSolutions += 1;
			}
		}
		return invalidSolutions;
	}
	
	/**
	 * Find all files with a given extension in the specified directory.
	 * 
	 * @param dirName
	 * @param extension	(e.g., ".txt")
	 * @param recursive	Look directory recursively
	 * @return
	 */
	public static List<File> finder(String dirName, String extension, boolean recursive){
        File dir = new File(dirName);
        
        List<File> files = Arrays.asList(dir.listFiles()).stream().filter(f -> f.getName().endsWith(extension)).collect(Collectors.toList());
        
        if (recursive) {
        	List<File> dirs = Arrays.asList(dir.listFiles()).stream().filter(f -> f.isDirectory()).collect(Collectors.toList());
        	for (File d : dirs) {
            	files.addAll(finder(d.getPath(), extension, recursive));
            }
        }
        return files;
    }
	
	/**
	 * Find the newest file from the specified list of file.
	 * 
	 * @param files
	 * @return
	 */
	public static File findNewestFile(List<File> files) {
		if (files.size() < 1) {
			return null;
		}
		return files.stream().sorted(Comparator.comparing(File::lastModified).reversed()).collect(Collectors.toList()).get(0);
	}
	
	/**
	 * Find the newest file from the specified list of file.
	 * 
	 * @param files
	 * @return
	 */
	public static List<File> findNewestFiles(List<File> files) {
		if (files.size() < 1) {
			return null;
		}
		return files.stream().sorted(Comparator.comparing(File::lastModified).reversed()).collect(Collectors.toList());
	}
}
