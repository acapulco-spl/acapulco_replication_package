package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.featuremodel.FeatureModelHelper
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision

class ActGroup implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, extension FeatureModelHelper fmHelper) {
		if (fd.activate) {
			if (fd.feature.isORGroup || fd.feature.isXORGroup) {
				oneOf (fd.feature.ownedFeatures.map[activate])
			} else {
				emptySet
			}
		} else {
			emptySet

		}
	}

}
