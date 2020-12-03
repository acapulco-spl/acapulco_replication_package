package acapulco.rulesgeneration;

import java.util.HashSet;
import java.util.Set;

import acapulco.rulesgeneration.activationdiagrams.FeatureDecision;

public class FMHenshinNode {
	private FeatureDecision featureDecision;
	private Set<Set<FeatureDecision>> pcs;
	
	public FMHenshinNode(FeatureDecision featureDecision) {
		super();
		this.featureDecision = featureDecision;
		this.pcs = new HashSet<Set<FeatureDecision>>();
	}

	public FeatureDecision getFeatureDecision() {
		return featureDecision;
	}

	public Set<Set<FeatureDecision>> getPCs() {
		return pcs;
	}
	
	public int hashCode() {
		return featureDecision.hashCode();
	}
	
	public boolean equals(Object other) {
		if (other instanceof FMHenshinNode) {
			return (((FMHenshinNode) other).getFeatureDecision().equals(featureDecision));
		}
		return false;
	}

	@Override
	public String toString() {
		return featureDecision + pcs.toString();
	} 
}
