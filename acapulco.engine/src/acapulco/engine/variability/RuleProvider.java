package acapulco.engine.variability;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.henshin.model.Rule;

public class RuleProvider {
	static Map<Rule,Map<String, ConfigurationSearchOperator>> ruleCache = new HashMap<>();
	
	public static ConfigurationSearchOperator provideRule(Rule rule, Map<String,Boolean> config) {
		Map<String, ConfigurationSearchOperator> map = ruleCache.get(rule);
		if (map == null) {
			map = new HashMap<String, ConfigurationSearchOperator>();
			ruleCache.put(rule, map);
		}
		
		StringBuilder configCodeSb = new StringBuilder();
		config.entrySet().forEach(e -> configCodeSb.append(e.getValue() ? 1 : 0));
		String configCode = configCodeSb.toString();
		
		ConfigurationSearchOperator result = map.get(configCode);
		if (result == null) {
			result = new RuleBuilder(rule, config).buildRule();
			map.put(configCode, result);
		}
		
		return result;
	}
}
