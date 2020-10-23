package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class ActGroup extends FADPrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (f.isGroup) {
			val fd = activationDiagram.findActivationOf(f)
			val consequences = fd.consequences.collectOrNodes

			assertTrue('''Must have a disjunction of activations for all children of «f.name».''',
				consequences.exists [ orNode |
				val fds = orNode.collectFeatureDecisions;

				(fds.size === f.ownedFeatures.size) && f.ownedFeatures.forall [ ownedFeature |
					fds.exists[activationOf(ownedFeature)]
				]
			])
		}
	}

}
