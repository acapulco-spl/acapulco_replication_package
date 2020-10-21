package acapulco.featuremodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import acapulco.model.CrossTreeConstraint;
import acapulco.model.CrossTreeConstraintType;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.model.GroupFeature;

/**
 * 
 * @author anonym
 *
 */
public class FeatureModelHelper {
	private FeatureModel fm;
	private List<Feature> features;
	private List<Feature> alwaysActiveFeatures;
	
	// Cache of constraints
	private Map<Feature, List<CrossTreeConstraint>> CTCs;
	private Map<Feature, List<CrossTreeConstraint>> incomingCTCs;

	
	public FeatureModelHelper(FeatureModel fm) {
		this.fm = fm;
		this.features = null;
		this.alwaysActiveFeatures = null;
		
		initializeCTCs();
	}
	
	private void initializeCTCs() {
		this.CTCs = new HashMap<Feature, List<CrossTreeConstraint>>();
		this.incomingCTCs = new HashMap<Feature, List<CrossTreeConstraint>>();
		
		for (Feature f : getFeatures()) {
			CTCs.put(f, getCrossTreeConstraints(f));
			incomingCTCs.put(f, getIncomingCrossTreeConstraints(f));
		}
	}

	public FeatureModel getFeatureModel() {
		return this.fm;
	}
	
	public List<Feature> getFeatures() {
		if (this.features != null) {
			return this.features;
		}
		LinkedList<Feature> features = new LinkedList<Feature>();
		
		Feature root = this.fm.getOwnedRoot();
		features.add(root);
		
		List<Feature> children = new ArrayList<Feature>();
		children.addAll(root.getOwnedFeatures());
		while (!children.isEmpty()) {
			List<Feature> newChildren = new ArrayList<Feature>();
			for (Feature f : children) {
				features.add(f);
				newChildren.addAll(f.getOwnedFeatures());
			}
			children = newChildren;
		}
		this.features = features;
		return features;
	}
	
//	public List<Feature> getFeaturesRec() {
//		LinkedList<Feature> features = new LinkedList<Feature>();
//		
//		Feature root = this.fm.getOwnedRoot();
//		features.add(root);
//		features.addAll(getChildFeaturesRec(root));
//		
//		return features;
//	}
//	
//	private List<Feature> getChildFeaturesRec(Feature f) {
//		LinkedList<Feature> features = new LinkedList<Feature>();
//		for (Feature child : f.getOwnedFeatures()) {
//			features.add(child);
//			features.addAll(getChildFeaturesRec(child));
//		}
//		return features;
//	}
	
	public List<Feature> getAlwaysActiveFeatures() {
		if (this.alwaysActiveFeatures != null) {
			return this.alwaysActiveFeatures;
		}
		LinkedList<Feature> activeFeatures = new LinkedList<Feature>();
		
		// The root is always active
		Feature root = this.fm.getOwnedRoot();
		activeFeatures.add(root);
		
		// Mandatory children of the root feature
		List<Feature> mandatoryFeatures = root.getOwnedFeatures().stream().filter(f -> !f.isOptional()).collect(Collectors.toList());
		while (!mandatoryFeatures.isEmpty()) {
			ArrayList<Feature> newMandatoryFeatures = new ArrayList<Feature>();
			for (Feature f : mandatoryFeatures) {
				activeFeatures.add(f);
				newMandatoryFeatures.addAll(f.getOwnedFeatures().stream().filter(child -> !child.isOptional()).collect(Collectors.toList()));
			}
			mandatoryFeatures = newMandatoryFeatures;
		}
		
		this.alwaysActiveFeatures = activeFeatures;
		return activeFeatures;
	}

	public boolean isAlwaysActive(Feature f) {
		return this.getAlwaysActiveFeatures().contains(f);
	}
	
	public List<Feature> getAlwaysActiveGroupFeatures() {
		return getAlwaysActiveFeatures().stream().filter(f -> isGroup(f)).collect(Collectors.toList());
	}
	
	public Feature getRootFeature() {
		return this.fm.getOwnedRoot();
	}
	
	public boolean isRoot(Feature f) {
		return f.getParentFeature() == null;
	}
	
	public boolean isGroup(Feature f) {
		return f instanceof GroupFeature;
	}
	
	/**
	 * Check if this feature is an OR feature 1..*
	 */
	public boolean isORGroup(Feature f) {
		if (f instanceof GroupFeature) {
			GroupFeature gf = (GroupFeature) f;
			int lowerBound = gf.getChildMinCardinality();
			int upperBound = gf.getChildMaxCardinality();
			return lowerBound == 1 && (upperBound == -1 || upperBound > lowerBound);
		}
		return false;
	}
	
	/**
	 * Check if this feature is an XOR Feature 1..1
	 */
	public boolean isXORGroup(Feature f) {
		if (f instanceof GroupFeature) {
			GroupFeature gf = (GroupFeature) f;
			int lowerBound = gf.getChildMinCardinality();
			int upperBound = gf.getChildMaxCardinality();
			return lowerBound == upperBound && lowerBound == 1;
		}
		return false;
	}
	
	
	public boolean isLeaf(Feature f) {
		return f.getOwnedFeatures().isEmpty();
	}
	
	public List<Feature> getMandatoryChildren(Feature f) {
		return f.getOwnedFeatures().stream().filter(c -> !c.isOptional()).collect(Collectors.toList());
	}
	
	/**
	 * Get the cross tree constraints for a given feature f (f -> X)
	 */	
	public List<CrossTreeConstraint> getCrossTreeConstraints(Feature f) {
		return fm.getCrossTreeConstraints().stream().filter(c -> c.getLeftFeature().equals(f)).collect(Collectors.toList());
	}
	
