package acapulco.rulesgeneration.activationdiagrams

import java.util.HashMap
import java.util.HashSet
import java.util.Set

/**
 * Generates a dot file from a feature-activation sub-diagram so that it can be rendered with GraphViz for debugging and publication purposes.
 */
class FASDDotGenerator {
	val FeatureActivationSubDiagram fasd
	
	new(FeatureActivationSubDiagram fasd) {
		this.fasd = fasd
	}
	
	def String render() '''
		digraph "«fasd.rootDecision»" {
			«renderNodes»
			
			«renderEdges»
		}
	'''
	
	private def renderNodes() {
		fasd.subdiagramContents.map[renderNode].join('\n')
	}
	
	var andNodeIndex = 0
	var orNodeIndex = 0
	val andNodeRegistry = new HashMap<AndNode, Integer>
	val orNodeRegistry = new HashMap<OrNode, Integer>
	
	private dispatch def String renderNode(AndNode andNode) {
		val ID = andNodeIndex++
		andNodeRegistry.put(andNode, ID)
		'''«andNode.nodeID» [shape = rectangle]'''
	}
	private dispatch def String renderNode(OrNode orNode) {
		val ID = orNodeIndex++
		orNodeRegistry.put(orNode, ID)
		'''«orNode.nodeID» [shape = diamond]'''
	}
	private dispatch def String renderNode(FeatureDecision decision) '''
		«decision.nodeID» [fillcolor = lightgrey, style = filled]
	'''
	
	private def renderEdges() {
		val visited = new HashSet<ActivationDiagramNode>
		fasd.rootDecision.recursivelyRenderConsequenceEdges(visited)
	}
	
	private def String recursivelyRenderConsequenceEdges(ActivationDiagramNode node, Set<ActivationDiagramNode> visited) {
		if (visited.contains(node)) {
			return ""
		}
		
		visited += node
		
		'''
			«node.consequences.map['''«node.nodeID» -> «it.nodeID»'''].join('\n')»
			«node.consequences.map[recursivelyRenderConsequenceEdges(visited)].join('\n')»
		'''
	}
	
	private dispatch def String nodeID(AndNode andNode) '''"AND_«andNodeRegistry.get(andNode)»"'''
	private dispatch def String nodeID(OrNode orNode) '''"OR_«orNodeRegistry.get(orNode)»"'''
	private dispatch def String nodeID(FeatureDecision fd) '''"«fd»"'''
}