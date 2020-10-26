package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set
import static org.junit.Assert.assertTrue

abstract class FeatureDecisionSetPrincipleTester {

	abstract def void checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions, FeatureModelHelper featureModelHelper)
	
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
	
	def static checkPrinciplesApply(Set<Pair<Feature, Boolean>> featureDecisions, FeatureModelHelper featureModelHelper) {
		featureDecisions.forEach[fd | principleChecks.forEach[checkPrincipleApplies(fd, featureDecisions, featureModelHelper)]]
	}
	
	// TODO: Just copied this from the other tester, an indication that it should really be moved somewhere else, perhaps the feature model helper?
	protected def getSiblings(Feature f) {
		f.parentFeature.ownedFeatures.reject[it === f].toSet
	}
	
	protected def hasActivationFor(Set<Pair<Feature, Boolean>> featureDecisions, Feature f) {
		featureDecisions.exists[(key === f) && value]
	}

	protected def hasDeactivationFor(Set<Pair<Feature, Boolean>> featureDecisions, Feature f) {
		featureDecisions.exists[(key === f) && !value]
	}
	
	protected def assertFDCheck(String message, Set<Pair<Feature, Boolean>> featureDecisions, boolean testResult) {
		assertTrue(message + featureDecisions.format, testResult)
	}
	
	private def format(Set<Pair<Feature, Boolean>> featureDecisions) ''' (Rule instance: «featureDecisions.map['''«key.name»«value?'+':'-'»'''].join(',')»)'''
}