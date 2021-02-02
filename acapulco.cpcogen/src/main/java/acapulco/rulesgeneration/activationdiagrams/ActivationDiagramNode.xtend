package acapulco.rulesgeneration.activationdiagrams

import java.util.ArrayList
import java.util.List
import java.util.Set

/**
 * A node in the feature activation diagram.
 * 
 * We're consciously leaving hashcode and equals to reference object identity. This is only changed for feature decision nodes. 
 */
abstract class ActivationDiagramNode {
	/**
	 * The consequences of this node according to our principles.
	 */
	var List<ActivationDiagramNode> consequences = new ArrayList<ActivationDiagramNode>
	
	def getConsequences() { consequences }

	/**
	 * Collect the nearest feature decisions.
	 */
	def Set<FeatureDecision> collectFeatureDecisions() {
		consequences.flatMap[collectFeatureDecisions].toSet
	}
}