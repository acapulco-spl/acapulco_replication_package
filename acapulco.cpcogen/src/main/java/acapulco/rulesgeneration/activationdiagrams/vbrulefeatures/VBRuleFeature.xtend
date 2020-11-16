package acapulco.rulesgeneration.activationdiagrams.vbrulefeatures

import java.util.ArrayList
import java.util.List
import org.eclipse.xtend.lib.annotations.Data

@Data
class VBRuleFeature {
	val String name
	val int ID = freshID
	
	val List<VBRuleFeature> children = new ArrayList<VBRuleFeature>
	
	override toString() { name }
	
	static var int lastID = 0
	private static def getFreshID() {
		lastID++
	}
	
	def Iterable<VBRuleFeature> collectFeatures() {
		children.flatMap[collectFeatures] + #{this}
	}
}
