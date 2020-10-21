/**
 */
package acapulco.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cross Tree Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.CrossTreeConstraint#getLeftFeature <em>Left Feature</em>}</li>
 *   <li>{@link acapulco.model.CrossTreeConstraint#getRightFeature <em>Right Feature</em>}</li>
 *   <li>{@link acapulco.model.CrossTreeConstraint#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getCrossTreeConstraint()
 * @model
 * @generated
 */
public interface CrossTreeConstraint extends EObject {
	/**
	 * Returns the value of the '<em><b>Left Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Left Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left Feature</em>' reference.
	 * @see #setLeftFeature(Feature)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getCrossTreeConstraint_LeftFeature()
	 * @model
	 * @generated
	 */
	Feature getLeftFeature();

	/**
	 * Sets the value of the '{@link acapulco.model.CrossTreeConstraint#getLeftFeature <em>Left Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left Feature</em>' reference.
	 * @see #getLeftFeature()
	 * @generated
	 */
	void setLeftFeature(Feature value);

	/**
	 * Returns the value of the '<em><b>Right Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Right Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Right Feature</em>' reference.
	 * @see #setRightFeature(Feature)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getCrossTreeConstraint_RightFeature()
	 * @model
	 * @generated
	 */
	Feature getRightFeature();

	/**
	 * Sets the value of the '{@link acapulco.model.CrossTreeConstraint#getRightFeature <em>Right Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Right Feature</em>' reference.
	 * @see #getRightFeature()
	 * @generated
	 */
	void setRightFeature(Feature value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link acapulco.model.CrossTreeConstraintType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see acapulco.model.CrossTreeConstraintType
	 * @see #setType(CrossTreeConstraintType)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getCrossTreeConstraint_Type()
	 * @model
	 * @generated
	 */
	CrossTreeConstraintType getType();

	/**
	 * Sets the value of the '{@link acapulco.model.CrossTreeConstraint#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see acapulco.model.CrossTreeConstraintType
	 * @see #getType()
	 * @generated
	 */
	void setType(CrossTreeConstraintType value);

} // CrossTreeConstraint
