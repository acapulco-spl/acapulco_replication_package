package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class DeChild implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (!fd.activate) {
			allOf(fd.feature.ownedFeatures.map[deactivate])
		} else {
			emptySet
		}
	}

}
