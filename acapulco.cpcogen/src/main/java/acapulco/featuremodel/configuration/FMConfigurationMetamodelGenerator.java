package acapulco.featuremodel.configuration;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import emf.utils.EMFUtils;
import emf.utils.EcoreMetamodelGenerator;
import acapulco.featuremodel.FeatureModelHelper;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;

/**
 * Generate the metamodel for the "configurations of the feature model" from the specified feature model.
 * 
 *
 */
public class FMConfigurationMetamodelGenerator {
	private FeatureModel featureModel;
	private FeatureModelHelper fmHelper;
	private EcoreMetamodelGenerator eMMGen;
	
	private Map<Feature, EClass> eClasses;
	private Map<Feature, EClass> eClassesAlternatives;
	
	public FMConfigurationMetamodelGenerator(FeatureModel fm, String name, String prefix, String uri) {
		this.featureModel = fm;
		this.fmHelper = new FeatureModelHelper(this.featureModel);
		this.eMMGen = new EcoreMetamodelGenerator(name, prefix, uri);
		
		this.eClasses = new HashMap<Feature, EClass>();
		this.eClassesAlternatives = new HashMap<Feature, EClass>();
	}
	
	public void generateMetamodel() {	
		Feature root = this.fmHelper.getRootFeature();
		addFeature(root);
	}
	
	public void saveMetamodel(String filepath) {
		EMFUtils.saveMetamodel(eMMGen.getContents(), filepath);
	}
	
	public EObject getMetamodel() {
		return eMMGen.getContents();
	}
	
	private void addFeature(Feature feature) {
		// Add the feature
		EClass eClass = eMMGen.createEClass(feature.getName());
		eMMGen.addEClassToEPackage(eClass);	// only for the root feature
		this.eClasses.put(feature, eClass);
		
		Feature parent = feature.getParentFeature();
		if (parent != null) {
			EClass parentEClass = this.eClasses.get(parent);
			
			if (fmHelper.isGroup(parent)) {
				EClass alternativeAbstractClass = createGroupFeatureAlternativeClass(parent);
				eMMGen.addEClassToEPackage(alternativeAbstractClass);
				eClass.getESuperTypes().add(alternativeAbstractClass);
				
				// Add the reference to the abstract class only once	
				if (!parentEClass.getEReferences().stream().anyMatch(ref -> ref.getEType() == alternativeAbstractClass)) {
					// Add reference
					EReference eRef = eMMGen.createEReference(alternativeAbstractClass.getName(), alternativeAbstractClass, 0, EStructuralFeature.UNBOUNDED_MULTIPLICITY, true);
					eMMGen.addEReferenceToEClass(eRef, parentEClass);
				}
					
			} else {
				// Add attribute 'selected'
				EAttribute selectedAttr = eMMGen.createEAttribute("selected", "EBoolean");
				eMMGen.addEAttributeToEClass(selectedAttr, eClass);
				
				// Add reference
				int lowerBound = feature.isOptional() ? 0 : 1;
				int upperBound = 1;
				EReference eRef = eMMGen.createEReference(feature.getName().toLowerCase(), eClass, lowerBound, upperBound, true);
				eMMGen.addEReferenceToEClass(eRef, parentEClass);
			}
		} else { // is the root feature
			// Add attribute 'selected'
			EAttribute selectedAttr = eMMGen.createEAttribute("selected", "EBoolean");
			eMMGen.addEAttributeToEClass(selectedAttr, eClass);
		}
		
		// Add children
		for (Feature f : feature.getOwnedFeatures()) {
			addFeature(f);
		}
	}
	
	private EClass createGroupFeatureAlternativeClass(Feature groupFeature) {
		if (this.eClassesAlternatives.containsKey(groupFeature)) {
			return this.eClassesAlternatives.get(groupFeature);
		}
		// Create alternative class
		EClass alternativeClass = eMMGen.createEClass(groupFeature.getName() + "Alternative");
		alternativeClass.setAbstract(true);
		
		// Add attribute 'selected'
		EAttribute selectedAttr = eMMGen.createEAttribute("selected", "EBoolean");
		eMMGen.addEAttributeToEClass(selectedAttr, alternativeClass);
		
		this.eClassesAlternatives.put(groupFeature, alternativeClass);
		
		return alternativeClass;	
	}

	public Map<Feature, EClass> geteClasses() {
		return eClasses;
	}

	public Map<Feature, EClass> geteClassesAlternatives() {
		return eClassesAlternatives;
	}

}
