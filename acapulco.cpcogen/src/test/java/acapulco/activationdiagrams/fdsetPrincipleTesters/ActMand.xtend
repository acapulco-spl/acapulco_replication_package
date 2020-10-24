package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class ActMand extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (fd.value) {
			assertFDCheck(
				'''All mandatory children of «fd.key.name» must be activated by this rule instance.''',
				featureDecisions,
				fd.key.mandatoryChildren.forall[f|featureDecisions.hasActivationFor(f)]
			)
		}
	}

}
