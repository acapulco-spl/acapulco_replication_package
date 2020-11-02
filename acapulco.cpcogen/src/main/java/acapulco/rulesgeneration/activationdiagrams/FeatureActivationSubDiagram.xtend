package acapulco.rulesgeneration.activationdiagrams

import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.orimplications.FinalisedOrImplication
import acapulco.rulesgeneration.activationdiagrams.orimplications.OrImplication
import acapulco.rulesgeneration.activationdiagrams.orimplications.ProxyOrImplication
import acapulco.rulesgeneration.activationdiagrams.presenceconditions.FeaturePresenceCondition
import acapulco.rulesgeneration.activationdiagrams.presenceconditions.PresenceCondition
import acapulco.rulesgeneration.activationdiagrams.presenceconditions.ProxyPresenceCondition
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * A feature-activation sub-diagram is the subset of the nodes in a feature-activation diagrams required for a particular activation decision.
 * 
 * It contains all information required to generate the VB rule for this feature decision; see public getter methods.
 */
class FeatureActivationSubDiagram {

	@Accessors(PUBLIC_GETTER)
	val FeatureDecision rootDecision

	@Accessors(PACKAGE_GETTER)
	val Set<ActivationDiagramNode> subdiagramContents = new HashSet<ActivationDiagramNode>

	/**
	 * The VB-rule feature model's root feature, from which all other features can be found
	 */
	@Accessors(PUBLIC_GETTER)
	val VBRuleFeature vbRuleFeatures = new VBRuleFeature("root")

	/**
	 * Helper for quick lookups
	 */
	val orFeatureMap = new HashMap<OrNode, VBRuleOrFeature>

	/**
	 * For every feature touched by this sub-diagram, record the decisions contained in the sub-diagram. There are either 1 or 2 such decisions. If there are 2, then they are conflicting decisions. 
	 */
	val featureDecisions = new HashMap<Feature, Set<FeatureDecision>>

	/**
	 * Presence conditions; these may need resolving
	 */
	val presenceConditions = new HashMap<FeatureDecision, Set<PresenceCondition>>

	/**
	 * The final resolved PCs
	 */
	val Map<FeatureDecision, Set<VBRuleFeature>> resolvedPCs = new HashMap

	/**
	 * Or-implications: Which ORs do we need to make a decision on when we have decided on a given root feature or OrAlternative feature.
	 * 
	 * This will be filled directly in the forward sweep, but may still contain unresolved proxies at that point. Proxies are then resolved using information from the followOr map.
	 */
	val orImplications = new HashMap<VBRuleFeature, Set<OrImplication>>

	/**
	 * The final resolved or implications
	 */
	val resolvedOrImplications = new HashMap<VBRuleFeature, Set<VBRuleOrFeature>>

	/**
	 * Internal storage for keeping track of information collected at each node during the forward sweep: 
	 * this is the set of Or nodes directly reachable from the given node.
	 */
	val followOrs = new HashMap<ActivationDiagramNode, Set<OrImplication>>
	
	/**
	 * Internal map for collecting direct or-predecessors of any feature decisions so we can compute or-overlap
	 */
	val immediateOrPredecessors = new HashMap<FeatureDecision, List<Pair<VBRuleOrFeature, VBRuleOrAlternative>>>
	
	/**
	 * Information about overlaps between or-features. These are redundant paths through the rule
	 */
	@Accessors(PUBLIC_GETTER)
	val orOverlaps = new HashMap<Pair<VBRuleOrFeature, VBRuleOrFeature>, List<Pair<VBRuleOrAlternative, VBRuleOrAlternative>>>

	new(FeatureDecision decision) {
		this.rootDecision = decision

		initialise
	}

	def getFeatureDecisions() {
		subdiagramContents.filter(FeatureDecision)
	}

	/**
	 * Produce and return the minimal set of pairwise exclusions between VB-rule features.
	 */
	def getFeatureExclusions() {
		featureDecisions.values.filter[size === 2].flatMap [
			// Produce pairwise exclusions so we can reduce them to the minimal set necessary
			val leftDecisions = getPresenceConditions().get(get(0))
			val rightDecisions = getPresenceConditions().get(get(1))

			leftDecisions.flatMap [ ld |
				rightDecisions.map [ rd |
					/*
					 * Use the feature ID to order the pairs. Order doesn't matter logically (as these represent mutual exclusions,
					 * but ordering means that the toSet call later can more effectively filter out duplicate entries.
					 */
					if (ld.ID > rd.ID) {
						rd -> ld
					} else {
						ld -> rd
					}
				]
			]
		].toSet
	}

	def getPresenceConditions() {
		resolvedPCs
	}

	def getOrImplications() {
		resolvedOrImplications
	}

