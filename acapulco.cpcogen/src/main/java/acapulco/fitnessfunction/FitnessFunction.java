//package acapulco.fitnessfunction;
//
//import java.io.IOException;
//import java.util.Iterator;
//
//import org.eclipse.emf.ecore.EAttribute;
//import org.eclipse.emf.ecore.EObject;
//
//import mdeo4efm.fitnessfunction.qas.QAsHelper;
//import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction;
//import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution;
//
//public abstract class FitnessFunction implements IGuidanceFunction {
//	private static QAsHelper qasHelper;
//	
//	static {
//		try {
//			qasHelper = new QAsHelper("QAs-MobileMedia-real.obj");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	public double computeFitness(Solution model) {
//		//System.out.println("QA: " + getName());
//		//System.out.println("QAs: " + qasHelper.getAttributes());
//		//System.out.println(model.getTransformationsChain());
//		
//		double total = 0.0;
//		
//		EObject fm = model.getModel();
//		String rootName = fm.eClass().getName();
//		EAttribute selectedAttribute = fm.eClass().getEAllAttributes().get(0);
//		boolean selected = (boolean) fm.eGet(selectedAttribute);
//		
//		//System.out.println("Feature root: " + rootName);
//		total += qasHelper.getQAValue(fm.eClass().getName(), getName());
//		//System.out.println(rootName + " -> " + total);
//		
//		Iterator<EObject> i = fm.eAllContents();
//		while (i.hasNext()) {
//			EObject feature = i.next();
//			selected = isSelected(feature);
//		
//			if (selected) {
//				double value = qasHelper.getQAValue(feature.eClass().getName(), getName());
//				//System.out.println(feature.eClass().getName() + " -> " + value);
//				total += value;	
//			}
//		}
//		return getValue(total);	
//	}
//
//	private boolean isSelected(EObject feature) {
//		EAttribute selectedAttribute = feature.eClass().getEAllAttributes().get(0);
//		boolean selected = (boolean) feature.eGet(selectedAttribute);
//		
//		return selected;
//	}
//	
//	abstract double getValue(double v);
//	
//	
//}
