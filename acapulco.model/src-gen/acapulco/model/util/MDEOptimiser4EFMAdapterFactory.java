/**
 */
package acapulco.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import acapulco.model.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see acapulco.model.MDEOptimiser4EFMPackage
 * @generated
 */
public class MDEOptimiser4EFMAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MDEOptimiser4EFMPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDEOptimiser4EFMAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MDEOptimiser4EFMPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MDEOptimiser4EFMSwitch<Adapter> modelSwitch = new MDEOptimiser4EFMSwitch<Adapter>() {
		@Override
		public Adapter caseFeatureModel(FeatureModel object) {
			return createFeatureModelAdapter();
		}

		@Override
		public Adapter caseFeature(Feature object) {
			return createFeatureAdapter();
		}

		@Override
		public Adapter caseQualityAttribute(QualityAttribute object) {
			return createQualityAttributeAdapter();
		}

		@Override
		public Adapter caseQualityAttributeAnnotation(QualityAttributeAnnotation object) {
			return createQualityAttributeAnnotationAdapter();
		}

		@Override
		public Adapter caseCrossTreeConstraint(CrossTreeConstraint object) {
			return createCrossTreeConstraintAdapter();
		}

		@Override
		public Adapter caseGroupFeature(GroupFeature object) {
			return createGroupFeatureAdapter();
		}

		@Override
		public Adapter caseQualityAttributesModel(QualityAttributesModel object) {
			return createQualityAttributesModelAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.FeatureModel <em>Feature Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.FeatureModel
	 * @generated
	 */
	public Adapter createFeatureModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.Feature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.Feature
	 * @generated
	 */
	public Adapter createFeatureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.QualityAttribute <em>Quality Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.QualityAttribute
	 * @generated
	 */
	public Adapter createQualityAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.QualityAttributeAnnotation <em>Quality Attribute Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.QualityAttributeAnnotation
	 * @generated
	 */
	public Adapter createQualityAttributeAnnotationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.CrossTreeConstraint <em>Cross Tree Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.CrossTreeConstraint
	 * @generated
	 */
	public Adapter createCrossTreeConstraintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.GroupFeature <em>Group Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.GroupFeature
	 * @generated
	 */
	public Adapter createGroupFeatureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link acapulco.model.QualityAttributesModel <em>Quality Attributes Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see acapulco.model.QualityAttributesModel
	 * @generated
	 */
	public Adapter createQualityAttributesModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MDEOptimiser4EFMAdapterFactory
