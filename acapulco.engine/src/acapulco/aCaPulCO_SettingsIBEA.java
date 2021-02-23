package acapulco;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;

import acapulco.algorithm.instrumentation.ToolInstrumenter;
import acapulco.algorithm.termination.StoppingCondition;
import acapulco.engine.HenshinFileReader;
import acapulco.engine.variability.ConfigurationSearchOperator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
import jmetal.operators.selection.BinaryTournament;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;

public class aCaPulCO_SettingsIBEA extends Settings {

	public int populationSize_;
	public int maxEvaluations_;
	public int archiveSize_;

	public double mutationProbability_;
	public double crossoverProbability_;

	public aCaPulCO_SettingsIBEA(Problem p) {
		super(p.getName());

		problem_ = p;
	} // IBEA_Settings

	@SuppressWarnings("unchecked")
	public Algorithm configure(ToolInstrumenter toolInstrumenter, StoppingCondition stoppingCondition,
			Integer stoppingValue, String fm,  int numFeat,
			List<List<Integer>> constr, List<ConfigurationSearchOperator> rules) throws JMException {

		populationSize_ = 100;
		archiveSize_ = 100;

//		mutationProbability_ = 0.001;
//		crossoverProbability_ = 0.05;

		mutationProbability_ = 1.0;
		crossoverProbability_ = 0.05;

		Algorithm algorithm;
		Operator selection;
		Operator crossover;
		Operator mutation;

		HashMap parameters; // Operator parameters

			
		
		if (stoppingCondition == StoppingCondition.TIME) {
			algorithm = new IBEATimeLimited(problem_, stoppingValue, toolInstrumenter);
		} else {
			algorithm = new IBEAEvolutionsLimited(problem_, stoppingValue, toolInstrumenter);
		}

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", populationSize_);
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
		algorithm.setInputParameter("archiveSize", archiveSize_);

		// Mutation and Crossover for Real codification
		parameters = new HashMap<>();
		parameters.put("probability", mutationProbability_);
		List<String> allLines = null;
		try {
			allLines = Files.readAllLines(new File(fm).toPath(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		Map<Integer, String> featureNames = getFeatureNames(allLines);
		Map<Integer, Integer> feature2ActivationRule = getFeature2Operator(allLines, rules, "Act_");
		Map<Integer, Integer> feature2DeactivationRule = getFeature2Operator(allLines, rules, "De_");
		List<Integer> trueOptionalFeatures = new ArrayList<>(aCaPulCO_Problem.featureIndicesAllowedFlip);
				
		aCaPulCO_Mutation mutation_ = new aCaPulCO_Mutation(parameters,  fm, featureNames, numFeat, rules, feature2ActivationRule, feature2DeactivationRule, trueOptionalFeatures, 
				 constr);
		mutation = mutation_;

		parameters = new HashMap<>();
		parameters.put("probability", crossoverProbability_);
		crossover = new aCaPulCO_Crossover(parameters, mutation_);

		/* Selection Operator */
		parameters = new HashMap<>();
		parameters.put("comparator", new FitnessComparator());
		selection = new BinaryTournament(parameters);

		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		return algorithm;
	}

	private Map<Integer, String> getFeatureNames(List<String> allLines) {
		Map<Integer, String> id2Name = new HashMap<>();
		for (String line : allLines) {
			if (line.startsWith("c ")) {
				String[] lineSplit = line.split(" ");
				int number = Integer.parseInt(lineSplit[1]);
				String featureName = lineSplit[2];
				
				id2Name.put(number, featureName);
			}
		}
		return id2Name;
	}

	private Map<Integer, Integer> getFeature2Operator(List<String> allLines, List<ConfigurationSearchOperator> rules, String prefix) {
		Map<String, Integer> nameToId = new HashMap<>();
		for (String line : allLines) {
			if (line.startsWith("c ")) {
				String[] lineSplit = line.split(" ");
				int number = Integer.parseInt(lineSplit[1]);
				String featureName = lineSplit[2];
				
				nameToId.put(featureName, number);
			}
		}
		
		Map<Integer, Integer> result = new HashMap<>();
		for (int i=0; i<rules.size(); i++) {
			String ruleName = rules.get(i).getName();
			if (ruleName.startsWith(prefix)) {
				String name = ruleName.substring(1+ruleName.indexOf('_'));
				if (!nameToId.containsKey(name))
					System.err.println("Unknown feature: "+name);
				else
					result.put(nameToId.get(name), i);
			}
		}
		return result;
	}

	private Map<EClass, Set<EClass>> getAbstract2Concrete(EPackage metamodel) {
		Map<EClass, Set<EClass>> result = new HashMap<EClass, Set<EClass>>();
		for (EClassifier cls_ : metamodel.getEClassifiers()) {
			EClass cls = (EClass) cls_;

			for (EClass superCls : cls.getEAllSuperTypes()) {
				if (superCls.isAbstract()) {
					Set<EClass> set = result.get(superCls);
					if (set == null) {
						set = new HashSet<>();
						result.put(superCls, set);
					}
					set.add(cls);
				}
			}
		}

		return result;
	}


	private Map<EClass, Integer> getClass2Variable(List<String> allLines, EPackage metamodel) {
		Map<EClass, Integer> result = new HashMap<>();
		for (String line : allLines) {
			if (line.startsWith("c ")) {
				String[] lineSplit = line.split(" ");
				int number = Integer.parseInt(lineSplit[1]);
				String featureName = lineSplit[2];
				EClass cls = (EClass) metamodel.getEClassifier(featureName);
				if (cls == null)
					throw new RuntimeException("Feature not found in metamodel: " + featureName);
				result.put(cls, number);
			}
		}

		return result;
	}


	@Override
	public Algorithm configure() throws JMException {
		throw new UnsupportedOperationException();
	}

} // IBEA_Settings
