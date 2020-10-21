package acapulco.featuremodel.extendedfm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import acapulco.featuremodel.FeatureModelHelper;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.model.MDEOptimiser4EFMFactory;
import acapulco.model.OptimizationType;
import acapulco.model.QualityAttribute;
import acapulco.model.QualityAttributeAnnotation;
import acapulco.model.QualityAttributesModel;

public class AttributedFMGenerator {
	private FeatureModel fm;
	private List<FeatureAttributes> attributes;
	private Map<String, OptimizationType> qas;
	private FeatureModelHelper fmHelper;
	
	/**
	 * 
	 * @param fm				Feature model.
	 * @param qasFilename		.csv file with the QAs' values for each feature.
	 * @throws IOException 
	 */
	public AttributedFMGenerator(FeatureModel fm, String qasFilename) throws IOException {
		this.fmHelper = new FeatureModelHelper(fm);
		readQAs(qasFilename);
		this.fm = generateAttributedFeatureModel(fm);
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
	private List<FeatureAttributes> readAttributes(List<String> qasNames, List<String> lines) {
		ArrayList<FeatureAttributes> attributes = new ArrayList<FeatureAttributes>();
		
		for (String line : lines) {
			List<String> l = Arrays.asList(line.split(" "));
			
			String featureName = l.get(0).trim();
			if (featureName.contains("(")) {
				featureName = featureName.replaceAll("[()]", "");
			}
			
			l = l.subList(1, l.size()).stream().filter(s -> !s.equals("")).collect(Collectors.toList());
			List<Double> values = l.stream().map(v -> Double.parseDouble(v.trim())).collect(Collectors.toList());
			
			FeatureAttributes fa = new FeatureAttributes(featureName);
			for (int i = 0; i < values.size(); i++) {
				fa.addAttribute(qasNames.get(i), values.get(i));		
			}
			attributes.add(fa);
		}
		return attributes;
	}
	
	/**
	 * Complete the feature model with the information about the QAs.
	 * 
	 * @param fm	Feature model.
	 * @return		Extended feature model.
	 */
	private FeatureModel generateAttributedFeatureModel(FeatureModel fm) {
		MDEOptimiser4EFMFactory factory = MDEOptimiser4EFMFactory.eINSTANCE;
		
		// Create the QAs model
		QualityAttributesModel qaModel = factory.createQualityAttributesModel();
		fm.setOwnedQualityAttributeModel(qaModel);
		
		// Create the QAs' information (name and optimization type)
		for (String name : this.qas.keySet()) {
			QualityAttribute qa = factory.createQualityAttribute();
			qa.setName(name);
			qa.setOptimizationType(qas.get(name));
			
			qaModel.getOwnedQualityAttributes().add(qa);
			
			// Assign the QAs' values to each feature
			for (FeatureAttributes a : this.attributes) {
				double value = a.getValue(name);
				
				QualityAttributeAnnotation annot = factory.createQualityAttributeAnnotation();
				annot.setQualityAttribute(qa);
				annot.setValue(value);
				//System.out.println("FN: " + a.getFeatureName());
				Feature f = fmHelper.getFeatureByName(a.getFeatureName());
				System.out.println("Feature: " + a.getFeatureName() + ", QA: " + name + ", value: " + value);
				annot.getFeatures().add(f);
				
				qaModel.getOwnedQualityAttributeAnnotations().add(annot);
			}
			
		}
		return fm;
	}
	
	public FeatureModel getExtendedFeatureModel() {
		return this.fm;
	}

}
