package acapulco.activationdiagrams.principleTesters

import acapulco.activationdiagrams.PrincipleTester
import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Set

import static org.junit.Assert.assertTrue

class ActPar extends PrincipleTester {

	override checkPrincipleApplies(Feature f, Set<ActivationDiagramNode> activationDiagram,
		extension FeatureModelHelper featureModelHelper) {
		val parent = f.parentFeature
		
		if (!parent.alwaysActive) {
			val fd = activationDiagram.findActivationOf(f)
			
			assertTrue('''Parent of feature «f.name» must be activated.''', fd.consequences.collectFeatureDecisions.
				exists[activationOf(parent)])
		}
	}
}
