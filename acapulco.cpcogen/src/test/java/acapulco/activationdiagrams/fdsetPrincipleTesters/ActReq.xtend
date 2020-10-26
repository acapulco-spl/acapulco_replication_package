package acapulco.activationdiagrams.fdsetPrincipleTesters

import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import java.util.Set

class ActReq extends FeatureDecisionSetPrincipleTester {

	override checkPrincipleApplies(Pair<Feature, Boolean> fd, Set<Pair<Feature, Boolean>> featureDecisions,
		extension FeatureModelHelper featureModelHelper) {
		if (fd.value) {
			assertFDCheck(
				'''All features required by «fd.key.name» must be activated.''',
				featureDecisions,
				fd.key.activableCTCFeaturesForActivateF.forall[f|featureDecisions.hasActivationFor(f)]
			)
		}
	}

}
