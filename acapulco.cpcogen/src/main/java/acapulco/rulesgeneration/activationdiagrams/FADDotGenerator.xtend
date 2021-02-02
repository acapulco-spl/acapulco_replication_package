package acapulco.rulesgeneration.activationdiagrams

import java.util.HashMap
import java.util.HashSet
import java.util.Set

/**
 * Generates a dot file from a feature-activation sub-diagram so that it can be rendered with GraphViz for debugging and publication purposes.
 */
class FADDotGenerator {
	val FeatureActivationDiagram fad

	new(FeatureActivationDiagram fad) {
		this.fad = fad
	}
	
	def String render() '''
		strict digraph "FAD" {
			«renderNodes»
			
			«renderEdges»
		}
	'''

	dispatch def Iterable<ActivationDiagramNode> collectConsequencesToNextOr(FeatureDecision fd, Set<ActivationDiagramNode> visited, boolean stopAtOr) {
		if (visited.contains(fd)) {
			return emptySet
		}
		
		visited += fd		
		
		#{fd} +
		fd.consequences.flatMap[collectConsequencesToNextOr(visited, true)]
	}

	dispatch def Iterable<ActivationDiagramNode> collectConsequencesToNextOr(OrNode or, Set<ActivationDiagramNode> visited, boolean stopAtOr) {
		if (visited.contains(or)) {
			return emptySet
		}
		
		visited += or
		
		#{or} + 
		((stopAtOr)?(emptySet):(or.consequences.flatMap[collectConsequencesToNextOr(visited, true)]))
	}

	dispatch def Iterable<ActivationDiagramNode> collectConsequencesToNextOr(AndNode and, Set<ActivationDiagramNode> visited, boolean stopAtOr) {
		if (visited.contains(and)) {
			return emptySet
		}
		
		visited += and
		
		#{and} + 
		and.consequences.flatMap[collectConsequencesToNextOr(visited, true)]
	}

	private def renderNodes() {
		fad.diagramNodes.filter[needToRender].map[renderNode].join('\n')
	}
	
	private dispatch def needToRender(ActivationDiagramNode node) { false }

	private dispatch def needToRender(FeatureDecision node) { true }

	private dispatch def needToRender(AndNode node) { false }

	private dispatch def needToRender(OrNode node) { node.consequences.size > 1 }

	var andNodeIndex = 0
	var orNodeIndex = 0
	val andNodeRegistry = new HashMap<AndNode, Integer>
	val orNodeRegistry = new HashMap<OrNode, Integer>

	private dispatch def String renderNode(AndNode andNode) {
		val ID = andNodeIndex++
		andNodeRegistry.put(andNode, ID)
		'''«andNode.nodeID» [shape = rectangle, label = <AND<SUB>«ID»</SUB>>]'''
	}

	private dispatch def String renderNode(OrNode orNode) {
		val ID = orNodeIndex++
		orNodeRegistry.put(orNode, ID)
		'''«orNode.nodeID» [shape = diamond, label = <OR<SUB>«ID»</SUB>>]'''
	}

	private dispatch def String renderNode(FeatureDecision decision) '''
		«decision.nodeID» [style = filled, fillcolor = lightgrey, label = «decision.renderLabel»]
	'''

	private def renderLabel(FeatureDecision fd) '''"«fd.toString»"'''

	private def renderEdges() {
		val visited = new HashSet<ActivationDiagramNode>

		fad.diagramNodes.head.recursivelyRenderConsequenceEdges(visited)
	}

	private def String recursivelyRenderConsequenceEdges(ActivationDiagramNode node, Set<ActivationDiagramNode> visited) {
		if (visited.contains(node) || !node.needToRender) {
			return ""
		}

		visited += node

		node.consequences.map[renderEdge(node, it, visited)].join('\n')
	}

	private dispatch def String renderEdge(ActivationDiagramNode from, ActivationDiagramNode to,
		Set<ActivationDiagramNode> visited) {
		if (from !== to) {
			defaultRenderEdge(from, to, visited)
		}
		else
			""
	}

	private dispatch def String renderEdge(ActivationDiagramNode from, AndNode to, Set<ActivationDiagramNode> visited) {
		if (from instanceof FeatureDecision) {
			// Everything that comes out of an FD is automatically anded together, so we don't need a special and-node rendering in the graph
			// We remove the edges to the and node, so it gets moved out of the way by GraphViz
			to.consequences.filter[needToRender].map[renderEdge(from, it, visited)].join('\n')
		} else {
			defaultRenderEdge(from, to, visited)
		}
	}

	private dispatch def String renderEdge(ActivationDiagramNode from, OrNode to, Set<ActivationDiagramNode> visited) {
		val toConsequences = to.consequences.filter[needToRender] 
		if (toConsequences.size === 1) {
			// Skip or-nodes if they have only one follower
			renderEdge(from, toConsequences.head, visited)
		} else if (toConsequences.size > 1) {
			defaultRenderEdge(from, to, visited)
		} else 
			""
	}
	
	private def String defaultRenderEdge(ActivationDiagramNode from, ActivationDiagramNode to, Set<ActivationDiagramNode> visited) '''
		«from.nodeID» -> «to.nodeID»
		«recursivelyRenderConsequenceEdges(to, visited)»
	'''
	
	private dispatch def String nodeID(AndNode andNode) '''"AND_«andNodeRegistry.get(andNode)»"'''

	private dispatch def String nodeID(OrNode orNode) '''"OR_«orNodeRegistry.get(orNode)»"'''

	private dispatch def String nodeID(FeatureDecision fd) '''"«fd»"'''
}
