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

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import acapulco.model.Feature;
import acapulco.model.MDEOptimiser4EFMPackage;
import acapulco.model.QualityAttributeAnnotation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureImpl#getName <em>Name</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureImpl#getOwnedFeatures <em>Owned Features</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureImpl#getParentFeature <em>Parent Feature</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureImpl#isSelected <em>Selected</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureImpl#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureImpl extends MinimalEObjectImpl.Container implements Feature {
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
	 * The cached value of the '{@link #getOwnedFeatures() <em>Owned Features</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<Feature> ownedFeatures;

	/**
	 * The default value of the '{@link #isOptional() <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptional()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPTIONAL_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isOptional() <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptional()
	 * @generated
	 * @ordered
	 */
	protected boolean optional = OPTIONAL_EDEFAULT;

	/**
	 * The default value of the '{@link #isSelected() <em>Selected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSelected()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SELECTED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSelected() <em>Selected</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSelected()
	 * @generated
	 * @ordered
	 */
	protected boolean selected = SELECTED_EDEFAULT;

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
	protected FeatureImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.FEATURE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.FEATURE__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelected(boolean newSelected) {
		boolean oldSelected = selected;
		selected = newSelected;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.FEATURE__SELECTED,
					oldSelected, selected));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<QualityAttributeAnnotation> getQualityAttributeAnnotations() {
		if (qualityAttributeAnnotations == null) {
			qualityAttributeAnnotations = new EObjectWithInverseResolvingEList.ManyInverse<QualityAttributeAnnotation>(
					QualityAttributeAnnotation.class, this,
					MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES);
		}
		return qualityAttributeAnnotations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Feature> getOwnedFeatures() {
		if (ownedFeatures == null) {
			ownedFeatures = new EObjectContainmentWithInverseEList<Feature>(Feature.class, this,
					MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES, MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE);
		}
		return ownedFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature getParentFeature() {
		if (eContainerFeatureID() != MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE)
			return null;
		return (Feature) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParentFeature(Feature newParentFeature, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newParentFeature, MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE,
				msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentFeature(Feature newParentFeature) {
		if (newParentFeature != eInternalContainer()
				|| (eContainerFeatureID() != MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE
						&& newParentFeature != null)) {
			if (EcoreUtil.isAncestor(this, newParentFeature))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParentFeature != null)
				msgs = ((InternalEObject) newParentFeature).eInverseAdd(this,
						MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES, Feature.class, msgs);
			msgs = basicSetParentFeature(newParentFeature, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE,
					newParentFeature, newParentFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptional(boolean newOptional) {
		boolean oldOptional = optional;
		optional = newOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.FEATURE__OPTIONAL,
					oldOptional, optional));
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
		case MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedFeatures()).basicAdd(otherEnd, msgs);
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetParentFeature((Feature) otherEnd, msgs);
		case MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS:
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
		case MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES:
			return ((InternalEList<?>) getOwnedFeatures()).basicRemove(otherEnd, msgs);
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			return basicSetParentFeature(null, msgs);
		case MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS:
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
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			return eInternalContainer().eInverseRemove(this, MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES,
					Feature.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.FEATURE__NAME:
			return getName();
		case MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES:
			return getOwnedFeatures();
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			return getParentFeature();
		case MDEOptimiser4EFMPackage.FEATURE__OPTIONAL:
			return isOptional();
		case MDEOptimiser4EFMPackage.FEATURE__SELECTED:
			return isSelected();
		case MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS:
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
		case MDEOptimiser4EFMPackage.FEATURE__NAME:
			setName((String) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES:
			getOwnedFeatures().clear();
			getOwnedFeatures().addAll((Collection<? extends Feature>) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			setParentFeature((Feature) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__OPTIONAL:
			setOptional((Boolean) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__SELECTED:
			setSelected((Boolean) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS:
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
		case MDEOptimiser4EFMPackage.FEATURE__NAME:
			setName(NAME_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES:
			getOwnedFeatures().clear();
			return;
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			setParentFeature((Feature) null);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__OPTIONAL:
			setOptional(OPTIONAL_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__SELECTED:
			setSelected(SELECTED_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS:
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
		case MDEOptimiser4EFMPackage.FEATURE__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case MDEOptimiser4EFMPackage.FEATURE__OWNED_FEATURES:
			return ownedFeatures != null && !ownedFeatures.isEmpty();
		case MDEOptimiser4EFMPackage.FEATURE__PARENT_FEATURE:
			return getParentFeature() != null;
		case MDEOptimiser4EFMPackage.FEATURE__OPTIONAL:
			return optional != OPTIONAL_EDEFAULT;
		case MDEOptimiser4EFMPackage.FEATURE__SELECTED:
			return selected != SELECTED_EDEFAULT;
		case MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS:
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
		result.append(", optional: ");
		result.append(optional);
		result.append(", selected: ");
		result.append(selected);
		result.append(')');
		return result.toString();
	}

} //FeatureImpl
