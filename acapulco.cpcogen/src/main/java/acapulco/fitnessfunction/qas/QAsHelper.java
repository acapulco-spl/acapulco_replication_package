package acapulco.fitnessfunction.qas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QAsHelper {
	private Map<String, FeatureAttributes> attributes; // feature's name -> attributes
	private Map<String, OptimizationType> qas;
	
	public QAsHelper(String qasFilename) throws IOException {
		readQAs(qasFilename);
	}
	
	private void readQAs(String qasFilename) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(qasFilename));
		this.qas = readQAsInfo(lines.get(0), lines.get(1));
		this.attributes = readAttributes(new ArrayList<String>(qas.keySet()), lines.subList(2, lines.size()));
	}

	/**
	 * Read the QAs information from the .csv file (two first lines).
	 */
	private Map<String, OptimizationType> readQAsInfo(String qasNames, String qasType) {
		LinkedHashMap<String, OptimizationType> qas = new LinkedHashMap<String, OptimizationType>();
	
		String names[] = qasNames.split(" ");
		String types[] = qasType.split(" ");
		
		for (int i = 1; i < names.length; i++) {
			OptimizationType ot = types[i].equalsIgnoreCase("min") ? OptimizationType.MINIMIZE : OptimizationType.MAXIMIZE;
			qas.put(names[i], ot);
		}
		return qas;
	}
	
	/**
	 * Read the QAs values from the .csv file
	 */
	private Map<String, FeatureAttributes> readAttributes(List<String> qasNames, List<String> lines) {
		Map<String, FeatureAttributes> attributes = new HashMap<String, FeatureAttributes>();
		
		for (String line : lines) {
			List<String> l = Arrays.asList(line.split(" "));
			
			String featureName = l.get(0).trim();
			if (featureName.contains("(")) {
				featureName = featureName.replaceAll("[()]", "");
			}
			
			l = l.subList(1, l.size()).stream().filter(s -> !s.equals("")).collect(Collectors.toList());
			List<Double> values = l.stream().map(v -> Double.parseDouble(v.trim())).collect(Collectors.toList());
			
			featureName = featureName.toLowerCase();
			FeatureAttributes fa = new FeatureAttributes(featureName);
			for (int i = 0; i < values.size(); i++) {
				fa.addAttribute(qasNames.get(i), values.get(i));		
			}
			attributes.put(featureName, fa);
		}
		return attributes;
	}

	public Map<String, FeatureAttributes> getAttributes() {
		return attributes;
	}

	public Map<String, OptimizationType> getQas() {
		return qas;
	}
	
	public double getQAValue(String featureName, String qaName) {
		return this.attributes.get(featureName.toLowerCase()).getValue(qaName);
	}
}
