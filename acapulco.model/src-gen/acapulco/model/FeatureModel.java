/**
 */
package acapulco.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.FeatureModel#getOwnedRoot <em>Owned Root</em>}</li>
 *   <li>{@link acapulco.model.FeatureModel#getName <em>Name</em>}</li>
 *   <li>{@link acapulco.model.FeatureModel#getOwnedQualityAttributeModel <em>Owned Quality Attribute Model</em>}</li>
 *   <li>{@link acapulco.model.FeatureModel#getCrossTreeConstraints <em>Cross Tree Constraints</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeatureModel()
 * @model
 * @generated
 */
public interface FeatureModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Owned Root</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Root</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Root</em>' containment reference.
	 * @see #setOwnedRoot(Feature)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeatureModel_OwnedRoot()
	 * @model containment="true"
	 * @generated
	 */
	Feature getOwnedRoot();

	/**
	 * Sets the value of the '{@link acapulco.model.FeatureModel#getOwnedRoot <em>Owned Root</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Root</em>' containment reference.
	 * @see #getOwnedRoot()
	 * @generated
	 */
	void setOwnedRoot(Feature value);

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
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeatureModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link acapulco.model.FeatureModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Owned Quality Attribute Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Quality Attribute Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Quality Attribute Model</em>' containment reference.
	 * @see #setOwnedQualityAttributeModel(QualityAttributesModel)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeatureModel_OwnedQualityAttributeModel()
	 * @model containment="true"
	 * @generated
	 */
	QualityAttributesModel getOwnedQualityAttributeModel();

	/**
	 * Sets the value of the '{@link acapulco.model.FeatureModel#getOwnedQualityAttributeModel <em>Owned Quality Attribute Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Quality Attribute Model</em>' containment reference.
	 * @see #getOwnedQualityAttributeModel()
	 * @generated
	 */
	void setOwnedQualityAttributeModel(QualityAttributesModel value);

	/**
	 * Returns the value of the '<em><b>Cross Tree Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link acapulco.model.CrossTreeConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cross Tree Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cross Tree Constraints</em>' containment reference list.
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getFeatureModel_CrossTreeConstraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<CrossTreeConstraint> getCrossTreeConstraints();

} // FeatureModel
