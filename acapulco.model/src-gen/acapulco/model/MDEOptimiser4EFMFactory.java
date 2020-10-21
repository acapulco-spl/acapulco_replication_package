/**
 */
package acapulco.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see acapulco.model.MDEOptimiser4EFMPackage
 * @generated
 */
public interface MDEOptimiser4EFMFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MDEOptimiser4EFMFactory eINSTANCE = acapulco.model.impl.MDEOptimiser4EFMFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Feature Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Feature Model</em>'.
	 * @generated
	 */
	FeatureModel createFeatureModel();

	/**
	 * Returns a new object of class '<em>Feature</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Feature</em>'.
	 * @generated
	 */
	Feature createFeature();

	/**
	 * Returns a new object of class '<em>Quality Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Quality Attribute</em>'.
	 * @generated
	 */
	QualityAttribute createQualityAttribute();

	/**
	 * Returns a new object of class '<em>Quality Attribute Annotation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Quality Attribute Annotation</em>'.
	 * @generated
	 */
	QualityAttributeAnnotation createQualityAttributeAnnotation();

	/**
	 * Returns a new object of class '<em>Cross Tree Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cross Tree Constraint</em>'.
	 * @generated
	 */
	CrossTreeConstraint createCrossTreeConstraint();

	/**
	 * Returns a new object of class '<em>Group Feature</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Group Feature</em>'.
	 * @generated
	 */
	GroupFeature createGroupFeature();

	/**
	 * Returns a new object of class '<em>Quality Attributes Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Quality Attributes Model</em>'.
	 * @generated
	 */
	QualityAttributesModel createQualityAttributesModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MDEOptimiser4EFMPackage getMDEOptimiser4EFMPackage();

} //MDEOptimiser4EFMFactory
