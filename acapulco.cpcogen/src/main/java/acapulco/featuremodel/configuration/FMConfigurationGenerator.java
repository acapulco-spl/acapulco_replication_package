//package acapulco.featuremodel.configuration;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.ecore.EAttribute;
//import org.eclipse.emf.ecore.EClass;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.ecore.EPackage;
//import org.eclipse.emf.ecore.EReference;
//import org.sat4j.specs.TimeoutException;
//
//import com.google.common.io.Files;
//
//import emf.utils.EMFUtils;
//import emf.utils.FMPropositionalFormula;
//import emf.utils.InstanceModelGenerator;
//import mdeo4efm.featuremodel.FeatureModelHelper;
//import mdeo4efm.rulesgeneration.EvolversGenerator;
//import mdeo4efm.rulesgeneration.FeatureInformation;
//import mdeo4efm.rulesgeneration.principles.AnalysisTreeNode;
//import mdeo4efm.rulesgeneration.principles.FeatureActivationDiagram;
//import mdeo4efm.rulesgeneration.principles.SelectionSolution;
//import mdeoptimiser4efm.Feature;
//import mdeoptimiser4efm.FeatureModel;
//import mdeoptimiser4efm.featureide.utils.FeatureIDEUtils;
//
//public class FMConfigurationGenerator {
//	private InstanceModelGenerator modelGen;
//	private FeatureModel fm;
//	private EObject instanceModel;
//	private EvolversGenerator evolversGen;
//	
//	public FMConfigurationGenerator(String fmConfigMetamodelFilepath, FeatureModel fm, EvolversGenerator evolversGen) {
//		this.modelGen = new InstanceModelGenerator(fmConfigMetamodelFilepath);
//		this.fm = fm;
//		this.evolversGen = evolversGen;
//	}
//	
//	public FMConfigurationGenerator(EPackage fmConfigMetamodel, FeatureModel fm, EvolversGenerator evolversGen) {
//		this.modelGen = new InstanceModelGenerator(fmConfigMetamodel);
//		this.fm = fm;
//		this.evolversGen = evolversGen;
//	}
//	
//	public void generateInitialConfiguration(File fmFeatureIDE) {
//		this.instanceModel = createInstance(fm, fmFeatureIDE);
//	} 
//	
//	public void generateInitialConfigurationFromFile(File initialInput) {
//		this.instanceModel = createInstanceFromFile(fm, initialInput);
//	} 
//	
//	private EObject createInstance(FeatureModel fm, File fmFeatureIDE) {
//		FeatureModelHelper fmHelper = new FeatureModelHelper(fm);
//		Map<Feature, EObject> featureObjects = new HashMap<Feature, EObject>();
//		
//		
//		Feature root = fmHelper.getRootFeature();
//		EObject rootInstance = this.createInstance(root);
//		featureObjects.put(root, rootInstance);
//		
//		// Feature(childs) -> parent instance
//		Map<Feature, EObject> children = new HashMap<Feature, EObject>();
//		for (Feature child : root.getOwnedFeatures()) {
//			children.put(child, rootInstance);
//		}
//		
//		while (!children.isEmpty()) {
//			Map<Feature, EObject> newChildren = new HashMap<Feature, EObject>();
//	
//			// Randomize for in case of groupfeature that are always active, we choose the first child to activate.
//			List<Feature> keys = new ArrayList<Feature>(children.keySet());
//			Collections.shuffle(keys);
//			for (Feature child : keys) {
//				Feature parent = child.getParentFeature();
//				EClass parentClass = modelGen.getEClass(parent.getName());
//				EObject parentInstance = children.get(child);
//				
//				// In case of groupfeature that are always active, we choose the first child to activate.
//				//boolean selected = fmHelper.getAlwaysActiveGroupFeatures().contains(parent) && !parentAlwaysActiveInitialized.contains(parent);
//				EObject childInstance = this.createInstance(child);
//				featureObjects.put(child, childInstance);
//				
//				EReference ref = modelGen.getEReference(child.getName().toLowerCase(), parentClass);
//				if (ref == null) {
//					ref = modelGen.getEReference(parent.getName()+"Alternative", parentClass);
//					((EList)parentInstance.eGet(ref)).add(childInstance);
//				} else {
//					parentInstance.eSet(ref, childInstance);
//				}
//				
//				for (Feature c : child.getOwnedFeatures()) {
//					newChildren.put(c, childInstance);
//				}
//			}
//			children = newChildren;
//		}
//		
//		
//		// Initialize selected attributes
//		//initializeSelectedAttributes(featureObjects, this.fm);
//		//initializeSelectedAttribute(featureObjects, fmHelper);
//		activateFeaturesInitialConfig(featureObjects, fmFeatureIDE);
//		
//		return rootInstance;
//	}
//	
//	private EObject createInstanceFromFile(FeatureModel fm, File initialInput) {
//		FeatureModelHelper fmHelper = new FeatureModelHelper(fm);
//		Map<Feature, EObject> featureObjects = new HashMap<Feature, EObject>();
//		
//		
//		Feature root = fmHelper.getRootFeature();
//		EObject rootInstance = this.createInstance(root);
//		featureObjects.put(root, rootInstance);
//		
//		// Feature(childs) -> parent instance
//		Map<Feature, EObject> children = new HashMap<Feature, EObject>();
//		for (Feature child : root.getOwnedFeatures()) {
//			children.put(child, rootInstance);
//		}
//		
//		while (!children.isEmpty()) {
//			Map<Feature, EObject> newChildren = new HashMap<Feature, EObject>();
//	
//			// Randomize for in case of groupfeature that are always active, we choose the first child to activate.
//			List<Feature> keys = new ArrayList<Feature>(children.keySet());
//			Collections.shuffle(keys);
//			for (Feature child : keys) {
//				Feature parent = child.getParentFeature();
//				EClass parentClass = modelGen.getEClass(parent.getName());
//				EObject parentInstance = children.get(child);
//				
//				// In case of groupfeature that are always active, we choose the first child to activate.
//				//boolean selected = fmHelper.getAlwaysActiveGroupFeatures().contains(parent) && !parentAlwaysActiveInitialized.contains(parent);
//				EObject childInstance = this.createInstance(child);
//				featureObjects.put(child, childInstance);
//				
//				EReference ref = modelGen.getEReference(child.getName().toLowerCase(), parentClass);
//				if (ref == null) {
//					ref = modelGen.getEReference(parent.getName()+"Alternative", parentClass);
//					((EList)parentInstance.eGet(ref)).add(childInstance);
//				} else {
//					parentInstance.eSet(ref, childInstance);
//				}
//				
//				for (Feature c : child.getOwnedFeatures()) {
//					newChildren.put(c, childInstance);
//				}
//			}
//			children = newChildren;
//		}
//		
//		
//		// Initialize selected attributes
//		//initializeSelectedAttributes(featureObjects, this.fm);
//		//initializeSelectedAttribute(featureObjects, fmHelper);
//		activateFeaturesInitialConfigFromFile(featureObjects, initialInput);
//		
//		return rootInstance;
//	}
//	
//	private void activateFeaturesInitialConfigFromFile(Map<Feature, EObject> featureObjects, File initialInput) {
//		FeatureModelHelper fmHelper = new FeatureModelHelper(this.fm);
//		try {
//			List<String> features = Files.readLines(initialInput, Charset.defaultCharset());
//			for (String fName : features) {
//				Feature f = fmHelper.getFeatureByName(fName);
//				if (f != null) {
//					EObject object = featureObjects.get(f);
//					setSelectedAttributed(f, object, true);	
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void activateFeaturesInitialConfig(Map<Feature, EObject> featureObjects, File fmFeatureIDE) {
//		FeatureModelHelper fmHelper = new FeatureModelHelper(this.fm);
//		try {
//			List<String> featuresSelected = FeatureIDEUtils.getValidConfiguration(fmFeatureIDE);
//			for (String fName : featuresSelected) {
//				Feature f = fmHelper.getFeatureByName(fName);
//				EObject object = featureObjects.get(f);
//				setSelectedAttributed(f, object, true);
//			}
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		}
//	}
//	
////	private void initializeSelectedAttributes(Map<Feature, EObject> featureObjects, FeatureModel fm) {
////		FMPropositionalFormula fmpf = new FMPropositionalFormula(fm);
////		String formula = fmpf.encodeFM();
////		System.out.println(formula);
////		System.out.println(SatSolver.checkSatisfiable(formula));
////		System.out.println(SatSolver.getAllSolutions(formula));
////	}
//
//	
//	private EObject createInstance(Feature feature) {
//		EClass featureClass = this.modelGen.getEClass(feature.getName());
//		EObject featureInstance = this.modelGen.createEClassInstance(featureClass);
//		
//		return featureInstance;
//	}
//	
//	private void setSelectedAttributed(Feature feature, EObject featureInstance, boolean selected) {
//		EClass featureClass = this.modelGen.getEClass(feature.getName());
//		EAttribute selectedAttr = this.modelGen.getEAttribute("selected", featureClass);
//		featureInstance.eSet(selectedAttr, selected);
//	}
//	
//	public void saveModel(String filepath) {
//		EMFUtils.saveModel(this.instanceModel, filepath);
//	}
//}
