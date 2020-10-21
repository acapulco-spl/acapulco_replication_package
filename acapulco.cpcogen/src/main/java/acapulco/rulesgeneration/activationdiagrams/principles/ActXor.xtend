package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class ActXor implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, extension FeatureModelHelper fmHelper) {
		if (fd.activate) {
			if (fd.feature.parentFeature.isXORGroup) {
				allOf(fd.feature.parentFeature.ownedFeatures.reject[it === fd.feature].map [deactivate])
			} else {
				emptySet
			}
		} else {
			emptySet
		}
	}

}
