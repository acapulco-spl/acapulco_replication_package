package acapulco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

import acapulco.engine.HenshinBinaryEngine;
import jmetal.core.Solution;
import jmetal.encodings.variable.ArrayInt;
import jmetal.encodings.variable.Binary;
import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;

public class aCaPulCO_Mutation extends Mutation {
	public static boolean DEBUG_MODE = false;
	
	private List<List<Integer>> constraints;

	private Double mutationProbability_ = null;
	private List<Rule> rules;
	private Map<EClass, Integer> class2variable;
	private Map<EClass, Set<EClass>> abstract2concrete;

	private Map<Integer, String> featureNames;
	private Map<Integer, Integer> feature2ActivationRule;
	private Map<Integer, Integer> feature2DeactivationRule;
	private Map<Integer, Integer> activationRule2feature;
	private Map<Integer, Integer> deactivationRule2feature;

	private List<Integer> trueOptionalFeatures;

	public aCaPulCO_Mutation(HashMap<String, Object> parameters, String fm, Map<Integer, String> featureNames, int nFeat, List<Rule> rules,
			Map<Integer, Integer> feature2ActivationRule, Map<Integer,Integer> feature2DeactivationRule, List<Integer> trueOptionalFeatures, Map<EClass, Integer> class2variable, Map<EClass, Set<EClass>> abstract2concrete,
			List<List<Integer>> constraints) {
		super(parameters);
		if (parameters.get("probability") != null) {
			mutationProbability_ = (Double) parameters.get("probability");
		}
		this.rules = rules;
		this.featureNames = featureNames;
		this.class2variable = class2variable;
		this.abstract2concrete = abstract2concrete;
		this.feature2ActivationRule = feature2ActivationRule;
		this.feature2DeactivationRule = feature2DeactivationRule;
		this.trueOptionalFeatures = trueOptionalFeatures;
		this.constraints = constraints;
		
		this.activationRule2feature = new HashMap<>();
		this.deactivationRule2feature = new HashMap<>();
		for (Entry<Integer, Integer> e: feature2ActivationRule.entrySet()) 
			activationRule2feature.put(e.getValue(), e.getKey());
		for (Entry<Integer, Integer> e: feature2DeactivationRule.entrySet()) 
			deactivationRule2feature.put(e.getValue(), e.getKey());

		if (DEBUG_MODE)
			System.out.println("Running in debug mode");
//		for (Entry<EClass, Set<EClass>> e : abstract2concrete.entrySet()) {
//			String str = e.getKey().getName()+" -> ";
//			for (EClass en : e.getValue()) {
//				str+= en.getName() + " ";
//			}
//			System.out.println(str);
//		}

	}

	public void doMutation(double probability, Solution solution) throws JMException {
			int featureIndex =trueOptionalFeatures.get((int) (Math.random() * trueOptionalFeatures.size()));
			boolean active = ((Binary)solution.getDecisionVariables()[0]).bits_.get(featureIndex );
			int ruleIndex = active ? feature2DeactivationRule.get(featureIndex+1) : 
				feature2ActivationRule.get(featureIndex+1);
			
			applyCpcoRuleToSolution(solution, ruleIndex);
	} 

