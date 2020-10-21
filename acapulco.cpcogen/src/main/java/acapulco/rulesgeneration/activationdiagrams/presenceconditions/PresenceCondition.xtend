package acapulco.rulesgeneration.activationdiagrams.presenceconditions

import java.util.Map
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature

abstract class PresenceCondition {
	abstract def boolean needsResolving()
	abstract def void resolve(Map<FeatureDecision, Set<PresenceCondition>> presenceConditions)
	
	/**
	 * Use this to read off the final presence condition
	 */
	abstract def Set<VBRuleFeature> resolvedCondition()
}