package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class DeChild extends FADPrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findDeactivationOf(f)
		val consequences = fd.consequences.collectFeatureDecisions

		assertTrue('''All children of «f.name» must be deactivated.''', f.ownedFeatures.forall [ child |
			consequences.exists[deactivationOf(child)]
		])
	}

}
