package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class DeParent implements ApplicationPrinciple {
	
	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (!fd.activate) {
			if (!fmHelper.alwaysActiveFeatures.contains(fd.feature.parentFeature) && !fd.feature.isOptional) {
				deactivate(fd.feature.parentFeature).only
			} else {
				emptySet
			}
		} else {
			emptySet
		}
	}
}