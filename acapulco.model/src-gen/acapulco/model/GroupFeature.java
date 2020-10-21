/**
 */
package acapulco.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group Feature</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link acapulco.model.GroupFeature#getChildMinCardinality <em>Child Min Cardinality</em>}</li>
 *   <li>{@link acapulco.model.GroupFeature#getChildMaxCardinality <em>Child Max Cardinality</em>}</li>
 * </ul>
 *
 * @see acapulco.model.MDEOptimiser4EFMPackage#getGroupFeature()
 * @model
 * @generated
 */
public interface GroupFeature extends Feature {
	/**
	 * Returns the value of the '<em><b>Child Min Cardinality</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Min Cardinality</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Min Cardinality</em>' attribute.
	 * @see #setChildMinCardinality(int)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getGroupFeature_ChildMinCardinality()
	 * @model
	 * @generated
	 */
	int getChildMinCardinality();

	/**
	 * Sets the value of the '{@link acapulco.model.GroupFeature#getChildMinCardinality <em>Child Min Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Min Cardinality</em>' attribute.
	 * @see #getChildMinCardinality()
	 * @generated
	 */
	void setChildMinCardinality(int value);

	/**
	 * Returns the value of the '<em><b>Child Max Cardinality</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Max Cardinality</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Max Cardinality</em>' attribute.
	 * @see #setChildMaxCardinality(int)
	 * @see acapulco.model.MDEOptimiser4EFMPackage#getGroupFeature_ChildMaxCardinality()
	 * @model
	 * @generated
	 */
	int getChildMaxCardinality();

	/**
	 * Sets the value of the '{@link acapulco.model.GroupFeature#getChildMaxCardinality <em>Child Max Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Max Cardinality</em>' attribute.
	 * @see #getChildMaxCardinality()
	 * @generated
	 */
	void setChildMaxCardinality(int value);

} // GroupFeature
