package acapulco.rulesgeneration.activationdiagrams.vbrulefeatures

import acapulco.rulesgeneration.activationdiagrams.OrNode
import org.eclipse.xtend.lib.annotations.Data

@Data
class VBRuleOrFeature extends VBRuleFeature {
	val OrNode orNode
	
	override toString() '''OR«ID»: «name»'''
}
