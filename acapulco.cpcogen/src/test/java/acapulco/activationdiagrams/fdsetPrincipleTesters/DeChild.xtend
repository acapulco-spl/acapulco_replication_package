package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class DeChild extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (! fd.value) {
			assertFDCheck(
				'''All children of feature «fd.key.name» must be deactivated by this rule instance.''',
				featureDecisions,
				fd.key.ownedFeatures.forall[f|featureDecisions.hasDeactivationFor(f)]
			)
		}
	}

}
