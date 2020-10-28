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
	 * Or-implications: Which ORs do we need to make a decision on when we have decided on a given root feature or OrAlternative feature.
	 * 
	 * This will be filled directly in the forward sweep, but may still contain unresolved proxies at that point. Proxies are then resolved using information from the followOr map.
	 */
	val orImplications = new HashMap<VBRuleFeature, Set<OrImplication>>

	/**
	 * Internal storage for keeping track of information collected at each node during the forward sweep: 
	 * this is the set of Or nodes directly reachable from the given node.
	 */
	val followOrs = new HashMap<ActivationDiagramNode, Set<OrImplication>>()

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

	var Map<FeatureDecision, Set<VBRuleFeature>> resolvedPCs

	def getPresenceConditions() {
		if (resolvedPCs === null) {
			resolvedPCs = new HashMap
			resolvedPCs.putAll(presenceConditions.mapValues[resolveAndSimplify])
		}
		
		resolvedPCs
	}

	def getOrImplications() {
		orImplications.mapValues[flatMap[resolvedImplication].toSet]
	}

	private def initialise() {
		// 1. Forward sweep the sub-diagram
		val rootImplications = rootDecision.visit(new FeaturePresenceCondition(vbRuleFeatures), rootDecision)
		orImplications.put(vbRuleFeatures, rootImplications)

		// 2. Resolve presence conditions -- We know this terminates because cycles are broken
		var proxyPresenceConditions = presenceConditions.filter[k, v|v.exists[needsResolving]]
		while (!proxyPresenceConditions.empty) {
			proxyPresenceConditions.forEach[k, v|v.forEach[resolve(presenceConditions)]]

			proxyPresenceConditions = presenceConditions.filter[k, v|v.exists[needsResolving]]
		}

		// 3. Resolve or-links -- We know this terminates because cycles are broken
		var proxyOrImplications = orImplications.filter[k, v|v.exists[needsResolving]]
		while (!proxyOrImplications.empty) {
			proxyOrImplications.forEach[k, v|v.forEach[resolve(followOrs)]]

			proxyOrImplications = orImplications.filter[k, v|v.exists[needsResolving]]
		}
	}

	private dispatch def Set<OrImplication> visit(OrNode or, PresenceCondition pc, FeatureDecision comingFrom) {
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
			val followOnOrs = c.visit(new FeaturePresenceCondition(orAlternativeFeature), comingFrom)

			// We put these directly into the Or implications as there is no node to proxy from for an Or alternative
			orImplications.put(orAlternativeFeature, followOnOrs)
		]

		return #{new FinalisedOrImplication(orFeature)}
	}

	private dispatch def Set<OrImplication> visit(AndNode and, PresenceCondition pc, FeatureDecision comingFrom) {
		if (subdiagramContents.contains(and)) {
			// Nothing to be done
			// But we must return the appropriate or-node information
			return #{new ProxyOrImplication(and)}
		}

		subdiagramContents.add(and)

		val followOrInformation = and.consequences.flatMap[visit(pc, comingFrom)].toSet
		followOrs.put(and, followOrInformation) // Store so we can use it in proxy resolution
		return followOrInformation
	}

	private dispatch def Set<OrImplication> visit(FeatureDecision fd, PresenceCondition pc,
		FeatureDecision comingFrom) {
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
		val followOrInformation = fd.consequences.flatMap[visit(presenceCondition, fd)].toSet

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

	private def resolveAndSimplify(Set<PresenceCondition> presenceConditions) {
		val tentativeResolvedCondition = presenceConditions.flatMap[resolvedCondition].toSet

		if (tentativeResolvedCondition.contains(vbRuleFeatures)) {
			// If the root feature is one of the conditions, everything else doesn't matter :-)
			#{vbRuleFeatures}
		} else {
			tentativeResolvedCondition
		}
	}
}
