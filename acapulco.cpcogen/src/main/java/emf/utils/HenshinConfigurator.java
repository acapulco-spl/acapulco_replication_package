package emf.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.henshin.model.Rule;

import acapulco.engine.variability.SatSolver;
import acapulco.engine.variability.XorEncoderUtil;


public class HenshinConfigurator {
	public static Rule removeVariability(Rule rule)  {
		// Assumption: If rules have annotations, they are VB,
		// and the first annotation is the feature constraint.
		if (rule.getAnnotations().isEmpty() || rule.getAnnotations().get(0).getValue().isBlank()) {
			return rule;
		}

		String featureConstraint = XorEncoderUtil.encodeXor(rule.getAnnotations().get(0).getValue());
		String featuresAsString = rule.getAnnotations().get(2).getValue().replace(" ", "");
		List<String> features = new ArrayList<String>(Arrays.asList(featuresAsString.split(",")));

		List<List<String>> solutions = SatSolver.getAllSolutions(featureConstraint);
		int index = (int) (Math.random() * solutions.size());
		if (solutions.size() == 0) {
			System.err.println(rule.getName());
			throw new RuntimeException("Could not satisfy FM: "+featureConstraint);
		}
		List<String> solution = solutions.get(index);
		
		Map<String, Boolean> config = new HashMap<>();
		for (String f : features) {
			config.put(f, solution.contains(f));
		}
		return RuleProvider.provideRule(rule,config);
	}

}
