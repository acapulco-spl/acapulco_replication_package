package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class DeOr extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (!fd.value) {
			if (fd.key.parentFeature.isORGroup) {
				assertFDCheck(
					'''One sibling of «fd.key.name» must be activated in this rule instance or the rule instance must deactivate the parent of this feature''',
					featureDecisions,
					featureDecisions.hasDeactivationFor(fd.key.parentFeature) || fd.key.siblings.exists [ f |
						featureDecisions.hasActivationFor(f)
					]
				)
			}
		}
	}

}
