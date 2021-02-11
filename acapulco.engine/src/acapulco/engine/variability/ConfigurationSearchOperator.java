package acapulco.engine.variability;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;

/**
 * A set of feature decisions to be affected in a configuration expressed as a
 * set of classes in the configuration metamodel and information about whether
 * to activate or deactivate them.
 * 
 * @author k1074611
 */
public class ConfigurationSearchOperator {
	private Map<EClass, Boolean> featureDecisions = new HashMap<>(); 
	private EClass root;
	private String name;
	

	public ConfigurationSearchOperator(EClass root) {
		this.root = root;
	}
	
	public ConfigurationSearchOperator(EClass root, boolean activateRoot) {
		this.root = root;
		featureDecisions.put(root, activateRoot);
	}
	
	public void addDecision(EClass feature, boolean activate) {
		featureDecisions.put(feature, activate);
	}
	
	public Set<EClass> getFeatures() {
		return featureDecisions.keySet();
	}
	
	public boolean isActivated(EClass feature) {
		return featureDecisions.get(feature);		
	}
	
	public boolean isRoot(EClass feature) {
		return root == feature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
