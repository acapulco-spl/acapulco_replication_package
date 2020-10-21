//package acapulco.fitnessfunction;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.ecore.EAttribute;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.ecore.EPackage.Registry;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
//import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
//import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
//
//import mdeoptimiser4efm.CrossTreeConstraint;
//import mdeoptimiser4efm.CrossTreeConstraintType;
//import mdeoptimiser4efm.Feature;
//import mdeoptimiser4efm.FeatureModel;
//import mdeoptimiser4efm.GroupFeature;
//import mdeoptimiser4efm.MDEOptimiser4EFMPackage;
//import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction;
//import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution;
//
//public class FeatureModelConstraints implements IGuidanceFunction {
//	List<EObject> active;
//
//	static String FM_FILE_PATH = "models\\FM-FQAs.xmi";
//	
//	static boolean initialized = false;
//	static Map<String, List<String>> excludes = new HashMap<>();
//	static Map<String, List<String>> requires = new HashMap<>();
//	static Map<String, List<String>> mandatoryChildren = new HashMap<>();
//	static Map<String, List<String>> xorGroups = new HashMap<>();
//	static Map<String, List<String>> orGroups = new HashMap<>();
//
//	public static void init() {
//		MDEOptimiser4EFMPackage.eINSTANCE.eClass();
//
//		ResourceSet resourceSet = new ResourceSetImpl();
//
//		Registry packageRegistry = resourceSet.getPackageRegistry();
//		packageRegistry.put(MDEOptimiser4EFMPackage.eNS_URI, MDEOptimiser4EFMPackage.eINSTANCE);
//
//		Map<String, Object> extensionFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
//		extensionFactoryMap.put("xmi", new XMIResourceFactoryImpl());
//
//		XMIResourceImpl resource = new XMIResourceImpl();
//		File source = new File(FM_FILE_PATH);
//		try {
//			resource.load(new FileInputStream(source), new HashMap<Object, Object>());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		FeatureModel fm = (FeatureModel) resource.getContents().get(0);
//
//		initNavigate(fm.getOwnedRoot());
//		for (CrossTreeConstraint c : fm.getCrossTreeConstraints()) {
//			Map<String, List<String>> map = c.getType().equals(CrossTreeConstraintType.REQUIRES) ? requires : excludes;
//			List<String> list = map.get(c.getLeftFeature().getName());
//			if (list == null) {
//				list = new ArrayList<String>();
//				map.put(c.getLeftFeature().getName(), list);
//			}
//			list.add(c.getRightFeature().getName());
//		}
//
//		initialized = true;
//	}
//
//	private static void initNavigate(Feature feature) {
//		for (Feature f : feature.getOwnedFeatures()) {
//			if (!f.isOptional()) {
//				List<String> list = mandatoryChildren.get(feature.getName());
//				if (list == null) {
//					list = new ArrayList<String>();
//					mandatoryChildren.put(feature.getName(), list);
//				}
//				list.add(f.getName());
//			}
//
//			if (f instanceof GroupFeature) {
//				GroupFeature fGroup = (GroupFeature) f;
//				List<String> children = new ArrayList<>();
//				fGroup.getOwnedFeatures().forEach(ch -> children.add(ch.getName()));
//				if (fGroup.getChildMaxCardinality() > 1) {
//					orGroups.put(fGroup.getName(), children);
//				} else {
//					xorGroups.put(fGroup.getName(), children);
//				}
//			}
//		}
//		
//		for (Feature g : feature.getOwnedFeatures()) {
//			initNavigate(g);
//		}
//	}
//
//	@Override
//	public double computeFitness(Solution model) {
//		EObject root = model.getModel();
//
//		if (!initialized)
//			init();
//		
//		Map<String,EObject> name2object = new HashMap<>();
//		
//		boolean fulfilled = checkParentChild(root, name2object);
//		fulfilled &= checkMandatory(name2object);
//		fulfilled &= checkXor(name2object);
//		fulfilled &= checkOr(name2object);
//		fulfilled &= checkRequires(name2object);
//		fulfilled &= checkExcludes(name2object);
////		System.out.println(fulfilled);
//		return 0.0;
//	}
//
//	private boolean checkXor(Map<String, EObject> name2object) {
//		for (Entry<String, List<String>> entry : xorGroups.entrySet()) {
//			EObject parent = name2object.get(entry.getKey());
//			if (isSelected(parent)) {
//				int count = 0;
//				for (String childName : entry.getValue()) {
//					EObject child = name2object.get(childName);
//					if (isSelected(child))
//						count++;
//				}	
//				if (count != 1)
//					throw new RuntimeException ("Solution violates XOR constraint of: "+entry.getKey());
//			}
//		}
//		return true;
//	}
//
//	private boolean checkExcludes(Map<String, EObject> name2object) {
//		for (Entry<String, List<String>> entry : excludes.entrySet()) {
//			EObject left = name2object.get(entry.getKey());
//			if (isSelected(left)) {
//				for (String rightName : entry.getValue()) {
//					EObject right = name2object.get(rightName);
//					if (isSelected(right))
//						throw new RuntimeException ("Solution violates EXCLUDES constraint: "+entry.getKey()+ " -> "+rightName);
//				}	
//			}
//		}
//		return true;
//	}
//	private boolean checkRequires(Map<String, EObject> name2object) {
//		for (Entry<String, List<String>> entry : requires.entrySet()) {
//			EObject left = name2object.get(entry.getKey());
//			if (isSelected(left)) {
//				for (String rightName : entry.getValue()) {
//					EObject right = name2object.get(rightName);
//					if (!isSelected(right))
//						throw new RuntimeException ("Solution violates a REQUIRES constraint: "
//								+entry.getKey()+" -> "+rightName);
//				}	
//			}
//		}
//		return true;
//	}
//
//	private boolean checkOr(Map<String, EObject> name2object) {
//		for (Entry<String, List<String>> entry : orGroups.entrySet()) {
//			EObject parent = name2object.get(entry.getKey());
//			if (isSelected(parent)) {
//				int count = 0;
//				for (String childName : entry.getValue()) {
//					EObject child = name2object.get(childName);
//					if (isSelected(child))
//						count++;
//				}	
//				if (count == 0)
//					throw new RuntimeException ("Solution violates OR constraint: "+entry.getKey());
//			}
//		}
//		return true;
//	}
//	
//	private boolean checkMandatory(Map<String, EObject> name2object) {
//		for (Entry<String, List<String>> entry : mandatoryChildren.entrySet()) {
//			EObject parent = name2object.get(entry.getKey());
//			if (isSelected(parent)) {
//				for (String childName : entry.getValue()) {
//					EObject child = name2object.get(childName);
//					if (!isSelected(child))
//						throw new RuntimeException ("Solution violates MANDATORY constraint: "
//								+entry.getKey() + " -> " + childName
//								);
//				}	
//			}
//		}
//		return true;
//	}
//
//	private boolean checkParentChild(EObject parent, Map<String, EObject> name2object) {
//		name2object.put(getName(parent), parent);
//		
//		boolean result = true;
//		for (EObject child : getChildren(parent)) {
//			if (isSelected(child) && !isSelected(parent)) {
//				throw new RuntimeException ("Solution violates a PARENT_CHILD: "
//						+ getName(parent) + " -> "+ getName(child) 
//						);
//			}
//			result &= checkParentChild(child, name2object);
//		}
//		return result;
//	}
//
//	private String getName(EObject object) {
//		return object.eClass().getName();
//	}
//
//	private Boolean isSelected(EObject object) {
//		EAttribute attr = (EAttribute) object.eClass().getEStructuralFeature("selected");
//		return (Boolean) object.eGet(attr);
//	}
//
//	private EList<EObject> getChildren(EObject object) {
//		return object.eContents();
//	}
//	
//	@Override
//	public String getName() {
//		return "Feature Model Constraints";
//	}
//
//}
//
//
