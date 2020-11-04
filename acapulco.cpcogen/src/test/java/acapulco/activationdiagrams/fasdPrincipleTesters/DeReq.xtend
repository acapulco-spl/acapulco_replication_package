package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue
import static extension acapulco.rulesgeneration.activationdiagrams.FADHelper.*

class DeReq extends FADPrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findDeactivationOf(f)
		val consequences = fd.consequences.collectFeatureDecisions

		assertTrue('''All features requiring «f.name» must be deactivated.''', f.deactivableCTCFeaturesForDeactivateF.
			forall [ requiredFeature |
				consequences.exists[deactivationOf(requiredFeature)]
			])
	}

}
