package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class ActExc extends FADPrincipleTester {

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
