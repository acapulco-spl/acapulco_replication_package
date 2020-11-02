package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.featuremodel.FeatureModelHelper
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision

class DeParent implements ApplicationPrinciple {
	
	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (!fd.activate) {			
			val parentFeature = fd.feature.parentFeature
			
			if ((parentFeature !== null) && !fmHelper.alwaysActiveFeatures.contains(parentFeature) && !fd.feature.isOptional) {
				deactivate(parentFeature).only
			} else {
				emptySet
			}
		} else {
			emptySet
		}
	}
}