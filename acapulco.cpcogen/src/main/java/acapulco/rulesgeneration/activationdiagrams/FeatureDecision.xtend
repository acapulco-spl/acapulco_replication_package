package acapulco.rulesgeneration.activationdiagrams

import acapulco.model.Feature
import org.eclipse.xtend.lib.annotations.Data

/**
 * Description of an activation decision for a particular feature.
 */
@Data
class FeatureDecision extends ActivationDiagramNode {
	Feature feature
	boolean activate
	
	override hashCode() {
		feature.hashCode + (activate?1:0)
	}
	
	override equals(Object other) {
		if (other instanceof FeatureDecision) {
			(feature === other.feature) && (activate === other.activate)
		} else {
			false
		}
	}
}