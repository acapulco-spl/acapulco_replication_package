package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Set
import org.eclipse.xtend.lib.annotations.Accessors
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative

/**
 * The graph of or-implications in a FASD.
 */
class OrImplicationGraph {
	val FeatureActivationSubDiagram fasd

	@Accessors(PUBLIC_GETTER)
	val nodes = new HashSet<VBRuleFeature>
	@Accessors(PUBLIC_GETTER)
	val edges = new HashMap<VBRuleFeature, List<? extends VBRuleFeature>>
	// The or alternatives implying a given or-feature
	val invertedOrImplications = new HashMap<VBRuleOrFeature, Set<VBRuleFeature>>

	new(FeatureActivationSubDiagram fasd) {
		this.fasd = fasd

		initialise
	}

	private def initialise() {
		nodes += fasd.vbRuleFeatures.collectFeatures

		fasd.vbRuleFeatures.children.forEach [ orFeature |
			edges.put(orFeature, new ArrayList(orFeature.children))
		]
		fasd.orImplications.entrySet.forEach [ e |
			edges.put(e.key, e.value.toList)

			e.value.forEach [ orAlternativeFeature |
				var invertedSet = invertedOrImplications.get(orAlternativeFeature)
				if (invertedSet === null) {
					invertedSet = new HashSet<VBRuleFeature>
					invertedOrImplications.put(orAlternativeFeature, invertedSet)
				}
				invertedSet += e.key
			]
		]

		computeCycles
	}

	/**
	 * Cycle information: each or feature in the keyset is in at least one cycle, the set of mapped or alternatives are all 
	 * alternatives that are outside the cycle and lead directly into the cycle. In other words, the cycle should only be active 
	 * if at least one of the mapped elements is active. This can be captured by requiring that the or-node can only be active 
	 * if at least one of the mapped alternatives is also active.
	 */
	@Accessors(PUBLIC_GETTER)
	val cycleEntries = new HashMap<VBRuleOrFeature, Set<VBRuleFeature>>

	/**
	 * For every cycle, we only need to find:
	 * 
	 * 1. One back edge (i.e. or alternative) to break to break the cycle when needed, and
	 * 2. All or-alternatives leading into the cycle but not part of the cycle itself.
	 * 
	 * We do this through a single depth-first search sweep through the or-implications graph.
	 */
	private def computeCycles() {
		val visited = new HashSet<VBRuleFeature>
		// Stack captures what we have visited in the current path rather than in the graph globally
		// We use a set for the stack as we will have to quickly look up things deeper in the stack and a hash set makes this O(1)
		val stack = new HashSet<VBRuleOrFeature>

		fasd.vbRuleFeatures.recursivelyComputeCycles(stack, visited, null)
	}
	
	/**
	 * @return the set of features for which we have found a cycle
	 */
	// This variant should only be invoked for the root feature
	private dispatch def Set<VBRuleOrFeature> recursivelyComputeCycles(VBRuleFeature feature, Set<VBRuleOrFeature> stack, Set<VBRuleFeature> visited, VBRuleOrAlternative comingFrom) {
		if (visited.contains(feature)) {
			return emptySet
		}
		
		visited += feature
		
		edges.get(feature)?.forEach[recursivelyComputeCycles(stack, visited, null)]
		
		return emptySet
	}
	
	private dispatch def Set<VBRuleOrFeature> recursivelyComputeCycles(VBRuleOrAlternative feature, Set<VBRuleOrFeature> stack, Set<VBRuleFeature> visited, VBRuleOrAlternative comingFrom) {
		if (visited.contains(feature)) {
			// We're only detecting cycles over or-features, so all we have to do is break off here 
			return emptySet
		}
		
		visited += feature
		
		// Remove feature from cycle entries for any or feature on the stack
		stack.filter(VBRuleOrFeature).filter[cycleEntries.containsKey(it)].forEach[orFeature |
			cycleEntries.get(orFeature).remove(feature)
		]
				
		edges.get(feature)?.flatMap[recursivelyComputeCycles(stack, visited, feature)].toSet
	}
	
	private dispatch def Set<VBRuleOrFeature> recursivelyComputeCycles(VBRuleOrFeature feature, Set<VBRuleOrFeature> stack, Set<VBRuleFeature> visited, VBRuleOrAlternative comingFrom) {
		if (visited.contains(feature)) {			
			if (stack.contains(feature)) {
				// We've found a cycle, record it...
				var cycleEntriesForFeature = cycleEntries.get(feature)
				if (cycleEntriesForFeature === null) {
					cycleEntriesForFeature = new HashSet
					cycleEntries.put(feature, cycleEntriesForFeature)

					cycleEntriesForFeature += invertedOrImplications.get(feature).reject[it === comingFrom].toList
				} else {
					cycleEntriesForFeature -= comingFrom
				}
								
				return #{feature}
			} else {
				return emptySet
			}
		}
		
		stack += feature
		visited += feature
		
		// TODO: after each return, add our incoming edges to the cycle entries for any cycle set returned
		// CHECK: Do we need to do this in the loop or is it OK to do it once at the end (which would be more efficient)?
		val result = edges.get(feature)?.flatMap[recursivelyComputeCycles(stack, visited, null)].toSet
		
		stack -= feature
		result -= feature
		
		return result
	}
}
