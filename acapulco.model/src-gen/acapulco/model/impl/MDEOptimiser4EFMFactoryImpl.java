/**
 */
package acapulco.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import acapulco.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MDEOptimiser4EFMFactoryImpl extends EFactoryImpl implements MDEOptimiser4EFMFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MDEOptimiser4EFMFactory init() {
		try {
			MDEOptimiser4EFMFactory theMDEOptimiser4EFMFactory = (MDEOptimiser4EFMFactory) EPackage.Registry.INSTANCE
					.getEFactory(MDEOptimiser4EFMPackage.eNS_URI);
			if (theMDEOptimiser4EFMFactory != null) {
				return theMDEOptimiser4EFMFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MDEOptimiser4EFMFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDEOptimiser4EFMFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case MDEOptimiser4EFMPackage.FEATURE_MODEL:
			return createFeatureModel();
		case MDEOptimiser4EFMPackage.FEATURE:
			return createFeature();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE:
			return createQualityAttribute();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTE_ANNOTATION:
			return createQualityAttributeAnnotation();
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT:
			return createCrossTreeConstraint();
		case MDEOptimiser4EFMPackage.GROUP_FEATURE:
			return createGroupFeature();
		case MDEOptimiser4EFMPackage.QUALITY_ATTRIBUTES_MODEL:
			return createQualityAttributesModel();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT_TYPE:
			return createCrossTreeConstraintTypeFromString(eDataType, initialValue);
		case MDEOptimiser4EFMPackage.OPTIMIZATION_TYPE:
			return createOptimizationTypeFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case MDEOptimiser4EFMPackage.CROSS_TREE_CONSTRAINT_TYPE:
			return convertCrossTreeConstraintTypeToString(eDataType, instanceValue);
		case MDEOptimiser4EFMPackage.OPTIMIZATION_TYPE:
			return convertOptimizationTypeToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureModel createFeatureModel() {
		FeatureModelImpl featureModel = new FeatureModelImpl();
		return featureModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Feature createFeature() {
		FeatureImpl feature = new FeatureImpl();
		return feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualityAttribute createQualityAttribute() {
		QualityAttributeImpl qualityAttribute = new QualityAttributeImpl();
		return qualityAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualityAttributeAnnotation createQualityAttributeAnnotation() {
		QualityAttributeAnnotationImpl qualityAttributeAnnotation = new QualityAttributeAnnotationImpl();
		return qualityAttributeAnnotation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CrossTreeConstraint createCrossTreeConstraint() {
		CrossTreeConstraintImpl crossTreeConstraint = new CrossTreeConstraintImpl();
		return crossTreeConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupFeature createGroupFeature() {
		GroupFeatureImpl groupFeature = new GroupFeatureImpl();
		return groupFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QualityAttributesModel createQualityAttributesModel() {
		QualityAttributesModelImpl qualityAttributesModel = new QualityAttributesModelImpl();
		return qualityAttributesModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CrossTreeConstraintType createCrossTreeConstraintTypeFromString(EDataType eDataType, String initialValue) {
		CrossTreeConstraintType result = CrossTreeConstraintType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCrossTreeConstraintTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimizationType createOptimizationTypeFromString(EDataType eDataType, String initialValue) {
		OptimizationType result = OptimizationType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException(
					"The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOptimizationTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDEOptimiser4EFMPackage getMDEOptimiser4EFMPackage() {
		return (MDEOptimiser4EFMPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MDEOptimiser4EFMPackage getPackage() {
		return MDEOptimiser4EFMPackage.eINSTANCE;
	}

} //MDEOptimiser4EFMFactoryImpl
