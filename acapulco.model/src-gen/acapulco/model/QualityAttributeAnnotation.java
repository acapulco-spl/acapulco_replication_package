/**
 */
package acapulco.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Quality Attribute Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.QualityAttributeAnnotation#getValue <em>Value</em>}</li>
 *   <li>{@link acapulco.model.QualityAttributeAnnotation#getQualityAttribute <em>Quality Attribute</em>}</li>
 *   <li>{@link acapulco.model.QualityAttributeAnnotation#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributeAnnotation()
 * @model
 * @generated
 */
public interface QualityAttributeAnnotation extends EObject {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(double)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributeAnnotation_Value()
	 * @model
	 * @generated
	 */
	double getValue();

	/**
	 * Sets the value of the '{@link acapulco.model.QualityAttributeAnnotation#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(double value);

	/**
	 * Returns the value of the '<em><b>Quality Attribute</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link acapulco.model.QualityAttribute#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quality Attribute</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quality Attribute</em>' reference.
	 * @see #setQualityAttribute(QualityAttribute)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributeAnnotation_QualityAttribute()
	 * @see acapulco.model.QualityAttribute#getQualityAttributeAnnotations
	 * @model opposite="qualityAttributeAnnotations"
	 * @generated
	 */
	QualityAttribute getQualityAttribute();

	/**
	 * Sets the value of the '{@link acapulco.model.QualityAttributeAnnotation#getQualityAttribute <em>Quality Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quality Attribute</em>' reference.
	 * @see #getQualityAttribute()
	 * @generated
	 */
	void setQualityAttribute(QualityAttribute value);

	/**
	 * Returns the value of the '<em><b>Features</b></em>' reference list.
	 * The list contents are of type {@link acapulco.model.Feature}.
	 * It is bidirectional and its opposite is '{@link acapulco.model.Feature#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Features</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Features</em>' reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributeAnnotation_Features()
	 * @see acapulco.model.Feature#getQualityAttributeAnnotations
	 * @model opposite="qualityAttributeAnnotations"
	 * @generated
	 */
	EList<Feature> getFeatures();

} // QualityAttributeAnnotation
