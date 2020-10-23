package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class DeOr extends FADPrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (f.parentFeature.isORGroup) {
			val fd = activationDiagram.findDeactivationOf(f)
			val siblings = f.siblings

			val consequences = fd.consequences.collectOrNodes

			assertTrue('''Must have a disjunction of activations for all siblings of «f.name» and the deactivation of its parent feature.''', consequences.
				exists [ orNode |
					val fds = orNode.collectFeatureDecisions;

					(fds.size === siblings.size + 1) && siblings.forall[sibling|fds.exists[activationOf(sibling)]] &&
						fds.exists [
							deactivationOf(f.parentFeature)
						]
				])
		}
	}
}
