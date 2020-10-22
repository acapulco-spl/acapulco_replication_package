package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class ActReq extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findActivationOf(f)

		val consequences = fd.consequences.collectFeatureDecisions

		assertTrue('''All features required by «f.name» must be activated.''', f.activableCTCFeaturesForActivateF.forall [ rf |
			consequences.exists[activationOf(rf)]
		])
	}
}
