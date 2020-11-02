package acapulco.rulesgeneration

import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.HashMap
import java.util.HashSet
import java.util.Map
import java.util.Set
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.henshin.model.HenshinFactory
import org.eclipse.emf.henshin.model.ModelElement
import org.eclipse.emf.henshin.model.Rule
import java.util.List
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative

class ActivationDiagToRuleConverter {
	public static val KEY_PRESENCE_CONDITION = "presenceCondition"
	public static val KEY_FEATURE_CONSTRAINT = "featureModel"
	public static val KEY_INJ_MATCHING = "injectiveMatchingPresenceCondition"
	public static val KEY_FEATURES = "features"

	def static Rule convert(FeatureActivationSubDiagram activationDiagram, Map<Feature, EClass> features2Classes) {
		HenshinFactory.eINSTANCE.createRule => [ r |
			// Name
			val rootDec = activationDiagram.rootDecision
			r.name = '''«rootDec.activate?'Act':'De'»_«rootDec.feature.name»'''

			// Nodes
			activationDiagram.presenceConditions.entrySet.forEach [
				val isRoot = rootDec === key
				val type = features2Classes.get(key.feature)
				val activate = key.activate
				r.addPreserveNode(type, activate, value, isRoot)
			]

			// Annotations
			r.addAnnotation(KEY_FEATURE_CONSTRAINT, activationDiagram.computeConstraintExpression)
			r.addAnnotation(KEY_INJ_MATCHING, "false")
			r.addAnnotation(KEY_FEATURES, activationDiagram.vbRuleFeatures.collectFeatures.map[name].join(','))
		]
	}

	private static def computeConstraintExpression(FeatureActivationSubDiagram fasd) {
		(
			fasd.featureModelExpressions + fasd.orImplicationExpressions + fasd.featureExclusionExpressions + fasd.orOverlapExpressions
		).join(' & ')
	}

	private static def orOverlapExpressions(FeatureActivationSubDiagram fasd) {
		#[
			fasd.orOverlaps.entrySet.flatMap[generateOrOverlapExpression(key, value)],
			fasd.generateOrToRootExclusions
		].flatten
	}

	private def static generateOrToRootExclusions(FeatureActivationSubDiagram fasd) {
		// If a given Or-feature is selected that has a direct root-follower, always select that
		fasd.orsToRoot.entrySet.map['''(!«key.name» | «value.name»)''']
	}

	private def static generateOrOverlapExpression(Pair<VBRuleOrFeature, VBRuleOrFeature> orPair,
		List<Pair<VBRuleOrAlternative, VBRuleOrAlternative>> alternatives) {
		alternatives.flatMap[generateOrOverlapExpression(orPair)]
	}

	private static def generateOrOverlapExpression(Pair<VBRuleOrAlternative, VBRuleOrAlternative> alternative,
		Pair<VBRuleOrFeature, VBRuleOrFeature> orPair) {
		#[
			'''(!«alternative.key.name» | !«orPair.value.name» | «alternative.value.name»)''',
			'''(!«alternative.value.name» | !«orPair.key.name» | «alternative.key.name»)'''
		]
	}

	private static def featureExclusionExpressions(FeatureActivationSubDiagram fasd) {
		fasd.featureExclusions.map['''(!«key.name» | !«value.name»)''']
	}

	private static def orImplicationExpressions(FeatureActivationSubDiagram fasd) {
		val separatedOrImplications = fasd.orImplications.entrySet.flatMap[e|e.value.map[it -> e.key]].fold(
			new HashMap<VBRuleOrFeature, Set<VBRuleFeature>>) [ acc, p |
			var set = acc.get(p.key)
			if (set === null) {
				set = new HashSet<VBRuleFeature>
				acc.put(p.key, set)
			}

			set += p.value

			acc
		]

		(
			// Ensure or nodes are activated when they are needed...
			fasd.orImplications.entrySet.reject[value.empty].flatMap[key.impliesAllOf(value)] + // ... and only when they are needed
		separatedOrImplications.entrySet.reject[value.empty].map[key.impliesOneOf(value)]
		)
	}

	private static def featureModelExpressions(FeatureActivationSubDiagram fasd) {
		#{fasd.vbRuleFeatures.name} + // Selecting an or-feature means selecting one of its alternative features
		fasd.vbRuleFeatures.children.flatMap [ feature |
			#{feature.impliesOneOf(feature.children)} + feature.children.eachImplies(feature)

//			val alternatives = '''(«children.map[name].join(' | ')»)'''
//
//			'''(!«name» | «alternatives») & (!«alternatives» | «name»)'''
		] +
		/*
		 * Selecting one alternative feature means none of its sibling features can be selected -- VB-rule or features are actually XOR features.		 *
		 * 
		 * This should ensure minimality of the rule instances we're generating. 
		 */
		fasd.vbRuleFeatures.children.flatMap [ orFeature |
			orFeature.children.flatMap [ alternative |
//				'''(!«alternative.name» | !(«orFeature.children.reject[it === alternative].map[name].join(' | ')»))'''
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

	private static def Iterable<VBRuleFeature> collectFeatures(VBRuleFeature feature) {
		feature.children.flatMap[collectFeatures] + #{feature}
	}

	private static def void addPreserveNode(Rule rule, EClass type, boolean activate, Set<VBRuleFeature> pcComponents,
		boolean isRoot) {
		if (type === null) {
			println("type was null")
		// TODO: Should this throw an exception?
		}

		val lhsNode = HenshinFactory.eINSTANCE.createNode(rule.lhs, type, "")
		val rhsNode = HenshinFactory.eINSTANCE.createNode(rule.rhs, type, "")
		rule.mappings.add(lhsNode, rhsNode)

		val attributeType = type.EAllAttributes.head
		if (isRoot) {
			HenshinFactory.eINSTANCE.createAttribute(lhsNode, attributeType, '''«!activate»''')
		}
		HenshinFactory.eINSTANCE.createAttribute(rhsNode, attributeType, '''«activate»''')

		// PCs that contain 'root' will already be simplified to only contain 'root'
		val pc = pcComponents.map[name].join('|')

		lhsNode.addAnnotation(KEY_PRESENCE_CONDITION, pc)
		rhsNode.addAnnotation(KEY_PRESENCE_CONDITION, pc)
	}

	private static def void addAnnotation(ModelElement elem, String aKey, String aValue) {
		elem.annotations += HenshinFactory.eINSTANCE.createAnnotation => [
			key = aKey
			value = aValue
		]
	}
}
