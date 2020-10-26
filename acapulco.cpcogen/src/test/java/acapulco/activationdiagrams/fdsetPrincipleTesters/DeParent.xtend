package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class DeParent extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (!fd.value) {
			if (!fd.key.isOptional) {
				assertFDCheck(
					'''Parent of feature «fd.key.name» must be deactivated by this rule instance.''',
					featureDecisions,
					featureDecisions.hasDeactivationFor(fd.key.parentFeature)
				)
			}
		}
	}

}
