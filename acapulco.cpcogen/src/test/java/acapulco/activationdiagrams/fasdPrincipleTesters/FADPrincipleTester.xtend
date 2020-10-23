package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.rulesgeneration.activationdiagrams.AndNode
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.OrNode
import java.util.Set

// TODO: This should really be a hamcrest matcher to make the code even more readable
abstract class FADPrincipleTester {
	/**
	 * Check that the given activation diagram correctly implements this principle for the given feature.
	 */
	def void checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		FeatureModelHelper featureModelHelper)

	static val principleChecks = #[
		new ActMand,
		new ActPar,
		new ActReq,
		new ActGroup,
		new ActXor,
		new ActExc,
		new DeChild,
		new DeXor,
		new DeOr,
		new DeParent,
		new DeReq
	]

	/**
	 * Check all principles and throw a jUnit exception if one doesn't apply
	 */
	static def checkPrinciplesApply(Feature f, Set<ActivationDiagramNode> activationDiagram, FeatureModelHelper featureModelHelper) {
		principleChecks.forEach[checkPrincipleApplies(f, activationDiagram, featureModelHelper)]
	}

	protected def getSiblings(Feature f) {
		f.parentFeature.ownedFeatures.reject[it === f].toSet
	}

	protected def findActivationOf(Set<ActivationDiagramNode> activationDiagram, Feature f) {
		activationDiagram.filter(FeatureDecision).filter[activationOf(f)].head
	}

	protected def activationOf(FeatureDecision fd, Feature feature) {
		(fd.feature === feature) && fd.activate
	}

	protected def findDeactivationOf(Set<ActivationDiagramNode> activationDiagram, Feature f) {
		activationDiagram.filter(FeatureDecision).filter[deactivationOf(f)].head
	}

	protected def deactivationOf(FeatureDecision fd, Feature feature) {
		(fd.feature === feature) && !fd.activate
	}

	protected dispatch def Iterable<OrNode> collectOrNodes(Iterable<ActivationDiagramNode> consequences) {
		consequences.flatMap[collectOrNodes]
	}

	protected dispatch def Iterable<OrNode> collectOrNodes(OrNode or) {
		#{or}
	}

	protected dispatch def Iterable<OrNode> collectOrNodes(AndNode and) {
		and.consequences.collectOrNodes
	}

	protected dispatch def Iterable<OrNode> collectOrNodes(FeatureDecision fd) {
		#{}
	}

	protected dispatch def Iterable<FeatureDecision> collectFeatureDecisions(
		Iterable<ActivationDiagramNode> consequences) {
		consequences.flatMap[collectFeatureDecisions]
	}

	protected dispatch def Iterable<FeatureDecision> collectFeatureDecisions(OrNode or) {
		or.consequences.collectFeatureDecisions
	}

	protected dispatch def Iterable<FeatureDecision> collectFeatureDecisions(AndNode and) {
		and.consequences.collectFeatureDecisions
	}

	protected dispatch def Iterable<FeatureDecision> collectFeatureDecisions(FeatureDecision fd) {
		#{fd}
	}

}
