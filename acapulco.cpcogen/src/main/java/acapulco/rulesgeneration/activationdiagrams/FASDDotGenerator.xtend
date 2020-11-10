package acapulco.rulesgeneration.activationdiagrams

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

	private def notInFASD(ActivationDiagramNode node) {
		(node instanceof FeatureDecision) && (fasd.presenceConditions.get(node) === null)
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
		} else '''
			<<TABLE BORDER="0"><TR><TD>«fd»</TD></TR><TR><TD>«fasd.presenceConditions.get(fd).map[name].join(',<BR/>')»</TD></TR></TABLE>>
		'''
	}

	private def renderEdges() {
		val visited = new HashSet<ActivationDiagramNode>
		fasd.rootDecision.recursivelyRenderConsequenceEdges(visited)
	}

	private def String recursivelyRenderConsequenceEdges(ActivationDiagramNode node,
		Set<ActivationDiagramNode> visited) {
		if (visited.contains(node) ||  node.notInFASD) {
			return ""
		}

		visited += node

		'''
			«node.consequences.reject[notInFASD].map['''«node.nodeID» -> «it.nodeID»'''].join('\n')»
			«node.consequences.map[recursivelyRenderConsequenceEdges(visited)].join('\n')»
		'''
	}

	private dispatch def String nodeID(AndNode andNode) '''"AND_«andNodeRegistry.get(andNode)»"'''

	private dispatch def String nodeID(OrNode orNode) '''"OR_«orNodeRegistry.get(orNode)»"'''

	private dispatch def String nodeID(FeatureDecision fd) '''"«fd»"'''
}
