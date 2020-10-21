/**
 */
package acapulco.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Quality Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.QualityAttribute#getName <em>Name</em>}</li>
 *   <li>{@link acapulco.model.QualityAttribute#getOptimizationType <em>Optimization Type</em>}</li>
 *   <li>{@link acapulco.model.QualityAttribute#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttribute()
 * @model
 * @generated
 */
public interface QualityAttribute extends EObject {
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
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttribute_Name()
	 * @model id="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link acapulco.model.QualityAttribute#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Optimization Type</b></em>' attribute.
	 * The literals are from the enumeration {@link acapulco.model.OptimizationType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optimization Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optimization Type</em>' attribute.
	 * @see acapulco.model.OptimizationType
	 * @see #setOptimizationType(OptimizationType)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttribute_OptimizationType()
	 * @model
	 * @generated
	 */
	OptimizationType getOptimizationType();

	/**
	 * Sets the value of the '{@link acapulco.model.QualityAttribute#getOptimizationType <em>Optimization Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optimization Type</em>' attribute.
	 * @see acapulco.model.OptimizationType
	 * @see #getOptimizationType()
	 * @generated
	 */
	void setOptimizationType(OptimizationType value);

	/**
	 * Returns the value of the '<em><b>Quality Attribute Annotations</b></em>' reference list.
	 * The list contents are of type {@link acapulco.model.QualityAttributeAnnotation}.
	 * It is bidirectional and its opposite is '{@link acapulco.model.QualityAttributeAnnotation#getQualityAttribute <em>Quality Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quality Attribute Annotations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quality Attribute Annotations</em>' reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttribute_QualityAttributeAnnotations()
	 * @see acapulco.model.QualityAttributeAnnotation#getQualityAttribute
	 * @model opposite="qualityAttribute"
	 * @generated
	 */
	EList<QualityAttributeAnnotation> getQualityAttributeAnnotations();

} // QualityAttribute
