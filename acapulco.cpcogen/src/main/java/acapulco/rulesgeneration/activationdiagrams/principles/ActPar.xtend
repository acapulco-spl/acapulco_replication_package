package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class ActPar implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (fd.activate) {
			val parent = fd.feature.parentFeature

			if ((parent !== null) && (!fmHelper.getAlwaysActiveFeatures().contains(parent))) {
				activate(parent).only
			} else {
				emptySet
			}
		} else {
			emptySet
		}
	}

}
