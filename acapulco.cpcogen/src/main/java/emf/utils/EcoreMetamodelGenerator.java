package emf.utils;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * Helper class to create an ecore metamodel.
 * 
 *
 */
public class EcoreMetamodelGenerator {
	private EcoreFactory ecoreFactory;
	private EcorePackage ecorePackage;
	private EPackage ePackage;
	
	/**
	 * Instantiate EcoreFactory and EPackage, providing unique URI to identify the package.
	 * 
	 * @param name		Metamodel's name.
	 * @param prefix	NS prefix.
	 * @param uri		NS URI to uniquely identify the package.
	 */
	public EcoreMetamodelGenerator(String name, String prefix, String uri) {
		// Instantiate EcoreFactory
		this.ecoreFactory = EcoreFactory.eINSTANCE;
		
		// Instantiate EcorePackage
		this.ecorePackage = EcorePackage.eINSTANCE;
		
		// Instantiate EPackage and provide unique URI to identify this package
		this.ePackage = ecoreFactory.createEPackage();
		this.ePackage.setName(name);
		this.ePackage.setNsPrefix(prefix);
		this.ePackage.setNsURI(uri);
	}
	
	/**
	 * Create an EClass to model the specified class.
	 * 
	 * @param name	Class' name.
	 * @return		EClass.
	 */
	public EClass createEClass(String name) {
		EClass eClass = this.ecoreFactory.createEClass();
		eClass.setName(name);
		return eClass;
	}

	/**
	 * Create an EAttribute.
	 * 
	 * @param name		Attribute's name.
	 * @param type		Attribute's type (e.g., EBoolean, EString,...).
	 * @return			EAttribute.
	 */
	public EAttribute createEAttribute(String name, String type) {
		EAttribute eAttr = this.ecoreFactory.createEAttribute();
		eAttr.setName(name);
		eAttr.setEType(this.ecorePackage.getEClassifier(type));
		
		return eAttr;
	}
	
	/**
	 * Create an EReference.
	 * 
	 * @param name			Reference's name.
	 * @param eClass		Type of the reference.
	 * @param lowerBound	Lower bound.
	 * @param upperBound	Upper bound (e.g., -1 is equivalent to '*' and to EStructuralFeature.UNBOUNDED_MULTIPLICITY).
	 * @param containment	The reference is a containment.
	 * @return				EReference.
	 */
	public EReference createEReference(String name, EClass eClass, int lowerBound, int upperBound, boolean containment) {
		EReference eRef = this.ecoreFactory.createEReference();
		eRef.setName(name);
		eRef.setEType(eClass);
		eRef.setLowerBound(lowerBound);
		eRef.setUpperBound(upperBound);
		eRef.setContainment(containment);
		
		return eRef;
	}
	
	public EPackage getContents() {
		return this.ePackage;
	}
	
	public void addEAttributeToEClass(EAttribute eAttr, EClass eClass) {
		eClass.getEStructuralFeatures().add(eAttr);
	}
	
	public void addEReferenceToEClass(EReference eRef, EClass eClass) {
		eClass.getEStructuralFeatures().add(eRef);
	}
	
	public void addEClassToEPackage(EClass eClass) {
		ePackage.getEClassifiers().add(eClass);
	}
	
//	public void writeMetamodel() {
//		ResourceSet metaResourceSet = new ResourceSetImpl();
//		 
//		// Register XML Factory implementation to handle .ecore files
//		metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMLResourceFactoryImpl());
//		
//		//Create empty resource with the given URI
//		Resource metaResource = metaResourceSet.createResource(URI.createURI("./" + ePackage.getName() + ".ecore"));
//		 
//		// Add the Package to contents list of the resource 
//		metaResource.getContents().add(ePackage);
//		 
//		try {
//			// Save the resource
//			metaResource.save(null);
//	    } catch (IOException e) {
//	    	e.printStackTrace();
//	   }
//	}	
	
}
