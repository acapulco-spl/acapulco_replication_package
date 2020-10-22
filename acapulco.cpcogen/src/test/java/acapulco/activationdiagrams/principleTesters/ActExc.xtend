package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class ActExc extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findActivationOf(f)
		val consequences = fd.consequences.collectFeatureDecisions

		assertTrue('''All features excluded by activation of «f.name» must be deactivated.''', f.
			deactivableCTCFeaturesForActivateF.forall [ df |
				consequences.exists[deactivationOf(df)]
			])
	}
}
