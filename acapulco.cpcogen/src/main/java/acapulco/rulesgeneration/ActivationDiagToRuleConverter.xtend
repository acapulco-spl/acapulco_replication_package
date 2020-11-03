package acapulco.rulesgeneration

import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import java.util.Map
import java.util.Set
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.henshin.model.HenshinFactory
import org.eclipse.emf.henshin.model.ModelElement
import org.eclipse.emf.henshin.model.Rule

import static extension acapulco.rulesgeneration.activationdiagrams.VBRuleFeatureConstraintGenerator.computeConstraintExpression

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
			r.addAnnotation(KEY_FEATURES, activationDiagram.collectAllFeatures.map[name].join(','))
		]
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
