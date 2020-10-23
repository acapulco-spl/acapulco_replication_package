package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class ActReq extends FADPrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findActivationOf(f)

		val consequences = fd.consequences.collectFeatureDecisions

		assertTrue('''All features required by «f.name» must be activated.''', f.activableCTCFeaturesForActivateF.forall [ rf |
			consequences.exists[activationOf(rf)]
		])
	}
}
