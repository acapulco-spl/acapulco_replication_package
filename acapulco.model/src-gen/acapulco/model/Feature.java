/**
 */
package acapulco.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.Feature#getName <em>Name</em>}</li>
 *   <li>{@link acapulco.model.Feature#getOwnedFeatures <em>Owned Features</em>}</li>
 *   <li>{@link acapulco.model.Feature#getParentFeature <em>Parent Feature</em>}</li>
 *   <li>{@link acapulco.model.Feature#isOptional <em>Optional</em>}</li>
 *   <li>{@link acapulco.model.Feature#isSelected <em>Selected</em>}</li>
 *   <li>{@link acapulco.model.Feature#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature()
 * @model
 * @generated
 */
public interface Feature extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature_Name()
	 * @model id="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link acapulco.model.Feature#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Selected</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Selected</em>' attribute.
	 * @see #setSelected(boolean)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature_Selected()
	 * @model
	 * @generated
	 */
	boolean isSelected();

	/**
	 * Sets the value of the '{@link acapulco.model.Feature#isSelected <em>Selected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Selected</em>' attribute.
	 * @see #isSelected()
	 * @generated
	 */
	void setSelected(boolean value);

	/**
	 * Returns the value of the '<em><b>Quality Attribute Annotations</b></em>' reference list.
	 * The list contents are of type {@link acapulco.model.QualityAttributeAnnotation}.
	 * It is bidirectional and its opposite is '{@link acapulco.model.QualityAttributeAnnotation#getFeatures <em>Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quality Attribute Annotations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quality Attribute Annotations</em>' reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature_QualityAttributeAnnotations()
	 * @see acapulco.model.QualityAttributeAnnotation#getFeatures
	 * @model opposite="features"
	 * @generated
	 */
	EList<QualityAttributeAnnotation> getQualityAttributeAnnotations();

	/**
	 * Returns the value of the '<em><b>Owned Features</b></em>' containment reference list.
	 * The list contents are of type {@link acapulco.model.Feature}.
	 * It is bidirectional and its opposite is '{@link acapulco.model.Feature#getParentFeature <em>Parent Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Features</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Features</em>' containment reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature_OwnedFeatures()
	 * @see acapulco.model.Feature#getParentFeature
	 * @model opposite="parentFeature" containment="true"
	 * @generated
	 */
	EList<Feature> getOwnedFeatures();

	/**
	 * Returns the value of the '<em><b>Parent Feature</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link acapulco.model.Feature#getOwnedFeatures <em>Owned Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Feature</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Feature</em>' container reference.
	 * @see #setParentFeature(Feature)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature_ParentFeature()
	 * @see acapulco.model.Feature#getOwnedFeatures
	 * @model opposite="ownedFeatures" transient="false"
	 * @generated
	 */
	Feature getParentFeature();

	/**
	 * Sets the value of the '{@link acapulco.model.Feature#getParentFeature <em>Parent Feature</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Feature</em>' container reference.
	 * @see #getParentFeature()
	 * @generated
	 */
	void setParentFeature(Feature value);

	/**
	 * Returns the value of the '<em><b>Optional</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optional</em>' attribute.
	 * @see #setOptional(boolean)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeature_Optional()
	 * @model default="true"
	 * @generated
	 */
	boolean isOptional();

	/**
	 * Sets the value of the '{@link acapulco.model.Feature#isOptional <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optional</em>' attribute.
	 * @see #isOptional()
	 * @generated
	 */
	void setOptional(boolean value);

} // Feature
