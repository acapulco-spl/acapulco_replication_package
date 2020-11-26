package acapulco.engine.variability;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

public class RuleProvider {	
	static Map<Rule, Map<BitSet, ConfigurationSearchOperator>> ruleCache = new HashMap<>();
	// Assumption: rules will only be resolved by the SAT Solver once, using the same variable mapping for all instances
	static Map<Rule, Map<Node, BitSet>> compiledPCsPerRule = new HashMap<>();
	
	public static ConfigurationSearchOperator provideRule(Rule rule, SatSolver.SatSolution configuration) {
		Map<BitSet, ConfigurationSearchOperator> map = ruleCache.get(rule);
		if (map == null) {
			map = new HashMap<BitSet, ConfigurationSearchOperator>();
			ruleCache.put(rule, map);
		}
		
		ConfigurationSearchOperator result = map.get(configuration.solution);
		if (result == null) {
			Map<Node, BitSet> compiledPCs = compiledPCsPerRule.get(rule);
			if (compiledPCs == null) {
				compiledPCs = compilePCs(rule, configuration.featureNameIndices);
				compiledPCsPerRule.put(rule, compiledPCs);
			}
			
			result = new RuleBuilder(rule, compiledPCs, configuration.solution).buildRule();
			map.put(configuration.solution, result);
		}
		
		return result;
	}
	
	private static Map<Node, BitSet> compilePCs(Rule rule, Map<String, Integer> featureNameIndices) {
		final Map<Node, BitSet> result = new HashMap<>();
		
		rule.getRhs().getNodes().forEach(n -> {
			final BitSet compiledPC = new BitSet(featureNameIndices.size());
			
			String pcAnnotation = n.getAnnotations().get(0).getValue();
			
			if (pcAnnotation == null || pcAnnotation.isBlank()) {
				compiledPC.set(0, featureNameIndices.size() - 1, true);
			}
			
			for (String featureName : pcAnnotation.split("\\|")) {
				compiledPC.set(featureNameIndices.get(featureName));
			}
			
			result.put(n, compiledPC);
		});
		
		return result;
	}
}
