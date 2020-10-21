/**
 */
package acapulco.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import acapulco.model.CrossTreeConstraint;
import acapulco.model.CrossTreeConstraintType;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.model.GroupFeature;
import acapulco.model.MDEOptimiser4EFMFactory;
import acapulco.model.MDEOptimiser4EFMPackage;
import acapulco.model.OptimizationType;
import acapulco.model.QualityAttribute;
import acapulco.model.QualityAttributeAnnotation;
import acapulco.model.QualityAttributesModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MDEOptimiser4EFMPackageImpl extends EPackageImpl implements MDEOptimiser4EFMPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass qualityAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass qualityAttributeAnnotationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass crossTreeConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass groupFeatureEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass qualityAttributesModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum crossTreeConstraintTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum optimizationTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see acapulco.model.MDEOptimiser4EFMPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MDEOptimiser4EFMPackageImpl() {
		super(eNS_URI, MDEOptimiser4EFMFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MDEOptimiser4EFMPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MDEOptimiser4EFMPackage init() {
		if (isInited)
			return (MDEOptimiser4EFMPackage) EPackage.Registry.INSTANCE.getEPackage(MDEOptimiser4EFMPackage.eNS_URI);

		// Obtain or create and register package
		MDEOptimiser4EFMPackageImpl theMDEOptimiser4EFMPackage = (MDEOptimiser4EFMPackageImpl) (EPackage.Registry.INSTANCE
				.get(eNS_URI) instanceof MDEOptimiser4EFMPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
						: new MDEOptimiser4EFMPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theMDEOptimiser4EFMPackage.createPackageContents();

		// Initialize created meta-data
		theMDEOptimiser4EFMPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMDEOptimiser4EFMPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MDEOptimiser4EFMPackage.eNS_URI, theMDEOptimiser4EFMPackage);
		return theMDEOptimiser4EFMPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeatureModel() {
		return featureModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureModel_OwnedRoot() {
		return (EReference) featureModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeatureModel_Name() {
		return (EAttribute) featureModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureModel_OwnedQualityAttributeModel() {
		return (EReference) featureModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeatureModel_CrossTreeConstraints() {
		return (EReference) featureModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFeature() {
		return featureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeature_Name() {
		return (EAttribute) featureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeature_Selected() {
		return (EAttribute) featureEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeature_QualityAttributeAnnotations() {
		return (EReference) featureEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeature_OwnedFeatures() {
		return (EReference) featureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFeature_ParentFeature() {
		return (EReference) featureEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFeature_Optional() {
		return (EAttribute) featureEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQualityAttribute() {
		return qualityAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQualityAttribute_Name() {
		return (EAttribute) qualityAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQualityAttribute_OptimizationType() {
		return (EAttribute) qualityAttributeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQualityAttribute_QualityAttributeAnnotations() {
		return (EReference) qualityAttributeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQualityAttributeAnnotation() {
		return qualityAttributeAnnotationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQualityAttributeAnnotation_Value() {
		return (EAttribute) qualityAttributeAnnotationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQualityAttributeAnnotation_QualityAttribute() {
		return (EReference) qualityAttributeAnnotationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQualityAttributeAnnotation_Features() {
		return (EReference) qualityAttributeAnnotationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCrossTreeConstraint() {
		return crossTreeConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCrossTreeConstraint_LeftFeature() {
		return (EReference) crossTreeConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCrossTreeConstraint_RightFeature() {
		return (EReference) crossTreeConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCrossTreeConstraint_Type() {
		return (EAttribute) crossTreeConstraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGroupFeature() {
		return groupFeatureEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGroupFeature_ChildMinCardinality() {
		return (EAttribute) groupFeatureEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGroupFeature_ChildMaxCardinality() {
		return (EAttribute) groupFeatureEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQualityAttributesModel() {
		return qualityAttributesModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQualityAttributesModel_OwnedQualityAttributes() {
		return (EReference) qualityAttributesModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getQualityAttributesModel_OwnedQualityAttributeAnnotations() {
		return (EReference) qualityAttributesModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getCrossTreeConstraintType() {
		return crossTreeConstraintTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getOptimizationType() {
		return optimizationTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDEOptimiser4EFMFactory getMDEOptimiser4EFMFactory() {
		return (MDEOptimiser4EFMFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		featureModelEClass = createEClass(FEATURE_MODEL);
		createEReference(featureModelEClass, FEATURE_MODEL__OWNED_ROOT);
		createEAttribute(featureModelEClass, FEATURE_MODEL__NAME);
		createEReference(featureModelEClass, FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL);
		createEReference(featureModelEClass, FEATURE_MODEL__CROSS_TREE_CONSTRAINTS);

		featureEClass = createEClass(FEATURE);
		createEAttribute(featureEClass, FEATURE__NAME);
		createEReference(featureEClass, FEATURE__OWNED_FEATURES);
		createEReference(featureEClass, FEATURE__PARENT_FEATURE);
		createEAttribute(featureEClass, FEATURE__OPTIONAL);
		createEAttribute(featureEClass, FEATURE__SELECTED);
		createEReference(featureEClass, FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS);

		qualityAttributeEClass = createEClass(QUALITY_ATTRIBUTE);
		createEAttribute(qualityAttributeEClass, QUALITY_ATTRIBUTE__NAME);
		createEAttribute(qualityAttributeEClass, QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE);
		createEReference(qualityAttributeEClass, QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS);

		qualityAttributeAnnotationEClass = createEClass(QUALITY_ATTRIBUTE_ANNOTATION);
		createEAttribute(qualityAttributeAnnotationEClass, QUALITY_ATTRIBUTE_ANNOTATION__VALUE);
		createEReference(qualityAttributeAnnotationEClass, QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE);
		createEReference(qualityAttributeAnnotationEClass, QUALITY_ATTRIBUTE_ANNOTATION__FEATURES);

		crossTreeConstraintEClass = createEClass(CROSS_TREE_CONSTRAINT);
		createEReference(crossTreeConstraintEClass, CROSS_TREE_CONSTRAINT__LEFT_FEATURE);
		createEReference(crossTreeConstraintEClass, CROSS_TREE_CONSTRAINT__RIGHT_FEATURE);
		createEAttribute(crossTreeConstraintEClass, CROSS_TREE_CONSTRAINT__TYPE);

		groupFeatureEClass = createEClass(GROUP_FEATURE);
		createEAttribute(groupFeatureEClass, GROUP_FEATURE__CHILD_MIN_CARDINALITY);
		createEAttribute(groupFeatureEClass, GROUP_FEATURE__CHILD_MAX_CARDINALITY);

		qualityAttributesModelEClass = createEClass(QUALITY_ATTRIBUTES_MODEL);
		createEReference(qualityAttributesModelEClass, QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES);
		createEReference(qualityAttributesModelEClass, QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS);

		// Create enums
		crossTreeConstraintTypeEEnum = createEEnum(CROSS_TREE_CONSTRAINT_TYPE);
		optimizationTypeEEnum = createEEnum(OPTIMIZATION_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		groupFeatureEClass.getESuperTypes().add(this.getFeature());

		// Initialize classes, features, and operations; add parameters
		initEClass(featureModelEClass, FeatureModel.class, "FeatureModel", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFeatureModel_OwnedRoot(), this.getFeature(), null, "ownedRoot", null, 0, 1,
				FeatureModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, FeatureModel.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureModel_OwnedQualityAttributeModel(), this.getQualityAttributesModel(), null,
				"ownedQualityAttributeModel", null, 0, 1, FeatureModel.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeatureModel_CrossTreeConstraints(), this.getCrossTreeConstraint(), null,
				"crossTreeConstraints", null, 0, -1, FeatureModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(featureEClass, Feature.class, "Feature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeature_Name(), ecorePackage.getEString(), "name", null, 0, 1, Feature.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeature_OwnedFeatures(), this.getFeature(), this.getFeature_ParentFeature(), "ownedFeatures",
				null, 0, -1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeature_ParentFeature(), this.getFeature(), this.getFeature_OwnedFeatures(), "parentFeature",
				null, 0, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeature_Optional(), ecorePackage.getEBoolean(), "optional", "true", 0, 1, Feature.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeature_Selected(), ecorePackage.getEBoolean(), "selected", null, 0, 1, Feature.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFeature_QualityAttributeAnnotations(), this.getQualityAttributeAnnotation(),
				this.getQualityAttributeAnnotation_Features(), "qualityAttributeAnnotations", null, 0, -1,
				Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(qualityAttributeEClass, QualityAttribute.class, "QualityAttribute", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQualityAttribute_Name(), ecorePackage.getEString(), "name", null, 0, 1,
				QualityAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getQualityAttribute_OptimizationType(), this.getOptimizationType(), "optimizationType", null, 0,
				1, QualityAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getQualityAttribute_QualityAttributeAnnotations(), this.getQualityAttributeAnnotation(),
				this.getQualityAttributeAnnotation_QualityAttribute(), "qualityAttributeAnnotations", null, 0, -1,
				QualityAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(qualityAttributeAnnotationEClass, QualityAttributeAnnotation.class, "QualityAttributeAnnotation",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQualityAttributeAnnotation_Value(), ecorePackage.getEDouble(), "value", null, 0, 1,
				QualityAttributeAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getQualityAttributeAnnotation_QualityAttribute(), this.getQualityAttribute(),
				this.getQualityAttribute_QualityAttributeAnnotations(), "qualityAttribute", null, 0, 1,
				QualityAttributeAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getQualityAttributeAnnotation_Features(), this.getFeature(),
				this.getFeature_QualityAttributeAnnotations(), "features", null, 0, -1,
				QualityAttributeAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(crossTreeConstraintEClass, CrossTreeConstraint.class, "CrossTreeConstraint", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCrossTreeConstraint_LeftFeature(), this.getFeature(), null, "leftFeature", null, 0, 1,
				CrossTreeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCrossTreeConstraint_RightFeature(), this.getFeature(), null, "rightFeature", null, 0, 1,
				CrossTreeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE,
				IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCrossTreeConstraint_Type(), this.getCrossTreeConstraintType(), "type", null, 0, 1,
				CrossTreeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
				IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(groupFeatureEClass, GroupFeature.class, "GroupFeature", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGroupFeature_ChildMinCardinality(), ecorePackage.getEInt(), "childMinCardinality", null, 0, 1,
				GroupFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getGroupFeature_ChildMaxCardinality(), ecorePackage.getEInt(), "childMaxCardinality", null, 0, 1,
				GroupFeature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);

		initEClass(qualityAttributesModelEClass, QualityAttributesModel.class, "QualityAttributesModel", !IS_ABSTRACT,
				!IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getQualityAttributesModel_OwnedQualityAttributes(), this.getQualityAttribute(), null,
				"ownedQualityAttributes", null, 0, -1, QualityAttributesModel.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getQualityAttributesModel_OwnedQualityAttributeAnnotations(),
				this.getQualityAttributeAnnotation(), null, "ownedQualityAttributeAnnotations", null, 0, -1,
				QualityAttributesModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
				!IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(crossTreeConstraintTypeEEnum, CrossTreeConstraintType.class, "CrossTreeConstraintType");
		addEEnumLiteral(crossTreeConstraintTypeEEnum, CrossTreeConstraintType.REQUIRES);
		addEEnumLiteral(crossTreeConstraintTypeEEnum, CrossTreeConstraintType.EXCLUDES);

		initEEnum(optimizationTypeEEnum, OptimizationType.class, "OptimizationType");
		addEEnumLiteral(optimizationTypeEEnum, OptimizationType.MAXIMIZE);
		addEEnumLiteral(optimizationTypeEEnum, OptimizationType.MINIMIZE);

		// Create resource
		createResource(eNS_URI);
	}

} //MDEOptimiser4EFMPackageImpl
