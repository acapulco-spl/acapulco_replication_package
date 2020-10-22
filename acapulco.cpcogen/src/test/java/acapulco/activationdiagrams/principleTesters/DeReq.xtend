package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class DeReq extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findDeactivationOf(f)
		val consequences = fd.consequences.collectFeatureDecisions

		// TODO: There is a bug here: if I change the below to f.activableCTCFeaturesForActivateF, the test breaks, but the two should logically be the same
		assertTrue('''All required features of «f.name» must be deactivated.''', f.deactivableCTCFeaturesForDeactivateF.
			forall [ requiredFeature |
				consequences.exists[deactivationOf(requiredFeature)]
			])
	}

}
