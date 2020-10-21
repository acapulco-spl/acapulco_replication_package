package acapulco.rulesgeneration.activationdiagrams.principles

import java.util.HashSet
import java.util.Set
import acapulco.featuremodel.FeatureModelHelper
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.model.Feature

interface ApplicationPrinciple {
	/**
	 * Result is an OR of ANDs
	 */
	def Set<Set<FeatureDecision>> applyTo(FeatureDecision fd, FeatureModelHelper fmHelper)
	
	// Helper functions
	
	/**
	 * Produce an OR of the given feature decisions
	 */
	static def Set<Set<FeatureDecision>> oneOf (Iterable<FeatureDecision> fds) {
		new HashSet<Set<FeatureDecision>>( fds.map[#{it}].toSet )
	}
	
	/**
	 * Add an OR of the given feature decisions to the existing set of consequences
	 */
	static def Set<Set<FeatureDecision>> or (Set<Set<FeatureDecision>> existingConsequences, FeatureDecision fd) {
		(existingConsequences + #{#{fd}}).toSet
	}

	/**
	 * Produce an AND of the given feature decisions
	 */
	static def Set<Set<FeatureDecision>> allOf (Iterable<FeatureDecision> fds) {
		#{ fds.toSet }
	}
	
	/**
	 * Produce a single consequence
	 */
	static def Set<Set<FeatureDecision>> only (FeatureDecision fd) {
		#{#{fd}}
	}
	
	static def FeatureDecision activate (Feature f) { new FeatureDecision(f, true) }
	static def FeatureDecision deactivate (Feature f) { new FeatureDecision(f, false) }
}