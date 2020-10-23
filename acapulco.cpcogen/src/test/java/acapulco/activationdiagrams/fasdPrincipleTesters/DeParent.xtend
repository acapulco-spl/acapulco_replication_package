package acapulco.activationdiagrams.fasdPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class DeParent extends FADPrincipleTester {
	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		if (f.parentFeature.mandatoryChildren.contains(f)) {
			val fd = activationDiagram.findDeactivationOf(f)
			val consequences = fd.consequences.collectFeatureDecisions

			assertTrue('''Parent of mandatory feature «f.name» must be deactivated.''', consequences.exists [
				deactivationOf(f.parentFeature)
			])
		}
	}
}
