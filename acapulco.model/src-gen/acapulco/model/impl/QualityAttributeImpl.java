/**
 */
package acapulco.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import acapulco.model.MDEOptimiser4EFMPackage;
import acapulco.model.OptimizationType;
import acapulco.model.QualityAttribute;
import acapulco.model.QualityAttributeAnnotation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Quality Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributeImpl#getName <em>Name</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributeImpl#getOptimizationType <em>Optimization Type</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributeImpl#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QualityAttributeImpl extends MinimalEObjectImpl.Container implements QualityAttribute {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOptimizationType() <em>Optimization Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimizationType()
	 * @generated
	 * @ordered
	 */
	protected static final OptimizationType OPTIMIZATION_TYPE_EDEFAULT = OptimizationType.MAXIMIZE;

	/**
	 * The cached value of the '{@link #getOptimizationType() <em>Optimization Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimizationType()
	 * @generated
	 * @ordered
	 */
	protected OptimizationType optimizationType = OPTIMIZATION_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getQualityAttributeAnnotations() <em>Quality Attribute Annotations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQualityAttributeAnnotations()
	 * @generated
	 * @ordered
	 */
	protected EList<QualityAttributeAnnotation> qualityAttributeAnnotations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QualityAttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.QUALITY_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__NAME,
					oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimizationType getOptimizationType() {
		return optimizationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptimizationType(OptimizationType newOptimizationType) {
		OptimizationType oldOptimizationType = optimizationType;
		optimizationType = newOptimizationType == null ? OPTIMIZATION_TYPE_EDEFAULT : newOptimizationType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE, oldOptimizationType,
					optimizationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<QualityAttributeAnnotation> getQualityAttributeAnnotations() {
		if (qualityAttributeAnnotations == null) {
			qualityAttributeAnnotations = new EObjectWithInverseResolvingEList<QualityAttributeAnnotation>(
					QualityAttributeAnnotation.class, this,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE);
		}
		return qualityAttributeAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getQualityAttributeAnnotations())
					.basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS:
			return ((InternalEList<?>) getQualityAttributeAnnotations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__NAME:
			return getName();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE:
			return getOptimizationType();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS:
			return getQualityAttributeAnnotations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__NAME:
			setName((String) newValue);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE:
			setOptimizationType((OptimizationType) newValue);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS:
			getQualityAttributeAnnotations().clear();
			getQualityAttributeAnnotations().addAll((Collection<? extends QualityAttributeAnnotation>) newValue);
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE:
			setOptimizationType(OPTIMIZATION_TYPE_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS:
			getQualityAttributeAnnotations().clear();
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE:
			return optimizationType != OPTIMIZATION_TYPE_EDEFAULT;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS:
			return qualityAttributeAnnotations != null && !qualityAttributeAnnotations.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", optimizationType: ");
		result.append(optimizationType);
		result.append(')');
		return result.toString();
	}

} //QualityAttributeImpl
