package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class DeOr extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (f.parentFeature.isORGroup) {
			val fd = activationDiagram.findDeactivationOf(f)
			val siblings = f.siblings

			if (siblings.size > 1) {
				val consequences = fd.consequences.collectOrNodes

				assertTrue('''Must have a disjunction of activations for all siblings of «f.name».''', consequences.
					exists [ orNode |
						val fds = orNode.collectFeatureDecisions;

						(fds.size === siblings.size) && siblings.forall[sibling|fds.exists[activationOf(sibling)]]
					])
			} else if (siblings.size === 1) {
				assertTrue('''Must have an activation of the sibling of «f.name».''',
					fd.consequences.collectFeatureDecisions.exists[activationOf(siblings.head)]
				)
			}
		}
	}
}
