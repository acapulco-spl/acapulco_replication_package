package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class DeChild extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val fd = activationDiagram.findDeactivationOf(f)
		val consequences = fd.consequences.collectFeatureDecisions

		assertTrue('''All children of «f.name» must be deactivated.''', f.ownedFeatures.forall [ child |
			consequences.exists[deactivationOf(child)]
		])
	}

}
