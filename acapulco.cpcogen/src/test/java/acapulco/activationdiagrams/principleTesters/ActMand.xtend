package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.model.Feature
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.featuremodel.FeatureModelHelper
import static org.junit.Assert.assertTrue

class ActMand extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (!f.isGroup) {
			val fd = activationDiagram.findActivationOf(f)
			val consequences = fd.consequences.collectFeatureDecisions

			assertTrue('''All mandatory children of «f.name» must be activated.''', f.mandatoryChildren.forall [ mc |
				consequences.exists[activationOf(mc)]
			])
		}
	}

}
