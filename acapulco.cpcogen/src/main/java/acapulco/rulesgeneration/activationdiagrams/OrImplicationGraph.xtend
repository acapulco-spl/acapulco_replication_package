package acapulco.rulesgeneration.activationdiagrams

import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Set
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.Data

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
	 * Final cycle information: each or feature in the keyset is in at least one cycle, the set of mapped or alternatives are all 
	 * alternatives that are outside the cycle and lead directly into the cycle. In other words, the cycle should only be active 
	 * if at least one of the mapped elements is active. This can be captured by requiring that the or-node can only be active 
	 * if at least one of the mapped alternatives is also active.
	 */
	@Accessors(PUBLIC_GETTER)
	val cycleEntries = new HashMap<VBRuleOrFeature, Set<VBRuleFeature>>
	
	/**
	 * Cycle information as collected. Here, we split this into additions and deletions, so that we can ensure deletions take precedence regardless of when they are collected during the DFS.
	 */
	val internalCycleEntries = new HashMap<VBRuleOrFeature, AdditionDeletionSet> 
	
	@Data
	private static class AdditionDeletionSet {
		val additions = new HashSet<VBRuleFeature>
		val deletions = new HashSet<VBRuleFeature>
		
		def getEffectiveSet() {
			additions.reject[deletions.contains(it)].toSet
		}
		
		def operator_add(AdditionDeletionSet features) {
			additions += features.additions
			deletions += features.deletions
		}
		
		def operator_add(Iterable<VBRuleFeature> features) {
			additions += features
		}

		def operator_remove(VBRuleFeature feature) {
			deletions += feature
		}
	}

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
			
		cycleEntries.clear
		cycleEntries.putAll (internalCycleEntries.mapValues[effectiveSet])
	}

	/**
	 * @return the set of features for which we have found a cycle
	 */
	// This variant should only be invoked for the root feature
	private dispatch def Set<VBRuleOrFeature> recursivelyComputeCycles(VBRuleFeature feature,
		Set<VBRuleOrFeature> stack, Set<VBRuleFeature> visited, VBRuleOrAlternative comingFrom) {

		if (visited.contains(feature)) {
			return emptySet
		}

		visited += feature

		edges.get(feature)?.forEach[recursivelyComputeCycles(stack, visited, null)]

		return emptySet
	}

	private dispatch def Set<VBRuleOrFeature> recursivelyComputeCycles(VBRuleOrAlternative feature,
		Set<VBRuleOrFeature> stack, Set<VBRuleFeature> visited, VBRuleOrAlternative comingFrom) {
		if (visited.contains(feature)) {
			// We're only detecting cycles over or-features, so all we have to do is break off here 
			return emptySet
		}

		visited += feature

		// Remove feature from cycle entries for any or-feature on the stack
		stack.filter(VBRuleOrFeature).filter[internalCycleEntries.containsKey(it)].forEach [ orFeature |
			internalCycleEntries.get(orFeature) -= feature
		]

		val result = edges.get(feature)?.flatMap[recursivelyComputeCycles(stack, visited, feature)]?.toSet

		if (result !== null) {
			result
		} else {
			emptySet
		}
	}

	private dispatch def Set<VBRuleOrFeature> recursivelyComputeCycles(VBRuleOrFeature feature,
		Set<VBRuleOrFeature> stack, Set<VBRuleFeature> visited, VBRuleOrAlternative comingFrom) {
		if (visited.contains(feature)) {
			if (stack.contains(feature)) {
				// We've found a cycle, record it...
				var cycleEntriesForFeature = internalCycleEntries.get(feature)
				if (cycleEntriesForFeature === null) {
					cycleEntriesForFeature = new AdditionDeletionSet
					internalCycleEntries.put(feature, cycleEntriesForFeature)

					val featuresToAdd = invertedOrImplications.get(feature).toList
					cycleEntriesForFeature += featuresToAdd
					cycleEntriesForFeature -= comingFrom
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

		val result = edges.get(feature)?.flatMap[recursivelyComputeCycles(stack, visited, null)]?.toSet

		stack -= feature

		if (result !== null) {
			/*
			 * Add our incoming edges (except those that are on a cycle to this feature to the cycle entries for any cycle set returned
			 * 
			 * Note that doing this here rather than after each descend call is both more efficient and removes the need for indirect
			 * representations of in-edges that need to be resolved later.
			 */
			val cycleEntriesForFeature = internalCycleEntries.get(feature)
			if (cycleEntriesForFeature !== null) {
				/*
				 * During the above descend we have found at least one cycle to this feature. result will contain the feature and we need to
				 * make sure to add only the cycle entries to the cycles still remaining.
				 */
				result -= feature
				result.forEach [ cycleFeature |
					internalCycleEntries.get(cycleFeature) += cycleEntriesForFeature
					internalCycleEntries.get(cycleFeature) -= comingFrom
				]
			} else {
				/*
				 * This feature isn't involved in any cycles as an "endpoint", so all entries, except the one we came in on, need to be added
				 * as potential entry points for any cycle we have found during descent.
				 * 
				 * Note that at this point result cannot contain feature, so there is no need to remove it.
				 */
				// Using toList to ensure this is calculated only once
				val nodesToAdd = invertedOrImplications.get(feature).toList
				result.forEach [ cycleFeature |
					val cycleEntriesForLoopedFeature = internalCycleEntries.get(cycleFeature)
					cycleEntriesForLoopedFeature += nodesToAdd
					cycleEntriesForLoopedFeature -= comingFrom
				]
				System.err.println("Entered the second branch")
			}

			result
		} else {
			emptySet
		}
	}
}
