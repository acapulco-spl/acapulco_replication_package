package acapulco.rulesgeneration.activationdiagrams

import java.util.ArrayList
import java.util.List

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

	public val ID = freshID
		
	def getConsequences() { consequences }
	
	static var lastID = 0
	static def freshID() {
		lastID++
	}
}