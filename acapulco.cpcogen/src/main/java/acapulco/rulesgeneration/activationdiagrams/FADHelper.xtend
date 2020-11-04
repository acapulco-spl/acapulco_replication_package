package acapulco.rulesgeneration.activationdiagrams

class FADHelper {
	static dispatch def Iterable<FeatureDecision> collectFeatureDecisions(
		Iterable<ActivationDiagramNode> consequences) {
		consequences.flatMap[collectFeatureDecisions]
	}

	static dispatch def Iterable<FeatureDecision> collectFeatureDecisions(OrNode or) {
		or.consequences.collectFeatureDecisions
	}

	static dispatch def Iterable<FeatureDecision> collectFeatureDecisions(AndNode and) {
		and.consequences.collectFeatureDecisions
	}

	static dispatch def Iterable<FeatureDecision> collectFeatureDecisions(FeatureDecision fd) {
		#{fd}
	}
}