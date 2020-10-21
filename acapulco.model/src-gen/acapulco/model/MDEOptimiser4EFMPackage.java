/**
 */
package acapulco.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see acapulco.model.MDEOptimiser4EFMFactory
 * @model kind="package"
 * @generated
 */
public interface MDEOptimiser4EFMPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "mdeoptimiser4efm";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/mdeoptimiser4efm";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "mdeoptimiser4efm";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MDEOptimiser4EFMPackage eINSTANCE = acapulco.model.impl.MDEOptimiser4EFMPackageImpl.init();

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.FeatureModelImpl <em>Feature Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.FeatureModelImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getFeatureModel()
	 * @generated
	 */
	int FEATURE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Owned Root</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__OWNED_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__NAME = 1;

	/**
	 * The feature id for the '<em><b>Owned Quality Attribute Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Cross Tree Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL__CROSS_TREE_CONSTRAINTS = 3;

	/**
	 * The number of structural features of the '<em>Feature Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Feature Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.FeatureImpl <em>Feature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.FeatureImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getFeature()
	 * @generated
	 */
	int FEATURE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Owned Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__OWNED_FEATURES = 1;

	/**
	 * The feature id for the '<em><b>Parent Feature</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__PARENT_FEATURE = 2;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__OPTIONAL = 3;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__SELECTED = 4;

	/**
	 * The feature id for the '<em><b>Quality Attribute Annotations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS = 5;

	/**
	 * The number of structural features of the '<em>Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.QualityAttributeImpl <em>Quality Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.QualityAttributeImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getQualityAttribute()
	 * @generated
	 */
	int QUALITY_ATTRIBUTE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Optimization Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Quality Attribute Annotations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS = 2;

	/**
	 * The number of structural features of the '<em>Quality Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Quality Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl <em>Quality Attribute Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getQualityAttributeAnnotation()
	 * @generated
	 */
	int QUALITY_ATTRIBUTE_ANNOTATION = 3;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_ANNOTATION__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Quality Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE = 1;

	/**
	 * The feature id for the '<em><b>Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_ANNOTATION__FEATURES = 2;

	/**
	 * The number of structural features of the '<em>Quality Attribute Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_ANNOTATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Quality Attribute Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTE_ANNOTATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.CrossTreeConstraintImpl <em>Cross Tree Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.CrossTreeConstraintImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getCrossTreeConstraint()
	 * @generated
	 */
	int CROSS_TREE_CONSTRAINT = 4;

	/**
	 * The feature id for the '<em><b>Left Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CROSS_TREE_CONSTRAINT__LEFT_FEATURE = 0;

	/**
	 * The feature id for the '<em><b>Right Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CROSS_TREE_CONSTRAINT__RIGHT_FEATURE = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CROSS_TREE_CONSTRAINT__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Cross Tree Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CROSS_TREE_CONSTRAINT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Cross Tree Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CROSS_TREE_CONSTRAINT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.GroupFeatureImpl <em>Group Feature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.GroupFeatureImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getGroupFeature()
	 * @generated
	 */
	int GROUP_FEATURE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__NAME = FEATURE__NAME;

	/**
	 * The feature id for the '<em><b>Owned Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__OWNED_FEATURES = FEATURE__OWNED_FEATURES;

	/**
	 * The feature id for the '<em><b>Parent Feature</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__PARENT_FEATURE = FEATURE__PARENT_FEATURE;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__OPTIONAL = FEATURE__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Selected</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__SELECTED = FEATURE__SELECTED;

	/**
	 * The feature id for the '<em><b>Quality Attribute Annotations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS = FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Child Min Cardinality</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__CHILD_MIN_CARDINALITY = FEATURE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Child Max Cardinality</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE__CHILD_MAX_CARDINALITY = FEATURE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Group Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_FEATURE_COUNT = FEATURE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Group Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROUP_FEATURE_OPERATION_COUNT = FEATURE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link mdeoptimiser4efm.impl.QualityAttributesModelImpl <em>Quality Attributes Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see mdeoptimiser4efm.impl.QualityAttributesModelImpl
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getQualityAttributesModel()
	 * @generated
	 */
	int QUALITY_ATTRIBUTES_MODEL = 6;

	/**
	 * The feature id for the '<em><b>Owned Quality Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES = 0;

	/**
	 * The feature id for the '<em><b>Owned Quality Attribute Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS = 1;

	/**
	 * The number of structural features of the '<em>Quality Attributes Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTES_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Quality Attributes Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUALITY_ATTRIBUTES_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link acapulco.model.CrossTreeConstraintType <em>Cross Tree Constraint Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see acapulco.model.CrossTreeConstraintType
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getCrossTreeConstraintType()
	 * @generated
	 */
	int CROSS_TREE_CONSTRAINT_TYPE = 7;

	/**
	 * The meta object id for the '{@link acapulco.model.OptimizationType <em>Optimization Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see acapulco.model.OptimizationType
	 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getOptimizationType()
	 * @generated
	 */
	int OPTIMIZATION_TYPE = 8;

	/**
	 * Returns the meta object for class '{@link acapulco.model.FeatureModel <em>Feature Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Model</em>'.
	 * @see acapulco.model.FeatureModel
	 * @generated
	 */
	EClass getFeatureModel();

	/**
	 * Returns the meta object for the containment reference '{@link acapulco.model.FeatureModel#getOwnedRoot <em>Owned Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Root</em>'.
	 * @see acapulco.model.FeatureModel#getOwnedRoot()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EReference getFeatureModel_OwnedRoot();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.FeatureModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see acapulco.model.FeatureModel#getName()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EAttribute getFeatureModel_Name();

	/**
	 * Returns the meta object for the containment reference '{@link acapulco.model.FeatureModel#getOwnedQualityAttributeModel <em>Owned Quality Attribute Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Quality Attribute Model</em>'.
	 * @see acapulco.model.FeatureModel#getOwnedQualityAttributeModel()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EReference getFeatureModel_OwnedQualityAttributeModel();

	/**
	 * Returns the meta object for the containment reference list '{@link acapulco.model.FeatureModel#getCrossTreeConstraints <em>Cross Tree Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cross Tree Constraints</em>'.
	 * @see acapulco.model.FeatureModel#getCrossTreeConstraints()
	 * @see #getFeatureModel()
	 * @generated
	 */
	EReference getFeatureModel_CrossTreeConstraints();

	/**
	 * Returns the meta object for class '{@link acapulco.model.Feature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature</em>'.
	 * @see acapulco.model.Feature
	 * @generated
	 */
	EClass getFeature();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.Feature#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see acapulco.model.Feature#getName()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Name();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.Feature#isSelected <em>Selected</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Selected</em>'.
	 * @see acapulco.model.Feature#isSelected()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Selected();

	/**
	 * Returns the meta object for the reference list '{@link acapulco.model.Feature#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Quality Attribute Annotations</em>'.
	 * @see acapulco.model.Feature#getQualityAttributeAnnotations()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_QualityAttributeAnnotations();

	/**
	 * Returns the meta object for the containment reference list '{@link acapulco.model.Feature#getOwnedFeatures <em>Owned Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Features</em>'.
	 * @see acapulco.model.Feature#getOwnedFeatures()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_OwnedFeatures();

	/**
	 * Returns the meta object for the container reference '{@link acapulco.model.Feature#getParentFeature <em>Parent Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent Feature</em>'.
	 * @see acapulco.model.Feature#getParentFeature()
	 * @see #getFeature()
	 * @generated
	 */
	EReference getFeature_ParentFeature();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.Feature#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see acapulco.model.Feature#isOptional()
	 * @see #getFeature()
	 * @generated
	 */
	EAttribute getFeature_Optional();

	/**
	 * Returns the meta object for class '{@link acapulco.model.QualityAttribute <em>Quality Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Quality Attribute</em>'.
	 * @see acapulco.model.QualityAttribute
	 * @generated
	 */
	EClass getQualityAttribute();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.QualityAttribute#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see acapulco.model.QualityAttribute#getName()
	 * @see #getQualityAttribute()
	 * @generated
	 */
	EAttribute getQualityAttribute_Name();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.QualityAttribute#getOptimizationType <em>Optimization Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optimization Type</em>'.
	 * @see acapulco.model.QualityAttribute#getOptimizationType()
	 * @see #getQualityAttribute()
	 * @generated
	 */
	EAttribute getQualityAttribute_OptimizationType();

	/**
	 * Returns the meta object for the reference list '{@link acapulco.model.QualityAttribute#getQualityAttributeAnnotations <em>Quality Attribute Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Quality Attribute Annotations</em>'.
	 * @see acapulco.model.QualityAttribute#getQualityAttributeAnnotations()
	 * @see #getQualityAttribute()
	 * @generated
	 */
	EReference getQualityAttribute_QualityAttributeAnnotations();

	/**
	 * Returns the meta object for class '{@link acapulco.model.QualityAttributeAnnotation <em>Quality Attribute Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Quality Attribute Annotation</em>'.
	 * @see acapulco.model.QualityAttributeAnnotation
	 * @generated
	 */
	EClass getQualityAttributeAnnotation();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.QualityAttributeAnnotation#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see acapulco.model.QualityAttributeAnnotation#getValue()
	 * @see #getQualityAttributeAnnotation()
	 * @generated
	 */
	EAttribute getQualityAttributeAnnotation_Value();

	/**
	 * Returns the meta object for the reference '{@link acapulco.model.QualityAttributeAnnotation#getQualityAttribute <em>Quality Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Quality Attribute</em>'.
	 * @see acapulco.model.QualityAttributeAnnotation#getQualityAttribute()
	 * @see #getQualityAttributeAnnotation()
	 * @generated
	 */
	EReference getQualityAttributeAnnotation_QualityAttribute();

	/**
	 * Returns the meta object for the reference list '{@link acapulco.model.QualityAttributeAnnotation#getFeatures <em>Features</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Features</em>'.
	 * @see acapulco.model.QualityAttributeAnnotation#getFeatures()
	 * @see #getQualityAttributeAnnotation()
	 * @generated
	 */
	EReference getQualityAttributeAnnotation_Features();

	/**
	 * Returns the meta object for class '{@link acapulco.model.CrossTreeConstraint <em>Cross Tree Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cross Tree Constraint</em>'.
	 * @see acapulco.model.CrossTreeConstraint
	 * @generated
	 */
	EClass getCrossTreeConstraint();

	/**
	 * Returns the meta object for the reference '{@link acapulco.model.CrossTreeConstraint#getLeftFeature <em>Left Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Left Feature</em>'.
	 * @see acapulco.model.CrossTreeConstraint#getLeftFeature()
	 * @see #getCrossTreeConstraint()
	 * @generated
	 */
	EReference getCrossTreeConstraint_LeftFeature();

	/**
	 * Returns the meta object for the reference '{@link acapulco.model.CrossTreeConstraint#getRightFeature <em>Right Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Right Feature</em>'.
	 * @see acapulco.model.CrossTreeConstraint#getRightFeature()
	 * @see #getCrossTreeConstraint()
	 * @generated
	 */
	EReference getCrossTreeConstraint_RightFeature();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.CrossTreeConstraint#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see acapulco.model.CrossTreeConstraint#getType()
	 * @see #getCrossTreeConstraint()
	 * @generated
	 */
	EAttribute getCrossTreeConstraint_Type();

	/**
	 * Returns the meta object for class '{@link acapulco.model.GroupFeature <em>Group Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group Feature</em>'.
	 * @see acapulco.model.GroupFeature
	 * @generated
	 */
	EClass getGroupFeature();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.GroupFeature#getChildMinCardinality <em>Child Min Cardinality</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Min Cardinality</em>'.
	 * @see acapulco.model.GroupFeature#getChildMinCardinality()
	 * @see #getGroupFeature()
	 * @generated
	 */
	EAttribute getGroupFeature_ChildMinCardinality();

	/**
	 * Returns the meta object for the attribute '{@link acapulco.model.GroupFeature#getChildMaxCardinality <em>Child Max Cardinality</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Max Cardinality</em>'.
	 * @see acapulco.model.GroupFeature#getChildMaxCardinality()
	 * @see #getGroupFeature()
	 * @generated
	 */
	EAttribute getGroupFeature_ChildMaxCardinality();

	/**
	 * Returns the meta object for class '{@link acapulco.model.QualityAttributesModel <em>Quality Attributes Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Quality Attributes Model</em>'.
	 * @see acapulco.model.QualityAttributesModel
	 * @generated
	 */
	EClass getQualityAttributesModel();

	/**
	 * Returns the meta object for the containment reference list '{@link acapulco.model.QualityAttributesModel#getOwnedQualityAttributes <em>Owned Quality Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Quality Attributes</em>'.
	 * @see acapulco.model.QualityAttributesModel#getOwnedQualityAttributes()
	 * @see #getQualityAttributesModel()
	 * @generated
	 */
	EReference getQualityAttributesModel_OwnedQualityAttributes();

	/**
	 * Returns the meta object for the containment reference list '{@link acapulco.model.QualityAttributesModel#getOwnedQualityAttributeAnnotations <em>Owned Quality Attribute Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Quality Attribute Annotations</em>'.
	 * @see acapulco.model.QualityAttributesModel#getOwnedQualityAttributeAnnotations()
	 * @see #getQualityAttributesModel()
	 * @generated
	 */
	EReference getQualityAttributesModel_OwnedQualityAttributeAnnotations();

	/**
	 * Returns the meta object for enum '{@link acapulco.model.CrossTreeConstraintType <em>Cross Tree Constraint Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Cross Tree Constraint Type</em>'.
	 * @see acapulco.model.CrossTreeConstraintType
	 * @generated
	 */
	EEnum getCrossTreeConstraintType();

	/**
	 * Returns the meta object for enum '{@link acapulco.model.OptimizationType <em>Optimization Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Optimization Type</em>'.
	 * @see acapulco.model.OptimizationType
	 * @generated
	 */
	EEnum getOptimizationType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MDEOptimiser4EFMFactory getMDEOptimiser4EFMFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.FeatureModelImpl <em>Feature Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.FeatureModelImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getFeatureModel()
		 * @generated
		 */
		EClass FEATURE_MODEL = eINSTANCE.getFeatureModel();

		/**
		 * The meta object literal for the '<em><b>Owned Root</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MODEL__OWNED_ROOT = eINSTANCE.getFeatureModel_OwnedRoot();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_MODEL__NAME = eINSTANCE.getFeatureModel_Name();

		/**
		 * The meta object literal for the '<em><b>Owned Quality Attribute Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MODEL__OWNED_QUALITY_ATTRIBUTE_MODEL = eINSTANCE
				.getFeatureModel_OwnedQualityAttributeModel();

		/**
		 * The meta object literal for the '<em><b>Cross Tree Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_MODEL__CROSS_TREE_CONSTRAINTS = eINSTANCE.getFeatureModel_CrossTreeConstraints();

		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.FeatureImpl <em>Feature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.FeatureImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getFeature()
		 * @generated
		 */
		EClass FEATURE = eINSTANCE.getFeature();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__NAME = eINSTANCE.getFeature_Name();

		/**
		 * The meta object literal for the '<em><b>Selected</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__SELECTED = eINSTANCE.getFeature_Selected();

		/**
		 * The meta object literal for the '<em><b>Quality Attribute Annotations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE__QUALITY_ATTRIBUTE_ANNOTATIONS = eINSTANCE.getFeature_QualityAttributeAnnotations();

		/**
		 * The meta object literal for the '<em><b>Owned Features</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE__OWNED_FEATURES = eINSTANCE.getFeature_OwnedFeatures();

		/**
		 * The meta object literal for the '<em><b>Parent Feature</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE__PARENT_FEATURE = eINSTANCE.getFeature_ParentFeature();

		/**
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE__OPTIONAL = eINSTANCE.getFeature_Optional();

		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.QualityAttributeImpl <em>Quality Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.QualityAttributeImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getQualityAttribute()
		 * @generated
		 */
		EClass QUALITY_ATTRIBUTE = eINSTANCE.getQualityAttribute();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute QUALITY_ATTRIBUTE__NAME = eINSTANCE.getQualityAttribute_Name();

		/**
		 * The meta object literal for the '<em><b>Optimization Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute QUALITY_ATTRIBUTE__OPTIMIZATION_TYPE = eINSTANCE.getQualityAttribute_OptimizationType();

		/**
		 * The meta object literal for the '<em><b>Quality Attribute Annotations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUALITY_ATTRIBUTE__QUALITY_ATTRIBUTE_ANNOTATIONS = eINSTANCE
				.getQualityAttribute_QualityAttributeAnnotations();

		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl <em>Quality Attribute Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.QualityAttributeAnnotationImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getQualityAttributeAnnotation()
		 * @generated
		 */
		EClass QUALITY_ATTRIBUTE_ANNOTATION = eINSTANCE.getQualityAttributeAnnotation();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute QUALITY_ATTRIBUTE_ANNOTATION__VALUE = eINSTANCE.getQualityAttributeAnnotation_Value();

		/**
		 * The meta object literal for the '<em><b>Quality Attribute</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUALITY_ATTRIBUTE_ANNOTATION__QUALITY_ATTRIBUTE = eINSTANCE
				.getQualityAttributeAnnotation_QualityAttribute();

		/**
		 * The meta object literal for the '<em><b>Features</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUALITY_ATTRIBUTE_ANNOTATION__FEATURES = eINSTANCE.getQualityAttributeAnnotation_Features();

		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.CrossTreeConstraintImpl <em>Cross Tree Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.CrossTreeConstraintImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getCrossTreeConstraint()
		 * @generated
		 */
		EClass CROSS_TREE_CONSTRAINT = eINSTANCE.getCrossTreeConstraint();

		/**
		 * The meta object literal for the '<em><b>Left Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CROSS_TREE_CONSTRAINT__LEFT_FEATURE = eINSTANCE.getCrossTreeConstraint_LeftFeature();

		/**
		 * The meta object literal for the '<em><b>Right Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CROSS_TREE_CONSTRAINT__RIGHT_FEATURE = eINSTANCE.getCrossTreeConstraint_RightFeature();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CROSS_TREE_CONSTRAINT__TYPE = eINSTANCE.getCrossTreeConstraint_Type();

		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.GroupFeatureImpl <em>Group Feature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.GroupFeatureImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getGroupFeature()
		 * @generated
		 */
		EClass GROUP_FEATURE = eINSTANCE.getGroupFeature();

		/**
		 * The meta object literal for the '<em><b>Child Min Cardinality</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP_FEATURE__CHILD_MIN_CARDINALITY = eINSTANCE.getGroupFeature_ChildMinCardinality();

		/**
		 * The meta object literal for the '<em><b>Child Max Cardinality</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROUP_FEATURE__CHILD_MAX_CARDINALITY = eINSTANCE.getGroupFeature_ChildMaxCardinality();

		/**
		 * The meta object literal for the '{@link mdeoptimiser4efm.impl.QualityAttributesModelImpl <em>Quality Attributes Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see mdeoptimiser4efm.impl.QualityAttributesModelImpl
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getQualityAttributesModel()
		 * @generated
		 */
		EClass QUALITY_ATTRIBUTES_MODEL = eINSTANCE.getQualityAttributesModel();

		/**
		 * The meta object literal for the '<em><b>Owned Quality Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTES = eINSTANCE
				.getQualityAttributesModel_OwnedQualityAttributes();

		/**
		 * The meta object literal for the '<em><b>Owned Quality Attribute Annotations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUALITY_ATTRIBUTES_MODEL__OWNED_QUALITY_ATTRIBUTE_ANNOTATIONS = eINSTANCE
				.getQualityAttributesModel_OwnedQualityAttributeAnnotations();

		/**
		 * The meta object literal for the '{@link acapulco.model.CrossTreeConstraintType <em>Cross Tree Constraint Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see acapulco.model.CrossTreeConstraintType
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getCrossTreeConstraintType()
		 * @generated
		 */
		EEnum CROSS_TREE_CONSTRAINT_TYPE = eINSTANCE.getCrossTreeConstraintType();

		/**
		 * The meta object literal for the '{@link acapulco.model.OptimizationType <em>Optimization Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see acapulco.model.OptimizationType
		 * @see mdeoptimiser4efm.impl.MDEOptimiser4EFMPackageImpl#getOptimizationType()
		 * @generated
		 */
		EEnum OPTIMIZATION_TYPE = eINSTANCE.getOptimizationType();

	}

} //MDEOptimiser4EFMPackage
