package acapulco.rulesgeneration.activationdiagrams.presenceconditions

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.Data

@Data
class FeaturePresenceCondition extends PresenceCondition {
	val VBRuleFeature feature

	override resolve(Map<FeatureDecision, Set<PresenceCondition>> presenceConditions, Set<FeatureDecision> visited) { #{feature} }	
}
