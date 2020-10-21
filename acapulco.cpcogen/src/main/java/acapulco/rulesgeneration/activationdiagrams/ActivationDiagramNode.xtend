package acapulco.rulesgeneration.activationdiagrams

import java.util.ArrayList
import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * A node in the feature activation diagram.
 * 
 * We're consciously leaving hashcode and equals to reference object identity. This is only changed for feature decision nodes. 
 */
abstract class ActivationDiagramNode {
	/**
	 * The consequences of this node according to our principles.
	 */
	@Accessors(PUBLIC_GETTER)
	var List<ActivationDiagramNode> consequences = new ArrayList<ActivationDiagramNode>
}