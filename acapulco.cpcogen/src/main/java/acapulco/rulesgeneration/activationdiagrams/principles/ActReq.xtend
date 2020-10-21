package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class ActReq implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (fd.activate) {
			val requiredFeatures = fmHelper.getActivableCTCFeaturesForActivateF(fd.feature)

			allOf(requiredFeatures.map[activate])
		} else {
			emptySet
		}
	}
}
