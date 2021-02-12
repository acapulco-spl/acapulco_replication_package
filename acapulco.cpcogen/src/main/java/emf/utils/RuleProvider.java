package emf.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.henshin.model.Rule;

public class RuleProvider {
	static Map<Rule,Map<String, Rule>> ruleCache = new HashMap<>();
	
	public static Rule provideRule(Rule rule, Map<String,Boolean> config) {
		Map<String, Rule> map = ruleCache.get(rule);
		if (map == null) {
			map = new HashMap<String, Rule>();
			ruleCache.put(rule, map);
		}
		
		StringBuilder configCodeSb = new StringBuilder();
		config.entrySet().forEach(e -> configCodeSb.append(e.getValue() ? 1 : 0));
		String configCode = configCodeSb.toString();
		
		Rule result = map.get(configCode);
		if (result == null) {
			result = new RuleBuilder(rule, config).buildRule();
			map.put(configCode, result);
		}
		
		return result;
	}
}
