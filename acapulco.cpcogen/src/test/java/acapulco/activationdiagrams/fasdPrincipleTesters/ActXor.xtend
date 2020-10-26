package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class ActXor extends FADPrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (f.parentFeature.isXORGroup) {
			val fd = activationDiagram.findActivationOf(f)
			val consequences = fd.consequences.collectFeatureDecisions

			assertTrue('''All siblings of «f.name» must be deactivated.''', f.parentFeature.ownedFeatures.reject [it === f].forall [ sibling |
				consequences.exists[deactivationOf(sibling)]
			])
		}
	}

}
