package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class ActPar extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (fd.value) {
			if (!fd.key.parentFeature.alwaysActive) {
				assertFDCheck(
					'''Parent of feature «fd.key.name» must be activated.''',
					featureDecisions,
					featureDecisions.hasActivationFor(fd.key.parentFeature)
				)
			}
		}
	}

}
