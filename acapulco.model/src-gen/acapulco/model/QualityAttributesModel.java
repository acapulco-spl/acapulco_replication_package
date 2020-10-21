/**
 */
package acapulco.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Quality Attributes Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.QualityAttributesModel#getOwnedQualityAttributes <em>Owned Quality Attributes</em>}</li>
 *   <li>{@link acapulco.model.QualityAttributesModel#getOwnedQualityAttributeAnnotations <em>Owned Quality Attribute Annotations</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributesModel()
 * @model
 * @generated
 */
public interface QualityAttributesModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Owned Quality Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link acapulco.model.QualityAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Quality Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Quality Attributes</em>' containment reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributesModel_OwnedQualityAttributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<QualityAttribute> getOwnedQualityAttributes();

	/**
	 * Returns the value of the '<em><b>Owned Quality Attribute Annotations</b></em>' containment reference list.
	 * The list contents are of type {@link acapulco.model.QualityAttributeAnnotation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Quality Attribute Annotations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Quality Attribute Annotations</em>' containment reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getQualityAttributesModel_OwnedQualityAttributeAnnotations()
	 * @model containment="true"
	 * @generated
	 */
	EList<QualityAttributeAnnotation> getOwnedQualityAttributeAnnotations();

} // QualityAttributesModel
