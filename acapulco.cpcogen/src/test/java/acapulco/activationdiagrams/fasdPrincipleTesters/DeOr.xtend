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

			if (!alwaysActiveFeatures.contains(f.parentFeature)) {
				val consequences = fd.consequences.collectOrNodes

				assertTrue('''Must have a disjunction of activations for all siblings of «f.name» and the deactivation of its parent feature.''', consequences.
					exists [ orNode |
						val fds = orNode.collectFeatureDecisions;

						(fds.size === siblings.size + 1) &&
							siblings.forall[sibling|fds.exists[activationOf(sibling)]] && fds.exists [
								deactivationOf(f.parentFeature)
							]
					])

			} else {
				if (siblings.size == 1) {
					// Edge case: there is only one sibling and we're not offering the option to deactivate the parent, so there won't be an or-node
					val consequences = fd.consequences.collectFeatureDecisions
					assertTrue('''Must have an activation of the single sibling of «f.name».''', consequences.exists [
						activationOf(siblings.head)
					])
				} else {
					// We expect an or node
					val consequences = fd.consequences.collectOrNodes

					assertTrue('''Must have a disjunction of activations for all siblings of «f.name».''', consequences.
						exists [ orNode |
							val fds = orNode.collectFeatureDecisions;

							(fds.size === siblings.size) && siblings.forall[sibling|fds.exists[activationOf(sibling)]]
						])
				}
			}
		}
	}
}
