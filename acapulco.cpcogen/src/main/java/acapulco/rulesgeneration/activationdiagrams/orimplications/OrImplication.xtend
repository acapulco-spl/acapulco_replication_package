package acapulco.rulesgeneration.activationdiagrams.orimplications

import java.util.Map
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature

abstract class OrImplication {
	abstract def boolean needsResolving()
	abstract def void resolve(Map<ActivationDiagramNode, Set<OrImplication>> orImplications)
	
	/**
	 * Use this to read off the final or implication
	 */
	abstract def Set<VBRuleOrFeature> resolvedImplication()
}
