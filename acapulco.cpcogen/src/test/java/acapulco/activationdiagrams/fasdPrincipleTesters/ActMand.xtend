package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue
import static extension acapulco.rulesgeneration.activationdiagrams.FADHelper.*

class ActMand extends FADPrincipleTester {

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
