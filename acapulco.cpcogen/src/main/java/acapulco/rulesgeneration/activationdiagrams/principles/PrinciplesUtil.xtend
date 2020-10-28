package acapulco.rulesgeneration.activationdiagrams.principles

import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.featuremodel.FeatureModelHelper

class PrinciplesUtil {
	static Set<ApplicationPrinciple> activationPrinciples = #{
		new ActMand,
		new ActPar,
		new ActReq,
		new ActGroup,
		new ActXor,
		new ActExc
	}
	static Set<ApplicationPrinciple> deactivationPrinciples = #{
		new DeChild,
		new DeXor,
		new DeOr,
		new DeParent,
		new DeReq
	}

	static def Set<Set<Set<FeatureDecision>>> applyPrinciples(FeatureDecision fd,
		extension FeatureModelHelper fmHelper) {
		(if (fd.activate) {
			activationPrinciples.map[applyTo(fd, fmHelper)]
		} else {
			deactivationPrinciples.map[applyTo(fd, fmHelper)]
		}).map [ // Ensure we never include any decisions about always active features
			map[
				reject[
					alwaysActiveFeatures.contains(feature) || alwaysActiveGroupFeatures.contains(feature)
				].toSet
			].reject[empty].toSet
		].reject[empty].toSet
	}
}
