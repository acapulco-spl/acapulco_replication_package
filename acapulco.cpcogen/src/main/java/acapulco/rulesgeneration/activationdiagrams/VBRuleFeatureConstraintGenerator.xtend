package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.Map
import java.util.Set

/**
 * Helper class to make the constraint generation reusable.
 */
abstract class VBRuleFeatureConstraintGenerator {
	static def String computeConstraintExpression(FeatureActivationSubDiagram fasd) {
		(
			fasd.featureModelExpressions + 
			fasd.orImplicationExpressions + 
			fasd.featureExclusionExpressions + 
			fasd.orFixingExpressions +
			fasd.orCycleBreakers
		).join(' & ')
	}

	private static def orFixingExpressions(FeatureActivationSubDiagram fasd) {
		fasd.orFixings.entrySet.flatMap[orFixingExpressionsFor(key, value)]
	}

	private static def orFixingExpressionsFor(VBRuleOrFeature orFeature, Map<VBRuleFeature, Set<VBRuleOrAlternative>> fixings) {
		fixings.entrySet.map[#[orFeature, key].allJointlyImply(value.head)]
	}

	private static def orCycleBreakers(FeatureActivationSubDiagram fasd) {
		val orImplGraph = new OrImplicationGraph(fasd)
		
		orImplGraph.cycleEntries.entrySet.map[key.impliesOneOf(value)]
	}

	private static def featureExclusionExpressions(FeatureActivationSubDiagram fasd) {
		fasd.featureExclusions.map['''(!«key.name» | !«value.name»)''']
	}

	private static def orImplicationExpressions(FeatureActivationSubDiagram fasd) {
//		val separatedOrImplications = fasd.orImplications.entrySet.flatMap[e|e.value.map[it -> e.key]].fold(
//			new HashMap<VBRuleOrFeature, Set<VBRuleFeature>>) [ acc, p |
//			var set = acc.get(p.key)
//			if (set === null) {
//				set = new HashSet<VBRuleFeature>
//				acc.put(p.key, set)
//			}
//
//			set += p.value
//
//			acc
//		]
		val separatedOrImplications = fasd.orImplications.entrySet.flatMap[e|e.value.map[it -> e.key]].groupBy[key].mapValues[map[value].toSet]
		

		(
			/*
			 * Ensure or nodes are activated when they are needed...
			 */
			fasd.orImplications.entrySet.reject[value.empty].flatMap[key.impliesAllOf(value)] +
			/*
			 * ... and only when they are needed
			 */
			separatedOrImplications.entrySet.reject[value.empty].map[key.impliesOneOf(value)]
		)
	}

	private static def featureModelExpressions(FeatureActivationSubDiagram fasd) {
		#{fasd.vbRuleFeatures.name} + 
		/*
		 *  Selecting an or-feature means selecting one of its alternative features
		 */
		fasd.vbRuleFeatures.children.flatMap [ feature |
			#{feature.impliesOneOf(feature.children)} + feature.children.eachImplies(feature)
		] +
		/*
		 * Selecting one alternative feature means none of its sibling features can be selected -- VB-rule or features are actually XOR features.		 *
		 * 
		 * This should ensure minimality of the rule instances we're generating. 
		 */
		fasd.vbRuleFeatures.children.flatMap [ orFeature |
			orFeature.children.flatMap [ alternative |
				alternative.impliesNoneOf(orFeature.children.reject[it === alternative])
			]
		]
	}

	private static def impliesAllOf(VBRuleFeature antecedent, Iterable<? extends VBRuleFeature> consequent) {
		// '''(!«antecedent.name» | («consequent.map[name].join(' & ')»))''', but turned into a CNF formula
		consequent.map['''(!«antecedent.name» | «name»)''']
	}

	private static def impliesNoneOf(VBRuleFeature antecedent, Iterable<? extends VBRuleFeature> consequent) {
		// '''(!«antecedent.name» | !(«consequent.map[name].join(' | ')»))''', but turned into a CNF formula
		consequent.map['''(!«antecedent.name» | !«name»)''']
	}

	private static def String impliesOneOf(VBRuleFeature antecedent,
		Iterable<? extends VBRuleFeature> consequent) '''(!«antecedent.name» | («consequent.map[name].join(' | ')»))'''

	private static def eachImplies(Iterable<? extends VBRuleFeature> antecedent, VBRuleFeature consequent) {
		antecedent.map['''(!«name» | «consequent.name»)''']
	}

	private static def allJointlyImply(Iterable<? extends VBRuleFeature> antecedents, VBRuleFeature consequent) '''
		(«antecedents.map['''!«name»'''].join(' | ')» | «consequent.name»)
	'''
}
