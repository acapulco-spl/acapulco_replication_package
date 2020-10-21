/**
 */
package acapulco.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import acapulco.model.MDEOptimiser4EFMPackage;
import acapulco.model.QualityAttribute;
import acapulco.model.QualityAttributeAnnotation;
import acapulco.model.QualityAttributesModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Quality Attributes Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributesModelImpl#getOwnedQualityAttributes <em>Owned Quality Attributes</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributesModelImpl#getOwnedQualityAttributeAnnotations <em>Owned Quality Attribute Annotations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QualityAttributesModelImpl extends MinimalEObjectImpl.Container implements QualityAttributesModel {
	/**
	 * The cached value of the '{@link #getOwnedQualityAttributes() <em>Owned Quality Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedQualityAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<QualityAttribute> ownedQualityAttributes;

	/**
	 * The cached value of the '{@link #getOwnedQualityAttributeAnnotations() <em>Owned Quality Attribute Annotations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedQualityAttributeAnnotations()
	 * @generated
	 * @ordered
	 */
	protected EList<QualityAttributeAnnotation> ownedQualityAttributeAnnotations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QualityAttributesModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.QUALITY_ATTRIBUTES_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<QualityAttribute> getOwnedQualityAttributes() {
		if (ownedQualityAttributes == null) {
			ownedQualityAttributes = new EObjectContainmentEList<QualityAttribute>(QualityAttribute.class, this,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES);
		}
		return ownedQualityAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<QualityAttributeAnnotation> getOwnedQualityAttributeAnnotations() {
		if (ownedQualityAttributeAnnotations == null) {
			ownedQualityAttributeAnnotations = new EObjectContainmentEList<QualityAttributeAnnotation>(
					QualityAttributeAnnotation.class, this,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS);
		}
		return ownedQualityAttributeAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES:
			return ((InternalEList<?>) getOwnedQualityAttributes()).basicRemove(otherEnd, msgs);
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS:
			return ((InternalEList<?>) getOwnedQualityAttributeAnnotations()).basicRemove(otherEnd, msgs);
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES:
			return getOwnedQualityAttributes();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS:
			return getOwnedQualityAttributeAnnotations();
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES:
			getOwnedQualityAttributes().clear();
			getOwnedQualityAttributes().addAll((Collection<? extends QualityAttribute>) newValue);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS:
			getOwnedQualityAttributeAnnotations().clear();
			getOwnedQualityAttributeAnnotations().addAll((Collection<? extends QualityAttributeAnnotation>) newValue);
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES:
			getOwnedQualityAttributes().clear();
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS:
			getOwnedQualityAttributeAnnotations().clear();
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES:
			return ownedQualityAttributes != null && !ownedQualityAttributes.isEmpty();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS:
			return ownedQualityAttributeAnnotations != null && !ownedQualityAttributeAnnotations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //QualityAttributesModelImpl
