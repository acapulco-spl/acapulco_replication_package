package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class ActGroup extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (fd.value) {
			if (fd.key.isGroup) {
				assertFDCheck(
					'''One of the children of group feature «fd.key.name» must be activated.''',
					featureDecisions,
					fd.key.ownedFeatures.exists[f|featureDecisions.hasActivationFor(f)]
				)
			}
		}
	}

}
