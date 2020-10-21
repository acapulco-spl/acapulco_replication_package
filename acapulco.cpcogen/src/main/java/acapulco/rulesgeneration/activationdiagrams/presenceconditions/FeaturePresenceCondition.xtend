package acapulco.rulesgeneration.activationdiagrams.presenceconditions

import java.util.Map
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import org.eclipse.xtend.lib.annotations.Data

@Data
class FeaturePresenceCondition extends PresenceCondition {
	val VBRuleFeature feature

	override resolve(Map<FeatureDecision, Set<PresenceCondition>> presenceConditions) { }
	
	override needsResolving() { false }
	
	override resolvedCondition() {
		#{feature}
	}
	
}
