package acapulco.rulesgeneration.activationdiagrams.orimplications

import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.Data

@Data
class FinalisedOrImplication extends OrImplication {

	val VBRuleOrFeature node

	override resolve(Map<ActivationDiagramNode, Set<OrImplication>> orImplications, Set<ActivationDiagramNode> visited) {
		#{node}
	}
}
