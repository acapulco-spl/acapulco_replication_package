package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Stack
import org.eclipse.xtend.lib.annotations.Accessors

import static extension acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram.*

/**
 * The graph of or-implications in a FASD.
 */
class OrImplicationGraph {
	val FeatureActivationSubDiagram fasd

	@Accessors(PUBLIC_GETTER)
	val nodes = new HashSet<VBRuleFeature>
	@Accessors(PUBLIC_GETTER)
	val edges = new HashSet<Pair<VBRuleFeature, VBRuleFeature>>
	val internalEdges = new HashMap<VBRuleFeature, List<VBRuleFeature>>
	val internalTransposedEdges = new HashMap<VBRuleFeature, List<VBRuleFeature>>
	@Accessors(PUBLIC_GETTER)
	val connectedComponents = new ArrayList<List<VBRuleFeature>>

	new(FeatureActivationSubDiagram fasd) {
		this.fasd = fasd

		initialise
	}

	private def initialise() {
		nodes += fasd.vbRuleFeatures.collectFeatures

		edges += fasd.vbRuleFeatures.children.flatMap[orFeature|orFeature.children.map[orFeature -> it]]
		edges += fasd.orImplications.entrySet.flatMap[e|e.value.map[e.key -> it as VBRuleFeature]]

		// Build an adjacency list for more efficient lookup...
		internalEdges.putAll(edges.groupBy[key].mapValues[map[value]])
		internalTransposedEdges.putAll(edges.groupBy[value].mapValues[map[key]])
		
		computeConnectedComponents
	}

	private def computeConnectedComponents() {
		val stack = new Stack<VBRuleFeature>
		val visited = new HashSet<VBRuleFeature>
		fasd.vbRuleFeatures.findOrder(visited, stack)

		visited.clear
		while (!stack.empty()) {
			val feature = stack.pop

			if (!visited.contains(feature)) {
				connectedComponents += feature.collectConnectedComponent(visited)
			}
		}
	}
	
	private def List<VBRuleFeature> collectConnectedComponent(VBRuleFeature feature, HashSet<VBRuleFeature> visited) {
		if (visited.contains(feature)) {
			return #[]
		}
		
		visited += feature;
		
		val transposedEdges = internalTransposedEdges.get(feature)
		
		if (transposedEdges !== null) {
			(internalTransposedEdges.get(feature).flatMap[collectConnectedComponent(visited)] + #[feature]).toList		
		} else {
			#[feature]
		}
	}

	private def void findOrder(VBRuleFeature feature, HashSet<VBRuleFeature> visited, Stack<VBRuleFeature> orderStack) {
		if (visited.contains(feature)) {
			return
		}

		visited += feature

		internalEdges.get(feature).forEach[findOrder(visited, orderStack)]

		orderStack.push(feature)
	}

}
