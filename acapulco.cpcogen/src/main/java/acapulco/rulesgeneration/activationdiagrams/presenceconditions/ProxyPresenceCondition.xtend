package acapulco.rulesgeneration.activationdiagrams.presenceconditions

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import java.util.HashSet
import java.util.Map
import java.util.Set

class ProxyPresenceCondition extends PresenceCondition {
	val FeatureDecision fd
	var Set<PresenceCondition> resolvedConditions = null

	new(FeatureDecision fd) {
		this.fd = fd
	}

	override resolve(Map<FeatureDecision, Set<PresenceCondition>> presenceConditions) {
		if (resolvedConditions === null) {
			resolvedConditions = new HashSet<PresenceCondition>(presenceConditions.get(fd))
		} else {
			val condsToResolve = resolvedConditions.filter[needsResolving]
			if (!condsToResolve.empty) {
				condsToResolve.forEach[resolve(presenceConditions)]
				resolvedConditions = new HashSet<PresenceCondition>(resolvedConditions.flatMap [ pc |
					if (pc instanceof ProxyPresenceCondition) {
						if (pc.resolvedConditions !== null) {
							pc.resolvedConditions
						} else {
							#{pc as PresenceCondition}
						}
					} else {
						#{pc}
					}
				].reject [ pc | // Remove cycles
					if (pc instanceof ProxyPresenceCondition) {
						pc.fd === fd
					} else {
						false
					}
				].toSet)
			}
		}
	}

	override needsResolving() { (resolvedConditions === null) || resolvedConditions.exists[needsResolving] }

	override resolvedCondition() {
		if (needsResolving) {
			throw new IllegalStateException("Proxy PCs must be resolved before they can be read off")
		}

		resolvedConditions.flatMap [ pc |
			if (pc instanceof ProxyPresenceCondition) {
				pc.resolvedConditions.flatMap[resolvedCondition]
			} else {
				pc.resolvedCondition
			}
		].toSet		
	}

}
