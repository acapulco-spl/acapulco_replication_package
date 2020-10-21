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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import acapulco.model.CrossTreeConstraint;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.model.MDEOptimiser4EFMPackage;
import acapulco.model.QualityAttributesModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureModelImpl#getOwnedRoot <em>Owned Root</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureModelImpl#getOwnedQualityAttributeModel <em>Owned Quality Attribute Model</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.FeatureModelImpl#getCrossTreeConstraints <em>Cross Tree Constraints</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureModelImpl extends MinimalEObjectImpl.Container implements FeatureModel {
	/**
	 * The cached value of the '{@link #getOwnedRoot() <em>Owned Root</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedRoot()
	 * @generated
	 * @ordered
	 */
	protected Feature ownedRoot;

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
	 * The cached value of the '{@link #getOwnedQualityAttributeModel() <em>Owned Quality Attribute Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedQualityAttributeModel()
	 * @generated
	 * @ordered
	 */
	protected QualityAttributesModel ownedQualityAttributeModel;

	/**
	 * The cached value of the '{@link #getCrossTreeConstraints() <em>Cross Tree Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrossTreeConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<CrossTreeConstraint> crossTreeConstraints;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.FEATURE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature getOwnedRoot() {
		return ownedRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedRoot(Feature newOwnedRoot, NotificationChain msgs) {
		Feature oldOwnedRoot = ownedRoot;
		ownedRoot = newOwnedRoot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT, oldOwnedRoot, newOwnedRoot);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwnedRoot(Feature newOwnedRoot) {
		if (newOwnedRoot != ownedRoot) {
			NotificationChain msgs = null;
			if (ownedRoot != null)
				msgs = ((InternalEObject) ownedRoot).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT, null, msgs);
			if (newOwnedRoot != null)
				msgs = ((InternalEObject) newOwnedRoot).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT, null, msgs);
			msgs = basicSetOwnedRoot(newOwnedRoot, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT,
					newOwnedRoot, newOwnedRoot));
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
			eNotify(new ENotificationImpl(this, Notification.SET, MDEOptimiser4EFMPackage.FEATURE_MODEL__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualityAttributesModel getOwnedQualityAttributeModel() {
		return ownedQualityAttributeModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedQualityAttributeModel(QualityAttributesModel newOwnedQualityAttributeModel,
			NotificationChain msgs) {
		QualityAttributesModel oldOwnedQualityAttributeModel = ownedQualityAttributeModel;
		ownedQualityAttributeModel = newOwnedQualityAttributeModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL, oldOwnedQualityAttributeModel,
					newOwnedQualityAttributeModel);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwnedQualityAttributeModel(QualityAttributesModel newOwnedQualityAttributeModel) {
		if (newOwnedQualityAttributeModel != ownedQualityAttributeModel) {
			NotificationChain msgs = null;
			if (ownedQualityAttributeModel != null)
				msgs = ((InternalEObject) ownedQualityAttributeModel).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL,
						null, msgs);
			if (newOwnedQualityAttributeModel != null)
				msgs = ((InternalEObject) newOwnedQualityAttributeModel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL,
						null, msgs);
			msgs = basicSetOwnedQualityAttributeModel(newOwnedQualityAttributeModel, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL, newOwnedQualityAttributeModel,
					newOwnedQualityAttributeModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CrossTreeConstraint> getCrossTreeConstraints() {
		if (crossTreeConstraints == null) {
			crossTreeConstraints = new EObjectContainmentEList<CrossTreeConstraint>(CrossTreeConstraint.class, this,
					MDEOptimiser4EFMPackage.FEATURE_MODEL__CROSS_TREE_CONSTRAINTS);
		}
		return crossTreeConstraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT:
			return basicSetOwnedRoot(null, msgs);
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL:
			return basicSetOwnedQualityAttributeModel(null, msgs);
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__CROSS_TREE_CONSTRAINTS:
			return ((InternalEList<?>) getCrossTreeConstraints()).basicRemove(otherEnd, msgs);
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
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT:
			return getOwnedRoot();
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__NAME:
			return getName();
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL:
			return getOwnedQualityAttributeModel();
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__CROSS_TREE_CONSTRAINTS:
			return getCrossTreeConstraints();
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
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT:
			setOwnedRoot((Feature) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__NAME:
			setName((String) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL:
			setOwnedQualityAttributeModel((QualityAttributesModel) newValue);
			return;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__CROSS_TREE_CONSTRAINTS:
			getCrossTreeConstraints().clear();
			getCrossTreeConstraints().addAll((Collection<? extends CrossTreeConstraint>) newValue);
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
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT:
			setOwnedRoot((Feature) null);
			return;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__NAME:
			setName(NAME_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL:
			setOwnedQualityAttributeModel((QualityAttributesModel) null);
			return;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__CROSS_TREE_CONSTRAINTS:
			getCrossTreeConstraints().clear();
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
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_ROOT:
			return ownedRoot != null;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL:
			return ownedQualityAttributeModel != null;
		case MDEOptimiser4EFMPackage.FEATURE_MODEL__CROSS_TREE_CONSTRAINTS:
			return crossTreeConstraints != null && !crossTreeConstraints.isEmpty();
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
		result.append(')');
		return result.toString();
	}

} //FeatureModelImpl
