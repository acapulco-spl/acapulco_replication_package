package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class ActExc implements ApplicationPrinciple {

	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (fd.activate) {
			// TODO: Check that this includes exclusions in both directions...
			val excludedFeatures = fmHelper.getDeactivableCTCFeaturesForActivateF(fd.feature)

			allOf(excludedFeatures.map[deactivate])
		} else {
			emptySet
		}
	}
}
