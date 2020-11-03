package acapulco.rulesgeneration.activationdiagrams.vbrulefeatures

import acapulco.rulesgeneration.activationdiagrams.OrNode
import org.eclipse.xtend.lib.annotations.Data

@Data
class VBRuleOrAlternative extends VBRuleFeature {
	val OrNode orNode
	val int alternativeID
	
	override toString() '''OrAlternative«alternativeID» «name»'''
}
