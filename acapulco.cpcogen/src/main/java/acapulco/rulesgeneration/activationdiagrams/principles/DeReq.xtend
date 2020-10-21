package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class DeReq implements ApplicationPrinciple {
	
	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (!fd.activate) {
			if (fmHelper.hasDeactivableCTCForDeactivateF(fd.feature)) {
				allOf(fmHelper.getDeactivableCTCFeaturesForDeactivateF(fd.feature).map[deactivate])
			} else {
				emptySet
			}
		} else {
			emptySet
		}
	}
}