package acapulco.rulesgeneration.activationdiagrams

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.model.FeatureModel
import java.util.HashSet
import java.util.Set

import static extension acapulco.rulesgeneration.activationdiagrams.principles.PrinciplesUtil.*

/**
 * Main class for generating rules. This should be reused for all feature decisions for which rules are to be generated within one feature model.
 * 
 * Create a new instance, providing the feature model. Then invoke calculateSubdiagramFor for each feature decision for which to generate a 
 * rule and read off the rule information from the sub diagram produced.
 */
class FeatureActivationDiagram {
	/**
	 * The full diagram.
	 */
	val Set<ActivationDiagramNode> diagram = new HashSet<ActivationDiagramNode>

	/** 
	 * Helper for analysing the feature model
	 */
	val FeatureModelHelper fmHelper

	new(FeatureModel featureModel) {
		this.fmHelper = new FeatureModelHelper(featureModel)
	}

	/**
	 * For testing purposes
	 */
	def getDiagramNodes() {
		diagram.unmodifiableView
	}

	/**
	 * Calculate the subdiagram for the given feature decision.
	 * 
	 * A sub-diagram contains all information needed for generating the VB rule for this feature decision.
	 */
	def FeatureActivationSubDiagram calculateSubdiagramFor(Feature f, boolean activate) {
		val featureDecision = addFeatureDecision(f, activate)

		return new FeatureActivationSubDiagram(featureDecision)
	}

	/**
	 * Add the feature decision and its consequences to the diagram and return the corresponding feature-decision node.
	 * 
	 * Note that the feature decision may already have been added in a previous step (as a consequence of a different decision), 
	 * in which case we simply return that decision node.
	 */
	private def FeatureDecision addFeatureDecision(Feature f, boolean activate) {
		val decisionNode = new FeatureDecision(f, activate)

		addFeatureDecision(decisionNode)
	}

	private def FeatureDecision addFeatureDecision(FeatureDecision decisionNode) {
		if (diagram.contains(decisionNode)) {
			// Return the existing node instead
			return diagram.findFirst[it == decisionNode] as FeatureDecision
		}

		diagram.add(decisionNode)

		val immediateConsequences = decisionNode.applyPrinciples(fmHelper)

		immediateConsequences.forEach[addConsequencesTo(decisionNode)]

		return decisionNode
	}

	/**
	 * Add consequences from one principle application
	 */
	private def addConsequencesTo(Set<Set<FeatureDecision>> consequencesSet, FeatureDecision parent) {
		if (!consequencesSet.empty) {
			if (consequencesSet.size > 1) {
				// Need an Or-node
				val orNode = new OrNode => [
					consequences += consequencesSet.map[toActivationDiagramNode]
				]
				diagram.add(orNode)
				parent.consequences += orNode
			} else {
				// Add directly
				parent.consequences += consequencesSet.head.toActivationDiagramNode
			}
		}
	}

	private def ActivationDiagramNode toActivationDiagramNode(Set<FeatureDecision> decisions) {
		if (decisions.size > 1) {
			val andNode = new AndNode => [
				consequences += decisions.map[addFeatureDecision(it)]
			]
			diagram.add(andNode)
			andNode
		} else {
			addFeatureDecision(decisions.head)
		}
	}
}
