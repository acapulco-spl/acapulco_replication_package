package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class ActXor extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (fd.value) {
			if (fd.key.parentFeature.isXORGroup) {
				assertFDCheck(
					'''All siblings of feature «fd.key.name» must be deactivated as this feature is a member of an XOR group.''',
					featureDecisions,
					fd.key.siblings.forall[f|featureDecisions.hasDeactivationFor(f)]
				)
			}
		}
	}

}
