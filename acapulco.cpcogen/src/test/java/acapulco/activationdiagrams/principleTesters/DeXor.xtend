package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class DeXor extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (f.parentFeature.isXORGroup) {
			val fd = activationDiagram.findDeactivationOf(f)
			val consequences = fd.consequences.collectOrNodes
			val siblings = f.parentFeature.ownedFeatures.reject[it === f].toSet

			assertTrue('''Must have a disjunction of activations for all siblings of «f.name».''', consequences.exists [ orNode |
				val fds = orNode.collectFeatureDecisions;

				(fds.size === siblings.size) && siblings.forall[sibling|fds.exists[activationOf(sibling)]]
			])
		}
	}
}
