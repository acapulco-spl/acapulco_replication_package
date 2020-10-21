package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.featuremodel.FeatureModelHelper
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision

class DeOr implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, extension FeatureModelHelper fmHelper) {
		if (!fd.activate) {
			if (fd.feature.parentFeature.isORGroup) {
				// Activate one of our sibling features
				oneOf(fd.feature.parentFeature.ownedFeatures.reject[it === fd.feature].map [activate]).
				// OR deactivate the parent feature
				or(fd.feature.parentFeature.deactivate)
			} else {
				emptySet
			}
		} else {
			emptySet
		}
	}
}