	void applyCpcoRuleToSolution(Solution solution, int ruleIndex) {
		Rule rule = rules.get(ruleIndex);
//		Rule rule = null;
//		for (Rule r : rules) {
//			if (r.getName().equals("Act_UNICODE_BIDI_SUPPORT"))
//				rule = r;
//		}
//		System.out.println(rule);

		boolean activationRule = activationRule2feature.keySet().contains(ruleIndex);
		Integer feature = activationRule ? activationRule2feature.get(ruleIndex) : deactivationRule2feature.get(ruleIndex);
		boolean featureActive = ((Binary)solution.getDecisionVariables()[0]).bits_.get(feature-1);
		if (featureActive && activationRule) 
			rule = rules.get(feature2DeactivationRule.get(feature));
		else if (!featureActive && !activationRule)
			rule = rules.get(feature2ActivationRule.get(feature));
		
		try {
			rule = HenshinBinaryEngine.removeVariability(rule);
		} catch (Exception e) {
			return;
		}
		Set<EClass> usedFeatures = new HashSet<>();
		LinkedHashMap<Integer, Boolean> change = new LinkedHashMap<>();

		applyCpcoRuleToSolutionRecursively(solution, rule, usedFeatures, change);

		for (Entry<Integer, Boolean> entry : change.entrySet()) {
			Integer variable = entry.getKey();
			boolean newValue = entry.getValue();

			((Binary) solution.getDecisionVariables()[0]).bits_.set(variable - 1, newValue);
		}
		
		ArrayInt applied = (ArrayInt) solution.getDecisionVariables()[1];
		int[] appliedNew = new int[applied.array_.length + 1];
		System.arraycopy(applied.array_, 0, appliedNew, 0, applied.array_.length);
		appliedNew[appliedNew.length - 1] = ruleIndex;
		applied.array_ = appliedNew;
		
		ArrayInt firm = (ArrayInt) solution.getDecisionVariables()[2];
		int[] firmNew = new int[firm.array_.length + change.keySet().size()];
		System.arraycopy(firm.array_, 0, firmNew, 0, firm.array_.length);
		int offset= firm.array_.length;
		for (Integer key : change.keySet()) {
			firmNew[offset] = key;
			offset++;
		}
		firm.array_ = firmNew;
		
		if (DEBUG_MODE) {
			checkViolatedConstraints(((Binary) solution.getDecisionVariables()[0]), ruleIndex);
		}
	}

	private void applyCpcoRuleToSolutionRecursively(Solution solution, Rule rule, Set<EClass> usedFeatures,
			Map<Integer, Boolean> change) {
		for (Node node : rule.getRhs().getNodes()) {
			if (rule.isMultiRule() && rule.getMultiMappings().getOrigin(node) != null)
				continue;

			Integer var = null;
			Boolean newValue = null;
			if (!node.getAttributes().isEmpty()) {
				newValue = Boolean.parseBoolean(node.getAttributes().get(0).getValue());
			}
			if (!node.getType().isAbstract()) {
				var = class2variable.get(node.getType());
				usedFeatures.add(node.getType());
				if (var != null & newValue != null) {
					change.put(var, newValue);
				}
			} else {
				List<EClass> options = new ArrayList<>(abstract2concrete.get(node.getType()));
				options.removeAll(usedFeatures);
				if (options.isEmpty())
					return;
				
				if (!rule.isMultiRule()) {
					EClass feature = options.get((int) (Math.random() * options.size()));
					usedFeatures.add(feature);
					var = class2variable.get(feature);
					if (var != null & newValue != null) {
						change.put(var, newValue);
					}
				} else {
					for (EClass o : options) {
						usedFeatures.add(o);
						var = class2variable.get(o);
						if (var != null & newValue != null) {
							change.put(var, newValue);
						}

					}
				}
			}

		}
		for (Rule multiRule : rule.getMultiRules()) {
			applyCpcoRuleToSolutionRecursively(solution, multiRule, usedFeatures, change);
		}
	}

	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;
		doMutation(mutationProbability_, solution);
		return solution;
	} 
	
	public boolean checkViolatedConstraints(Binary b, Integer lastRuleApplied) {
		for (List<Integer> constraint : constraints) {
			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b.getIth(abs - 1) == sign) {
					sat = true;
					break;
				}
			}
			if (!sat) {
				String errorMessage = "Rule application lead to constraint violation.\n";
				errorMessage += "Rule: " +rules.get(lastRuleApplied).getName() +"\n";
				errorMessage += "Constraint: ";
				
				for (Iterator<Integer> it = constraint.iterator();it.hasNext(); ) {
					Integer cur = it.next();
					if (cur<0) {
						errorMessage+="!";
					}
					errorMessage+= featureNames.get(Math.abs(cur));
					if (it.hasNext())
						errorMessage +=" or ";
				}
				errorMessage += " (" + constraint +")";
				System.err.println(errorMessage);
				return false;
//				throw new RuntimeException(errorMessage);
			}
		}
		return true;
	}

} 