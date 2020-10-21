/**
 */
package acapulco.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extended Feature Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.ExtendedFeatureModel#getOwnedRoot <em>Owned Root</em>}</li>
 *   <li>{@link acapulco.model.ExtendedFeatureModel#getName <em>Name</em>}</li>
 *   <li>{@link acapulco.model.ExtendedFeatureModel#getOwnedQualityAttributes <em>Owned Quality Attributes</em>}</li>
 *   <li>{@link acapulco.model.ExtendedFeatureModel#getCrossTreeConstraints <em>Cross Tree Constraints</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getExtendedFeatureModel()
 * @model
 * @generated
 */
public interface ExtendedFeatureModel extends EObject {
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
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getExtendedFeatureModel_OwnedRoot()
	 * @model containment="true"
	 * @generated
	 */
	Feature getOwnedRoot();

	/**
	 * Sets the value of the '{@link acapulco.model.ExtendedFeatureModel#getOwnedRoot <em>Owned Root</em>}' containment reference.
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
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getExtendedFeatureModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link acapulco.model.ExtendedFeatureModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getExtendedFeatureModel_OwnedQualityAttributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<QualityAttribute> getOwnedQualityAttributes();

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
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getExtendedFeatureModel_CrossTreeConstraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<CrossTreeConstraint> getCrossTreeConstraints();

} // ExtendedFeatureModel