	/**
	 * Get the features that are dependent on the given feature f (X -> f).
	 */
	public List<CrossTreeConstraint> getIncomingCrossTreeConstraints(Feature f) {
		return fm.getCrossTreeConstraints().stream().filter(c -> c.getRightFeature().equals(f)).collect(Collectors.toList());
	}
	
	/**
	 * Get the REQUIRES cross tree constraints for a given feature f (f REQUIRES X).
	 */	
	public List<CrossTreeConstraint> getREQUIRESCrossTreeConstraints(Feature f) {
		if (!CTCs.containsKey(f)) {
			return List.of();
		}
		return CTCs.get(f).stream().filter(c -> c.getType() == CrossTreeConstraintType.REQUIRES).collect(Collectors.toList());
	}
	
	/**
	 * Get the EXCLUDES cross tree constraints for a given feature f (f EXCLUDES X).
	 */	
	public List<CrossTreeConstraint> getEXCLUDESCrossTreeConstraints(Feature f) {
		if (!CTCs.containsKey(f)) {
			return List.of();
		}
		return CTCs.get(f).stream().filter(c -> c.getType() == CrossTreeConstraintType.EXCLUDES).collect(Collectors.toList());
	}
	
	/**
	 * Get the REQUIRES incoming cross tree constraints for a given feature f (X REQUIRES f).
	 */	
	public List<CrossTreeConstraint> getREQUIRESIncomingCrossTreeConstraints(Feature f) {
		if (!incomingCTCs.containsKey(f)) {
			return List.of();
		}
		return incomingCTCs.get(f).stream().filter(c -> c.getType() == CrossTreeConstraintType.REQUIRES).collect(Collectors.toList());
	}
	
	/**
	 * Get the EXCLUDES incoming cross tree constraints for a given feature f (X EXCLUDES f).
	 */	
	public List<CrossTreeConstraint> getEXCLUDESIncomingCrossTreeConstraints(Feature f) {
		if (!incomingCTCs.containsKey(f)) {
			return List.of();
		}
		return incomingCTCs.get(f).stream().filter(c -> c.getType() == CrossTreeConstraintType.EXCLUDES).collect(Collectors.toList());
	}
	
	/**
	 * Features f' that need to be activate due to the activation of feature f, because of a cross-tree constraint.
	 * (i.e., REQUIRES constraints: f -> f')
	 * 
	 * @param f
	 * @return
	 */
	public Set<Feature> getActivableCTCFeaturesForActivateF(Feature f) {
		return getREQUIRESCrossTreeConstraints(f).stream().map(c -> c.getRightFeature()).collect(Collectors.toSet());
	}
	
	/**
	 * True if activating the feature f involves activating another feature f' due to cross-tree constraints.
	 * (i.e., REQUIRES constraints: f -> f')
	 */
	public boolean hasActivableCTCForActivateF(Feature f) {
		return !getActivableCTCFeaturesForActivateF(f).isEmpty();
	}
	
	/**
	 * Features f' that need to be deactivate due to the activation of feature f, because of a cross-tree constraint.
	 * (i.e., EXCLUDES constraints: f EXCL f' or f' EXCL f)
	 * 
	 * @param f
	 * @return
	 */
	public Set<Feature> getDeactivableCTCFeaturesForActivateF(Feature f) {
		Set<Feature> featuresRight = getEXCLUDESCrossTreeConstraints(f).stream().map(c -> c.getRightFeature()).collect(Collectors.toSet());
		Set<Feature> featuresLeft = getEXCLUDESIncomingCrossTreeConstraints(f).stream().map(c -> c.getLeftFeature()).collect(Collectors.toSet());
		featuresRight.addAll(featuresLeft);
		return featuresRight;
	}
	
	/**
	 * True if activating the feature f involves deactivating another feature f' due to cross-tree constraints.
	 * (i.e., EXCLUDES constraints: f EXCL f' or f' EXCL f)
	 */
	public boolean hasDeactivableCTCForActivateF(Feature f) {
		return !getDeactivableCTCFeaturesForActivateF(f).isEmpty();
	}
	
	/**
	 * Features f' that need to be deactivate due to the deactivation of feature f, because of a cross-tree constraint.
	 * (i.e., REQUIRES constraints: f' -> f)
	 * 
	 * @param f
	 * @return
	 */
	public Set<Feature> getDeactivableCTCFeaturesForDeactivateF(Feature f) {
		return getREQUIRESIncomingCrossTreeConstraints(f).stream().map(c -> c.getLeftFeature()).collect(Collectors.toSet());
	}
	
	/**
	 * True if deactivating the feature f involves to deactivate another feature f' due to cross-tree constraints.
	 * (i.e., REQUIRES constraints: f' -> f)
	 */
	public boolean hasDeactivableCTCForDeactivateF(Feature f) {
		return !getDeactivableCTCFeaturesForDeactivateF(f).isEmpty();
	}
	
	public Feature getFeatureByName(String featureName) {
		Optional<Feature> o = this.getFeatures().stream().filter(f -> f.getName().equalsIgnoreCase(featureName)).findAny();
		if (o.isPresent()) {
			return o.get();
		} else {
			return null;
		}
	}
	
	public boolean isLeafOrOptionalParent(Feature c) {
		//return isLeaf(c) || (!isGroup(c) && c.getOwnedFeatures().stream().allMatch(child -> child.isOptional()));
		return isLeaf(c);
	}
}
