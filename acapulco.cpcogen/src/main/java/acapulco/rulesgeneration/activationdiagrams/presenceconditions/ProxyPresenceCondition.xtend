package acapulco.rulesgeneration.activationdiagrams.presenceconditions

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.Data

@Data
class ProxyPresenceCondition extends PresenceCondition {
	val FeatureDecision fd

	override resolve(Map<FeatureDecision, Set<PresenceCondition>> presenceConditions, Set<FeatureDecision> visited) {
		if (visited.contains(fd)) {
			emptySet
		} else {
			visited += fd
			presenceConditions.get(fd).flatMap[resolve(presenceConditions, visited)].toSet
		// We don't need to remove fd from visited: once we have resolved the PC for a particular FD anywhere in the overall PC, we don't need to resolve it anywhere else again as everything gets flattened to a disjunction
		}
	}
}
