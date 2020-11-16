package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative

class OrImplicationDotGenerator {
	val OrImplicationGraph orImplications
	val FeatureActivationSubDiagram fasd

	new(FeatureActivationSubDiagram fasd) {
		this.fasd = fasd
		orImplications = new OrImplicationGraph(fasd)
	}

	def String render() '''
		digraph "«fasd.rootDecision»" {
			«renderNodes»
			
			«renderEdges»
		}
	'''

	// TODO: Render nodes in sub-graphs according to the connected components, always keeping the component containing root at the top.

	private def renderNodes() {
		orImplications.nodes.map[renderNode].join('\n')
	}

	private def renderEdges() {
		orImplications.edges.map[renderEdge].join('\n')
	}

	private dispatch def renderNode(VBRuleFeature node) { node.renderAlternativeNode }

	private dispatch def renderNode(VBRuleOrFeature node) '''
		«node.name» [shape=diamond]
	'''

	private dispatch def renderNode(VBRuleOrAlternative node) { node.renderAlternativeNode }

	private def renderAlternativeNode(VBRuleFeature node) '''
		«node.name» [shape=rectangle]
	'''
	
	private def renderEdge(Pair<VBRuleFeature, VBRuleFeature> edge) '''
		«edge.key.name» -> «edge.value.name»
	'''
}
