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

import acapulco.model.Feature;
import acapulco.model.MDEOptimiser4EFMPackage;
import acapulco.model.QualityAttribute;
import acapulco.model.QualityAttributeAnnotation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Quality Attribute Annotation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl#getValue <em>Value</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl#getQualityAttribute <em>Quality Attribute</em>}</li>
 *   <li>{@link mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QualityAttributeAnnotationImpl extends MinimalEObjectImpl.Container implements QualityAttributeAnnotation {
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final double VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected double value = VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getQualityAttribute() <em>Quality Attribute</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQualityAttribute()
	 * @generated
	 * @ordered
	 */
	protected QualityAttribute qualityAttribute;

	/**
	 * The cached value of the '{@link #getFeatures() <em>Features</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<Feature> features;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QualityAttributeAnnotationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MDEOptimiser4EFMPackage.Literals.QUALITY_ATTRIBUTE_ANNOTATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(double newValue) {
		double oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualityAttribute getQualityAttribute() {
		if (qualityAttribute != null && qualityAttribute.eIsProxy()) {
			InternalEObject oldQualityAttribute = (InternalEObject) qualityAttribute;
			qualityAttribute = (QualityAttribute) eResolveProxy(oldQualityAttribute);
			if (qualityAttribute != oldQualityAttribute) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE,
							oldQualityAttribute, qualityAttribute));
			}
		}
		return qualityAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualityAttribute basicGetQualityAttribute() {
		return qualityAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetQualityAttribute(QualityAttribute newQualityAttribute, NotificationChain msgs) {
		QualityAttribute oldQualityAttribute = qualityAttribute;
		qualityAttribute = newQualityAttribute;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE, oldQualityAttribute,
					newQualityAttribute);
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
	public void setQualityAttribute(QualityAttribute newQualityAttribute) {
		if (newQualityAttribute != qualityAttribute) {
			NotificationChain msgs = null;
			if (qualityAttribute != null)
				msgs = ((InternalEObject) qualityAttribute).eInverseRemove(this,
						MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS,
						QualityAttribute.class, msgs);
			if (newQualityAttribute != null)
				msgs = ((InternalEObject) newQualityAttribute).eInverseAdd(this,
						MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS,
						QualityAttribute.class, msgs);
			msgs = basicSetQualityAttribute(newQualityAttribute, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE, newQualityAttribute,
					newQualityAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Feature> getFeatures() {
		if (features == null) {
			features = new EObjectWithInverseResolvingEList.ManyInverse<Feature>(Feature.class, this,
					MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES,
					MDEOptimiser4EFMPackage.FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS);
		}
		return features;
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE:
			if (qualityAttribute != null)
				msgs = ((InternalEObject) qualityAttribute).eInverseRemove(this,
						MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS,
						QualityAttribute.class, msgs);
			return basicSetQualityAttribute((QualityAttribute) otherEnd, msgs);
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getFeatures()).basicAdd(otherEnd, msgs);
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE:
			return basicSetQualityAttribute(null, msgs);
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES:
			return ((InternalEList<?>) getFeatures()).basicRemove(otherEnd, msgs);
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__VALUE:
			return getValue();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE:
			if (resolve)
				return getQualityAttribute();
			return basicGetQualityAttribute();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES:
			return getFeatures();
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__VALUE:
			setValue((Double) newValue);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE:
			setQualityAttribute((QualityAttribute) newValue);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES:
			getFeatures().clear();
			getFeatures().addAll((Collection<? extends Feature>) newValue);
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__VALUE:
			setValue(VALUE_EDEFAULT);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE:
			setQualityAttribute((QualityAttribute) null);
			return;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES:
			getFeatures().clear();
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
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__VALUE:
			return value != VALUE_EDEFAULT;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE:
			return qualityAttribute != null;
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION__FEATURES:
			return features != null && !features.isEmpty();
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
		result.append(" (value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} //QualityAttributeAnnotationImpl
