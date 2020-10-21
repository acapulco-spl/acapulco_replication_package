package acapulco.rulesgeneration.activationdiagrams.orimplications

import java.util.Map
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import org.eclipse.xtend.lib.annotations.Data

@Data
class FinalisedOrImplication extends OrImplication {

	val VBRuleOrFeature node

	override resolve(Map<ActivationDiagramNode, Set<OrImplication>> orImplications) {}

	override needsResolving() { false }

	override Set<VBRuleOrFeature> resolvedImplication() { #{node} }

}
