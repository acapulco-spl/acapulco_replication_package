package acapulco.rulesgeneration.activationdiagrams.presenceconditions

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import java.util.Map
import java.util.Set

abstract class PresenceCondition {
	abstract def Set<VBRuleFeature> resolve(Map<FeatureDecision, Set<PresenceCondition>> presenceConditions,
		Set<FeatureDecision> visited)
}
