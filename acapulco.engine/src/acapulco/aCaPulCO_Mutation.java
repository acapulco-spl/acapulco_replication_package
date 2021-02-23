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
import org.eclipse.emf.henshin.model.Rule;

import acapulco.engine.variability.ConfigurationSearchOperator;
import jmetal.core.Solution;
import jmetal.encodings.variable.ArrayInt;
import jmetal.encodings.variable.Binary;
import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;

public class aCaPulCO_Mutation extends Mutation {
	public static boolean DEBUG_MODE = false;

	private List<List<Integer>> constraints;

	private Double mutationProbability_ = null;
	private List<ConfigurationSearchOperator> operators;

	private Map<Integer, String> featureNames;
	private Map<Integer, Integer> feature2ActivationRule;
	private Map<Integer, Integer> feature2DeactivationRule;
	private Map<Integer, Integer> activationRule2feature;
	private Map<Integer, Integer> deactivationRule2feature;

	private List<Integer> trueOptionalFeatures;

	public aCaPulCO_Mutation(HashMap<String, Object> parameters, String fm, Map<Integer, String> featureNames,
			int nFeat, List<ConfigurationSearchOperator> operators, Map<Integer, Integer> feature2ActivationRule,
			Map<Integer, Integer> feature2DeactivationRule, List<Integer> trueOptionalFeatures,
			List<List<Integer>> constraints) {
		super(parameters);
		if (parameters.get("probability") != null) {
			mutationProbability_ = (Double) parameters.get("probability");
		}
		this.operators = operators;
		this.featureNames = featureNames;
		this.feature2ActivationRule = feature2ActivationRule;
		this.feature2DeactivationRule = feature2DeactivationRule;
		this.trueOptionalFeatures = new ArrayList<>(feature2DeactivationRule.keySet());
		this.constraints = constraints;

		this.activationRule2feature = new HashMap<>();
		this.deactivationRule2feature = new HashMap<>();
		for (Entry<Integer, Integer> e : feature2ActivationRule.entrySet())
			activationRule2feature.put(e.getValue(), e.getKey());
		for (Entry<Integer, Integer> e : feature2DeactivationRule.entrySet())
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
		int featureIndex = trueOptionalFeatures.get((int) (Math.random() * trueOptionalFeatures.size()));
		boolean active = ((Binary) solution.getDecisionVariables()[0]).bits_.get(featureIndex);
		Integer ruleIndexInteger = active ? feature2DeactivationRule.get(featureIndex)
				: feature2ActivationRule.get(featureIndex);

		int ruleIndex = (int) ruleIndexInteger;

		applyCpcoRuleToSolution(solution, ruleIndex);
	}

	void applyCpcoRuleToSolution(Solution solution, int ruleIndex) {
		ConfigurationSearchOperator operator = operators.get(ruleIndex);

		boolean activationRule = activationRule2feature.keySet().contains(ruleIndex);
		Integer feature = activationRule ? activationRule2feature.get(ruleIndex)
				: deactivationRule2feature.get(ruleIndex);
		boolean featureActive = ((Binary) solution.getDecisionVariables()[0]).bits_.get(feature - 1);
		if (featureActive && activationRule)
			operator = operators.get(feature2DeactivationRule.get(feature));
		else if (!featureActive && !activationRule)
			operator = operators.get(feature2ActivationRule.get(feature));

		try {

			Set<EClass> usedFeatures = new HashSet<>();
			LinkedHashMap<Integer, Boolean> change = new LinkedHashMap<>();

//			System.out.println("Solution was: " + solution);

			applyCpcoRuleToSolutionRecursively(solution, operator, usedFeatures, change);

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
			int offset = firm.array_.length;
			for (Integer key : change.keySet()) {
				firmNew[offset] = key;
				offset++;
			}
			firm.array_ = firmNew;

			if (DEBUG_MODE) {
				checkViolatedConstraints(((Binary) solution.getDecisionVariables()[0]), ruleIndex);
			}

//			System.out.println("Solution now is: " + solution);
//			System.out.println();
		} catch (Exception e) {
			return;
		}
	}

	private void applyCpcoRuleToSolutionRecursively(Solution solution, ConfigurationSearchOperator cso,
			Set<EClass> usedFeatures, Map<Integer, Boolean> change) {
		// TODO: Not currently supporting what we used to do with multi-operators --
		// these would need an extension to ConfigurationSearchOperator
		// TODO: Not updating used features as this was only needed for multi-operators.
		for (Integer feature : cso.getFeatures()) {
			boolean isActivate = cso.isActivated(feature);
			// Don't need to do the below, because the rule will have been selected to
			// ensure it can be applied for the current configuration
//			boolean needChecking = cso.isRoot(feature);

			change.put(feature, isActivate);
		}

//		for (Node node : rule.getRhs().getNodes()) {
//			if (rule.isMultiRule() && rule.getMultiMappings().getOrigin(node) != null)
//				continue;
//
//			Integer var = null;
//			Boolean newValue = null;
//			if (!node.getAttributes().isEmpty()) {
//				newValue = Boolean.parseBoolean(node.getAttributes().get(0).getValue());
//			}
//			if (!node.getType().isAbstract()) {
//				var = class2variable.get(node.getType());
//				usedFeatures.add(node.getType());
//				if (var != null & newValue != null) {
//					change.put(var, newValue);
//				}
//			} else {
//				List<EClass> options = new ArrayList<>(abstract2concrete.get(node.getType()));
//				options.removeAll(usedFeatures);
//				if (options.isEmpty())
//					return;
//
//				if (!rule.isMultiRule()) {
//					EClass feature = options.get((int) (Math.random() * options.size()));
//					usedFeatures.add(feature);
//					var = class2variable.get(feature);
//					if (var != null & newValue != null) {
//						change.put(var, newValue);
//					}
//				} else {
//					for (EClass o : options) {
//						usedFeatures.add(o);
//						var = class2variable.get(o);
//						if (var != null & newValue != null) {
//							change.put(var, newValue);
//						}
//
//					}
//				}
//			}
//
//		}
//		for (Rule multiRule : rule.getMultiRules()) {
//			applyCpcoRuleToSolutionRecursively(solution, multiRule, usedFeatures, change);
//		}
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
				errorMessage += "Rule: " + operators.get(lastRuleApplied).getName() + "\n";
				errorMessage += "Constraint: ";

				for (Iterator<Integer> it = constraint.iterator(); it.hasNext();) {
					Integer cur = it.next();
					if (cur < 0) {
						errorMessage += "!";
					}
					errorMessage += featureNames.get(Math.abs(cur));
					if (it.hasNext())
						errorMessage += " or ";
				}
				errorMessage += " (" + constraint + ")";
				System.err.println(errorMessage);
				return false;
//				throw new RuntimeException(errorMessage);
			}
		}
		return true;
	}

}