package acapulco.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.henshin.model.Rule;

import acapulco.engine.variability.ConfigurationSearchOperator;
import acapulco.engine.variability.RuleBuilder;
import acapulco.engine.variability.RuleProvider;
import acapulco.engine.variability.SatSolver;
import acapulco.engine.variability.XorEncoderUtil;


public class HenshinConfigurator {
	// This cache assumes that we only use one SAT solution.
	public static Map<Rule,ConfigurationSearchOperator> cache = new HashMap<>();
	
	public static ConfigurationSearchOperator removeVariability(Rule rule) throws Exception {
		if (cache.containsKey(rule))
			return cache.get(rule);
		
		// Assumption: If rules have annotations, they are VB,
		// and the first annotation is the feature constraint.
		if (rule.getAnnotations().isEmpty() || rule.getAnnotations().get(0).getValue().isBlank()) {
			ConfigurationSearchOperator result = new RuleBuilder(rule, Collections.emptyMap()).buildRule(false);
			cache.put(rule, result);
			return result;
		}

		String featureConstraint = rule.getAnnotations().get(0).getValue();
		String featuresAsString = rule.getAnnotations().get(2).getValue().replace(" ", "");
		List<String> features = new ArrayList<String>(Arrays.asList(featuresAsString.split(",")));

		List<List<String>> solutions = SatSolver.getAllSolutions(featureConstraint);
//		System.out.println(solutions.size());
		int index = (int) (Math.random() * solutions.size());
		if (solutions.size() == 0) {
			System.err.println(rule.getName());
			throw new Exception("Could not satisfy FM: "+featureConstraint);
		}
		List<String> solution = solutions.get(index);
		
		Map<String, Boolean> config = new HashMap<>();
		for (String f : features) {
			config.put(f, solution.contains(f));
		}
		ConfigurationSearchOperator result = RuleProvider.provideRule(rule,config);
		cache.put(rule, result);
		return result;
	}

}
