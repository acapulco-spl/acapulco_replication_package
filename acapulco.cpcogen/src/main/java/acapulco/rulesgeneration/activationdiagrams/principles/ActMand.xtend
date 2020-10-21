package acapulco.rulesgeneration.activationdiagrams.principles

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

/**
 * Activate the mandatory children of an activated feature
 */
class ActMand implements ApplicationPrinciple {
	
	override applyTo(FeatureDecision fd, FeatureModelHelper fmHelper) {
		if (fd.activate) {
			allOf(fmHelper.getMandatoryChildren(fd.feature).map[activate])
		} else {
			emptySet
		}
	}
	
}