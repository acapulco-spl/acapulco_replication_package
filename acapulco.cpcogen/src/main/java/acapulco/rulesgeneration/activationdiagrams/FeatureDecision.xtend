package acapulco.rulesgeneration.activationdiagrams

import acapulco.model.Feature
import java.util.Set
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * Description of an activation decision for a particular feature.
 */
class FeatureDecision extends ActivationDiagramNode {
	@Accessors(PUBLIC_GETTER)
	val Feature feature
	@Accessors(PUBLIC_GETTER)
	val boolean activate

	new(Feature feature, boolean activate) {
		this.feature = feature
		this.activate = activate
	}

	override Set<FeatureDecision> collectFeatureDecisions() {
		#{this}
	}

	override toString() '''«feature.name»«activate?'+':'-'»'''

	override hashCode() {
		feature.hashCode + (activate ? 1 : 0)
	}

	override equals(Object other) {
		if (other instanceof FeatureDecision) {
			(feature === other.feature) && (activate === other.activate)
		} else {
			false
		}
	}
}
