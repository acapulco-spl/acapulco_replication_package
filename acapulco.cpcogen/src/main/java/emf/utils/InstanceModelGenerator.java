package emf.utils;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * Helper class to create an ecore metamodel.
 * 
 *
 */
public class InstanceModelGenerator {
	private EPackage metaPackage;
	private EFactory metafactory;
	
	public InstanceModelGenerator(String metamodelFilepath) {
		this((EPackage) EMFUtils.loadMetamodel(metamodelFilepath));
	}
	
	public EPackage getPackage() {
		return metaPackage;
	}
	
	public InstanceModelGenerator(EPackage metamodel) {
		this.metaPackage = metamodel;
		this.metafactory =  this.metaPackage.getEFactoryInstance();
	}
	
	public EClass getEClass(String name) {
		return (EClass) metaPackage.getEClassifier(name);
	}
	
	public EObject createEClassInstance(EClass eClass) {
		return metafactory.create(eClass);
	}
	
	public EAttribute getEAttribute(String name, EClass eClass) {
		return (EAttribute) eClass.getEStructuralFeature(name);
	}
	
	public EReference getEReference(String name, EClass eClass) {
		return (EReference) eClass.getEStructuralFeature(name);
	}
}
