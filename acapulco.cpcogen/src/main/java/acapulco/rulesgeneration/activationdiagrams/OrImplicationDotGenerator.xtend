package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.List
import java.util.Map.Entry

class OrImplicationDotGenerator {
	val OrImplicationGraph orImplications
	val FeatureActivationSubDiagram fasd

	new(FeatureActivationSubDiagram fasd) {
		this.fasd = fasd
		orImplications = new OrImplicationGraph(fasd)
	}

	def String render() '''
		strict digraph "«fasd.rootDecision»" { packmode=array_c1;
			«renderNodes»
			
			«renderEdges»
		}
	'''

	private def renderNodes() '''
		subgraph rootGraph { rank = source;
			«fasd.vbRuleFeatures.renderNode»
		}
		
		«orImplications.nodes.reject[it === fasd.vbRuleFeatures].map[renderNode].join('\n')»
	'''

// Uncommented this for now in favour of using the standard DOT packing of connected components	
//
//	private def renderNodes() {
//		orImplications.connectedComponents.map[renderComponent].join('\n')
//	}
//	
//	// TODO: Render nodes in sub-graphs according to the connected components, always keeping the component containing root at the top.
//	private def renderComponent(List<VBRuleFeature> connectedComponent) '''
//		subgraph { rank = «connectedComponent.contains(fasd.vbRuleFeatures)?'source':'same'»;
//			subgraph orNodes { rank = same;
//				«connectedComponent.filter(VBRuleOrFeature).map[renderNode].join('\n')»
//			}
//			
//			subgraph orAlternativeNodes { rank = same;
//				«connectedComponent.filter[(it instanceof VBRuleOrAlternative) || (it === fasd.vbRuleFeatures)].map[renderNode].join('\n')»
//			}
//		}
//	'''

	private def renderEdges() {
		orImplications.edges.entrySet.flatMap[doRenderEdges].join('\n')
	}

	private dispatch def renderNode(VBRuleFeature node) { node.renderAlternativeNode }

	private dispatch def renderNode(VBRuleOrFeature node) '''
		«node.name» [shape=diamond]
	'''

	private dispatch def renderNode(VBRuleOrAlternative node) { node.renderAlternativeNode }

	private def renderAlternativeNode(VBRuleFeature node) '''
		«node.name» [shape=rectangle]
	'''
	
	private def doRenderEdges(Entry<VBRuleFeature, List<? extends VBRuleFeature>> edgeSet) {
		edgeSet.value.map[renderEdge(edgeSet.key, it)]
	} 
	
	private def renderEdge(VBRuleFeature from, VBRuleFeature to) '''
		«from.name» -> «to.name»
	'''
}
