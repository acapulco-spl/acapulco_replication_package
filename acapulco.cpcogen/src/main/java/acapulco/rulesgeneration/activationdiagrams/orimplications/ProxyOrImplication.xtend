package acapulco.rulesgeneration.activationdiagrams.orimplications

import java.util.HashSet
import java.util.Map
import java.util.Set
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrFeature

class ProxyOrImplication extends OrImplication {
	
	val ActivationDiagramNode node
	var Set<OrImplication> resolvedOrImplications = null

	new(ActivationDiagramNode node) {
		this.node = node
	}
	
	override resolve(Map<ActivationDiagramNode, Set<OrImplication>> orImplications) {
		if (resolvedOrImplications === null) {
			resolvedOrImplications = new HashSet<OrImplication>(orImplications.get(node))
		} else {
			val implicationsToResolve = resolvedOrImplications.filter[needsResolving]
			if (!implicationsToResolve.empty) {
				implicationsToResolve.forEach[resolve(orImplications)]
				resolvedOrImplications = new HashSet<OrImplication>(resolvedOrImplications.flatMap [ oi |
					if (oi instanceof ProxyOrImplication) {
						if (oi.resolvedOrImplications !== null) {
							oi.resolvedOrImplications
						} else {
							#{oi as OrImplication}
						}
					} else {
						#{oi}
					}
				].reject[oi | // Break cycles
					if (oi instanceof ProxyOrImplication) {
						oi.node === node
					} else {
						false
					}
				].toSet)
			}
		}
	}
	
	override needsResolving() {
		(resolvedOrImplications === null) || resolvedOrImplications.exists[needsResolving]
	}

	override Set<VBRuleOrFeature> resolvedImplication() { 
		if (needsResolving) {
			throw new IllegalStateException("Proxy Or implications must be resolved before they can be read off")
		}
		
		resolvedOrImplications.flatMap[oi |
			if (oi instanceof ProxyOrImplication) {
				oi.resolvedOrImplications.flatMap[resolvedImplication]
			} else {
				oi.resolvedImplication
			}
		].toSet
	}
}