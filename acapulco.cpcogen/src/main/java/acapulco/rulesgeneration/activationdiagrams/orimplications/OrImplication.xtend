package acapulco.rulesgeneration.activationdiagrams.orimplications

import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.Map
import java.util.Set

abstract class OrImplication {
	abstract def Set<VBRuleOrFeature> resolve(Map<ActivationDiagramNode, Set<OrImplication>> orImplications,
		Set<ActivationDiagramNode> visited)
}
