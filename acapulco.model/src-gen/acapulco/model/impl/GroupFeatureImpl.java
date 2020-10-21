/**
 */
package acapulco.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import acapulco.model.GroupFeature;
import acapulco.model.MDEOptimiser4EFMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.GroupFeatureImpl#getChildMinCardinality <em>Child Min Cardinality</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.GroupFeatureImpl#getChildMaxCardinality <em>Child Max Cardinality</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GroupFeatureImpl extends FeatureImpl implements GroupFeature {
	/**
	 * The default value of the '{@link #getChildMinCardinality() <em>Child Min Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildMinCardinality()
	 * @generated
	 * @ordered
	 */
	protected static final int CHILD_MIN_CARDINALITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getChildMinCardinality() <em>Child Min Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildMinCardinality()
	 * @generated
	 * @ordered
	 */
	protected int childMinCardinality = CHILD_MIN_CARDINALITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getChildMaxCardinality() <em>Child Max Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildMaxCardinality()
	 * @generated
	 * @ordered
	 */
	protected static final int CHILD_MAX_CARDINALITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getChildMaxCardinality() <em>Child Max Cardinality</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildMaxCardinality()
	 * @generated
	 * @ordered
	 */
	protected int childMaxCardinality = CHILD_MAX_CARDINALITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupFeatureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.GROUP_FEATURE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getChildMinCardinality() {
		return childMinCardinality;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildMinCardinality(int newChildMinCardinality) {
		int oldChildMinCardinality = childMinCardinality;
		childMinCardinality = newChildMinCardinality;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MIN_CARDINALITY, oldChildMinCardinality,
					childMinCardinality));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getChildMaxCardinality() {
		return childMaxCardinality;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildMaxCardinality(int newChildMaxCardinality) {
		int oldChildMaxCardinality = childMaxCardinality;
		childMaxCardinality = newChildMaxCardinality;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MAX_CARDINALITY, oldChildMaxCardinality,
					childMaxCardinality));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MIN_CARDINALITY:
			return getChildMinCardinality();
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MAX_CARDINALITY:
			return getChildMaxCardinality();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MIN_CARDINALITY:
			setChildMinCardinality((Integer) newValue);
			return;
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MAX_CARDINALITY:
			setChildMaxCardinality((Integer) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MIN_CARDINALITY:
			setChildMinCardinality(CHILD_MIN_CARDINALITY_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MAX_CARDINALITY:
			setChildMaxCardinality(CHILD_MAX_CARDINALITY_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MIN_CARDINALITY:
			return childMinCardinality != CHILD_MIN_CARDINALITY_EDEFAULT;
		case MDEOptimiser4EFMPackage.GROUP_FEATURE__CHILD_MAX_CARDINALITY:
			return childMaxCardinality != CHILD_MAX_CARDINALITY_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (childMinCardinality: ");
		result.append(childMinCardinality);
		result.append(", childMaxCardinality: ");
		result.append(childMaxCardinality);
		result.append(')');
		return result.toString();
	}

} //GroupFeatureImpl