	private def initialise() {
		// 1. Forward sweep the sub-diagram
		val rootImplications = rootDecision.visit(new FeaturePresenceCondition(vbRuleFeatures), rootDecision, null)
		orImplications.put(vbRuleFeatures, rootImplications)

		// 2. Resolve presence conditions -- We know this terminates because cycles are broken
		presenceConditions.entrySet.map [ e |
			val resolvedPC = e.value.flatMap [
				val fds = new HashSet<FeatureDecision>(#{e.key})
				resolve(presenceConditions, fds)
			].toSet

			// Simplify, where 'root' is part of the PC
			e.key -> if (resolvedPC.contains(vbRuleFeatures)) {
				#{vbRuleFeatures}
			} else {
				resolvedPC
			}
		].forEach[resolvedPCs.put(key, value)]

		// 3. Resolve or-links -- We know this terminates because cycles are broken
		orImplications.entrySet.map [ e |
			e.key -> e.value.flatMap [
				val nodes = new HashSet<ActivationDiagramNode>()
				resolve(followOrs, nodes)
			].toSet
		].forEach[resolvedOrImplications.put(key, value)]
		
		// 4. Resolve or overlaps
		immediateOrPredecessors.values.reject[size < 2].forEach[list |
			list.forEach[or1, idx1|
				// Drop the first items so we only explore the diagonal
				list.drop(idx1 + 1).forEach[or2 |
					val id1 = or1.key.ID
					val id2 = or2.key.ID
					
					var Pair<VBRuleOrFeature, VBRuleOrFeature> key = null
					var Pair<VBRuleOrAlternative, VBRuleOrAlternative> value = null
					
					if (id1 < id2) {
						key = or1.key -> or2.key
						value = or1.value -> or2.value
					} else {
						key = or2.key -> or1.key
						value = or2.value -> or1.value
					}
					
					var registry = orOverlaps.get(key)
					if (registry === null) {
						registry = new ArrayList<Pair<VBRuleOrAlternative, VBRuleOrAlternative>>
						orOverlaps.put(key, registry)
					}
					registry += value
				]
			]
		]
	}

	private dispatch def Set<OrImplication> visit(OrNode or, PresenceCondition pc, FeatureDecision comingFrom, VBRuleOrFeature predecessorOrNode) {
		if (subdiagramContents.contains(or)) {
			// Nothing to be done: dependencies between presence conditions will be sorted out by adding appropriate cross-dependencies
			// But we must return this node so dependencies can be built
			return #{new FinalisedOrImplication(featureFor(or))}
		}

		subdiagramContents.add(or)

		// 1. Create features for this or-node and it's children
		val orFeature = or.createFeatures(comingFrom)

		// 2. Step down
		or.consequences.forEach [ c, idx |
			val orAlternativeFeature = orFeature.children.get(idx)
			val followOnOrs = c.visit(new FeaturePresenceCondition(orAlternativeFeature), comingFrom, orFeature)

			// We put these directly into the Or implications as there is no node to proxy from for an Or alternative
			orImplications.put(orAlternativeFeature, followOnOrs)
		]

		return #{new FinalisedOrImplication(orFeature)}
	}

	private dispatch def Set<OrImplication> visit(AndNode and, PresenceCondition pc, FeatureDecision comingFrom, VBRuleOrFeature predecessorOrNode) {
		if (subdiagramContents.contains(and)) {
			// Nothing to be done
			// But we must return the appropriate or-node information
			return #{new ProxyOrImplication(and)}
		}

		subdiagramContents.add(and)

		val followOrInformation = and.consequences.flatMap[visit(pc, comingFrom, null)].toSet
		followOrs.put(and, followOrInformation) // Store so we can use it in proxy resolution
		return followOrInformation
	}

	private dispatch def Set<OrImplication> visit(FeatureDecision fd, PresenceCondition pc,
		FeatureDecision comingFrom, VBRuleOrFeature predecessorOrNode) {
		// Record or-predecessor, if any
		if (predecessorOrNode !== null) {
			// This will work because that's the only situation where we would have a non-null predecessorNode
			val alternative = (pc as FeaturePresenceCondition).feature as VBRuleOrAlternative
			
			var registry = immediateOrPredecessors.get(fd)
			if (registry === null) {
				registry = new ArrayList<Pair<VBRuleOrFeature, VBRuleOrAlternative>>
				immediateOrPredecessors.put(fd, registry)
			}
			
			registry += predecessorOrNode -> alternative
		}

		var pcs = presenceConditions.get(fd)
		if (pcs === null) {
			pcs = new HashSet<PresenceCondition>
			presenceConditions.put(fd, pcs)
		}
		pcs.add(pc)

		if (subdiagramContents.contains(fd)) {
			return #{new ProxyOrImplication(fd)}
		}

		subdiagramContents.add(fd)

		// 1. Record feature decision for mutual exclusions
		var decisionsSoFar = featureDecisions.get(fd.feature)
		if (decisionsSoFar === null) {
			decisionsSoFar = new HashSet<FeatureDecision>
			featureDecisions.put(fd.feature, decisionsSoFar)
		}
		decisionsSoFar += fd

		// 2. step down
		val presenceCondition = new ProxyPresenceCondition(fd)
		val followOrInformation = fd.consequences.flatMap[visit(presenceCondition, fd, null)].toSet

		// 3. Return information about following Ors
		followOrs.put(fd, followOrInformation) // Store so we can use it in proxy resolution
		return followOrInformation
	}

	/**
	 * Create a fresh set of VB rule features for the given OrNode.
	 */
	private def VBRuleOrFeature createFeatures(OrNode or, FeatureDecision source) {
		val vbRuleOrFeature = new VBRuleOrFeature(source.name, or) => [
			val counter = new ArrayList<Integer>()
			counter += 0
			children += or.consequences.map [
				val ID = counter.head + 1
				counter.set(0, ID)
				new VBRuleOrAlternative(
					// TODO: This introduces a small lookahead, which might make performance a little less good
					'''«source.name»_Alternative«ID»_«it.name»''',
					or,
					ID
				)
			]
		]
		vbRuleFeatures.children += vbRuleOrFeature
		orFeatureMap.put(or, vbRuleOrFeature)

		vbRuleOrFeature
	}

	private def featureFor(OrNode or) {
		orFeatureMap.get(or)
	}

	private dispatch def String getName(ActivationDiagramNode node) {
		node.consequences.head.name
	}

	private dispatch def String getName(FeatureDecision fd) {
		fd.feature.name.sanitise + (fd.activate ? "Act" : "DeAct")
	}

	/**
	 * Sanitise feature names so they can serve as variable identifiers in the formula given to the SAT solver 
	 */
	private def sanitise(String featureName) {
		featureName.replaceAll("\\W", "")
	}
}
