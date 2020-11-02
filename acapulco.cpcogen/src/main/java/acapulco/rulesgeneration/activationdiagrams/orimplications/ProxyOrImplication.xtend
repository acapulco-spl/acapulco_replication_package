package acapulco.rulesgeneration.activationdiagrams.orimplications

import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.Data

@Data
class ProxyOrImplication extends OrImplication {
	
	val ActivationDiagramNode node

	override resolve(Map<ActivationDiagramNode, Set<OrImplication>> orImplications, Set<ActivationDiagramNode> visited) {
		if (visited.contains(node)) {
			emptySet
		} else {
			visited += node
			orImplications.get(node).flatMap[resolve(orImplications, visited)].toSet
			// We don't have to remove node from the visited list as everything get conjoined together
		}
	}
}