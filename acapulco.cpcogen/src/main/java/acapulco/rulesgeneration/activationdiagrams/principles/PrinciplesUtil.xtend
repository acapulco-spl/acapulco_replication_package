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

	static def Set<Set<Set<FeatureDecision>>> applyPrinciples(FeatureDecision fd, FeatureModelHelper fmHelper) {
		(if (fd.activate) {
			activationPrinciples.map[applyTo(fd, fmHelper)]
		} else {
			deactivationPrinciples.map[applyTo(fd, fmHelper)]
		})
		.reject[s | // Remove all application results that were empty 
			s.empty || s.forall[empty]
		]
		.toSet
	}
}
