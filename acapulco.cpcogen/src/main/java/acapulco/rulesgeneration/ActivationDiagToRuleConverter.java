package acapulco.rulesgeneration;

import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.henshin.model.Annotation;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.ModelElement;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.impl.HenshinFactoryImpl;
import org.eclipse.xtext.xbase.lib.Pair;

import acapulco.model.Feature;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision;
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature;
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature;

public class ActivationDiagToRuleConverter {
	public final static String KEY_PRESENCE_CONDITION = "presenceCondition";

	public final static String KEY_FEATURE_CONSTRAINT = "featureModel";
	public final static String KEY_INJ_MATCHING = "injectiveMatchingPresenceCondition";
	public final static String KEY_FEATURES = "features";

	public static Rule convert(FeatureActivationSubDiagram activationDiagram, Map<Feature, EClass> features2Classes) {
		Rule rule = HenshinFactory.eINSTANCE.createRule();

		// Name
		FeatureDecision rootDec = activationDiagram.getRootDecision();
		String ruleName = (rootDec.isActivate() ? "Act" : "De") + "_" + rootDec.getFeature().getName();
		rule.setName(ruleName);

		// Nodes
		for (Entry<FeatureDecision, Set<VBRuleFeature>> entry : activationDiagram.getPresenceConditions().entrySet()) {
			boolean isRoot = rootDec == entry.getKey();
			EClass type = features2Classes.get(entry.getKey().getFeature());
			boolean activate = entry.getKey().isActivate();

			addPreserveNode(rule, type, activate, entry.getValue(), isRoot);
		}

		// Constraint
		String constraint = "";
		
		for (Entry<VBRuleFeature, Set<VBRuleOrFeature>> orImpl : activationDiagram.getOrImplications().entrySet()) {
			constraint += "(!" + orImpl.getKey().getName() + " | ";
			constraint += prettyPrint(orImpl.getValue(), " | ");
			constraint += ") & ";
		}
		
		for (Pair<VBRuleFeature, VBRuleFeature> exclPair : activationDiagram.getFeatureExclusions()) {
				constraint += "(!" + exclPair.getKey().getName() + " | !" + exclPair.getValue().getName() + ") & ";
		}
		
		if (constraint.endsWith(" & ")) {
			constraint = constraint.substring(0,constraint.length()-3);
		}
		
		Set<VBRuleFeature> features = new HashSet<>();
		addFeaturesRecursive(features, activationDiagram.getVbRuleFeatures());

		addAnnotation(rule, KEY_FEATURE_CONSTRAINT, constraint);
		addAnnotation(rule, KEY_INJ_MATCHING, false + "");
		addAnnotation(rule, KEY_FEATURES, prettyPrint(features, ","));

		return rule;
	}

	private static void addFeaturesRecursive(Set<VBRuleFeature> features, VBRuleFeature feature) {
		features.add(feature);
		for (VBRuleFeature f : feature.getChildren())
			addFeaturesRecursive(features, f);
	}

	private static String prettyPrint(Set<? extends VBRuleFeature> value, String delim) {
		StringBuilder res = new StringBuilder();
		Iterator<? extends VBRuleFeature> it = value.iterator();
		while (it.hasNext()) {
			res.append(it.next().getName());
			if (it.hasNext())
				res.append(delim);
		}
		return res.toString();
	}

	private static void addPreserveNode(Rule rule, EClass type, boolean activate, Set<VBRuleFeature> pcComponents,
			boolean isRoot) {
		
		if (type == null)
			System.out.println("type was null");
		
		Node lhsNode = HenshinFactory.eINSTANCE.createNode(rule.getLhs(), type, "");
		Node rhsNode = HenshinFactory.eINSTANCE.createNode(rule.getRhs(), type, "");
		rule.getMappings().add(lhsNode, rhsNode);
		EAttribute attributeType = type.getEAllAttributes().get(0);
		if (isRoot)
			HenshinFactory.eINSTANCE.createAttribute(lhsNode, attributeType, "" + !activate);
		HenshinFactory.eINSTANCE.createAttribute(rhsNode, attributeType, "" + activate);

		
		VBRuleFeature singleFeature = null; 
		if (pcComponents.size() == 1) {
			singleFeature = pcComponents.iterator().next();
		} else {
			for (VBRuleFeature f : pcComponents) {
				if (f.getName().equals("root")) {
					singleFeature = f;
					break;
				}
			}
		}
		
		String pc = null;
		if (singleFeature != null)  {
			pc = singleFeature.getName();
		} else {
			pc = prettyPrint(pcComponents, " | ");
		}
			
		
		addAnnotation(lhsNode, KEY_PRESENCE_CONDITION, pc);
		addAnnotation(rhsNode, KEY_PRESENCE_CONDITION, pc);

	}

	private static void addAnnotation(ModelElement elem, String key, String value) {
		Annotation ann = HenshinFactory.eINSTANCE.createAnnotation();
		ann.setKey(key);
		ann.setValue(value);
		elem.getAnnotations().add(ann);
	}

}
