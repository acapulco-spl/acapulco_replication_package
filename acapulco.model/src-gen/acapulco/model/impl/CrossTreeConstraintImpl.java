/**
 */
package acapulco.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import acapulco.model.CrossTreeConstraint;
import acapulco.model.CrossTreeConstraintType;
import acapulco.model.Feature;
import acapulco.model.MDEOptimiser4EFMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cross Tree Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.CrossTreeConstraintImpl#getLeftFeature <em>Left Feature</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.CrossTreeConstraintImpl#getRightFeature <em>Right Feature</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.CrossTreeConstraintImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CrossTreeConstraintImpl extends MinimalEObjectImpl.Container implements CrossTreeConstraint {
	/**
	 * The cached value of the '{@link #getLeftFeature() <em>Left Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeftFeature()
	 * @generated
	 * @ordered
	 */
	protected Feature leftFeature;

	/**
	 * The cached value of the '{@link #getRightFeature() <em>Right Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRightFeature()
	 * @generated
	 * @ordered
	 */
	protected Feature rightFeature;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final CrossTreeConstraintType TYPE_EDEFAULT = CrossTreeConstraintType.REQUIRES;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected CrossTreeConstraintType type = TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CrossTreeConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.CROSS_TREE_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature getLeftFeature() {
		if (leftFeature != null && leftFeature.eIsProxy()) {
			InternalEObject oldLeftFeature = (InternalEObject) leftFeature;
			leftFeature = (Feature) eResolveProxy(oldLeftFeature);
			if (leftFeature != oldLeftFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__LEFT_FEATURE, oldLeftFeature, leftFeature));
			}
		}
		return leftFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature basicGetLeftFeature() {
		return leftFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeftFeature(Feature newLeftFeature) {
		Feature oldLeftFeature = leftFeature;
		leftFeature = newLeftFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__LEFT_FEATURE, oldLeftFeature, leftFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature getRightFeature() {
		if (rightFeature != null && rightFeature.eIsProxy()) {
			InternalEObject oldRightFeature = (InternalEObject) rightFeature;
			rightFeature = (Feature) eResolveProxy(oldRightFeature);
			if (rightFeature != oldRightFeature) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__RIGHT_FEATURE, oldRightFeature,
							rightFeature));
			}
		}
		return rightFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature basicGetRightFeature() {
		return rightFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRightFeature(Feature newRightFeature) {
		Feature oldRightFeature = rightFeature;
		rightFeature = newRightFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__RIGHT_FEATURE, oldRightFeature, rightFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CrossTreeConstraintType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(CrossTreeConstraintType newType) {
		CrossTreeConstraintType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__TYPE,
					oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__LEFT_FEATURE:
			if (resolve)
				return getLeftFeature();
			return basicGetLeftFeature();
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__RIGHT_FEATURE:
			if (resolve)
				return getRightFeature();
			return basicGetRightFeature();
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__TYPE:
			return getType();
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
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__LEFT_FEATURE:
			setLeftFeature((Feature) newValue);
			return;
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__RIGHT_FEATURE:
			setRightFeature((Feature) newValue);
			return;
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__TYPE:
			setType((CrossTreeConstraintType) newValue);
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
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__LEFT_FEATURE:
			setLeftFeature((Feature) null);
			return;
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__RIGHT_FEATURE:
			setRightFeature((Feature) null);
			return;
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__TYPE:
			setType(TYPE_EDEFAULT);
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
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__LEFT_FEATURE:
			return leftFeature != null;
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__RIGHT_FEATURE:
			return rightFeature != null;
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT__TYPE:
			return type != TYPE_EDEFAULT;
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
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //CrossTreeConstraintImpl
