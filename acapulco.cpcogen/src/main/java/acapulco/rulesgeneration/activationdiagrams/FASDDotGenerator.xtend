package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.HashMap
import java.util.HashSet
import java.util.Set

/**
 * Generates a dot file from a feature-activation sub-diagram so that it can be rendered with GraphViz for debugging and publication purposes.
 */
class FASDDotGenerator {
	val FeatureActivationSubDiagram fasd
	val boolean showPCs

	new(FeatureActivationSubDiagram fasd, boolean showPCs) {
		this.fasd = fasd
		this.showPCs = showPCs
	}

	def String render() '''
		digraph "«fasd.rootDecision»" {
			«renderNodes»
			
			«renderEdges»
		}
	'''

	private def renderNodes() {
		fasd.subdiagramContents.reject[notInFASD].map[renderNode].join('\n')
	}

	private dispatch def notInFASD(ActivationDiagramNode node) { false }

	private dispatch def notInFASD(FeatureDecision node) { fasd.presenceConditions.get(node) === null }

	private dispatch def notInFASD(AndNode node) { false }

	private dispatch def notInFASD(OrNode node) {
		fasd.vbRuleFeatures.children.filter(VBRuleOrFeature).findFirst[orNode === node] === null
	}

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

	private dispatch def String renderNode(FeatureDecision decision) {
		if (fasd.rootDecision === decision)
			'''
				subgraph { rank = source
					«decision.nodeID» [style = filled, fillcolor = lightblue, label = «decision.renderLabel»]
				}
			'''
		else
			'''
				«decision.nodeID» [style = filled, fillcolor = lightgrey, label = «decision.renderLabel»]
			'''
	}

	private def renderLabel(FeatureDecision fd) {
		if (!showPCs) {
			fd.toString
		} else
			'''
				<<TABLE BORDER="0"><TR><TD>«fd»</TD></TR><TR><TD>«fasd.presenceConditions.get(fd).map[name].join(',<BR/>')»</TD></TR></TABLE>>
			'''
	}

	private def renderEdges() {
		val visited = new HashSet<ActivationDiagramNode>
		fasd.rootDecision.recursivelyRenderConsequenceEdges(visited)
	}

	private def String recursivelyRenderConsequenceEdges(ActivationDiagramNode node, Set<ActivationDiagramNode> visited) {
		if (visited.contains(node) || node.notInFASD) {
			return ""
		}

		visited += node

		val realOutgoingEdges = node.consequences.reject[notInFASD]
		realOutgoingEdges.map[renderEdge(node, it, visited)].join('\n')
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
			to.consequences.reject[notInFASD].map[renderEdge(from, it, visited)].join('\n')
		} else {
			defaultRenderEdge(from, to, visited)
		}
	}

	private dispatch def String renderEdge(ActivationDiagramNode from, OrNode to, Set<ActivationDiagramNode> visited) {
		val toConsequences = to.consequences.reject[notInFASD] 
		if (toConsequences.size === 1) {
			// Skip or-nodes if they have only one follower
			renderEdge(from, toConsequences.head, visited)
		} else if (toConsequences.size > 1) {
			defaultRenderEdge(from, to, visited)			
		}
	}
	
	private def String defaultRenderEdge(ActivationDiagramNode from, ActivationDiagramNode to, Set<ActivationDiagramNode> visited) '''
		«from.nodeID» -> «to.nodeID»
		«recursivelyRenderConsequenceEdges(to, visited)»
	'''
	
	private dispatch def String nodeID(AndNode andNode) '''"AND_«andNodeRegistry.get(andNode)»"'''

	private dispatch def String nodeID(OrNode orNode) '''"OR_«orNodeRegistry.get(orNode)»"'''

	private dispatch def String nodeID(FeatureDecision fd) '''"«fd»"'''
}
