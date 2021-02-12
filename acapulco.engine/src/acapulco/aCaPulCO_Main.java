package acapulco;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;

import acapulco.algorithm.instrumentation.ToolInstrumenter;
import acapulco.algorithm.termination.StoppingCondition;
import acapulco.engine.HenshinFileReader;
import acapulco.engine.variability.ConfigurationSearchOperator;
import acapulco.tool.executor.AbstractExecutor;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.encodings.variable.Binary;
import picocli.CommandLine;

public class aCaPulCO_Main extends AbstractExecutor {

	private static Random r = new Random();
	private static final int SATtimeout = 1000;
	private static final long iteratorTimeout = 150000;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		String fm = "testdata\\linux\\linux.dimacs";
		StoppingCondition sc = StoppingCondition.EVOLUTIONS;
		Integer sv = 50;
		boolean debug = true;

		aCaPulCO_Main main = new aCaPulCO_Main();

		if (args.length > 0) {
			fm = args[0] + ".dimacs";
			sc = args[1].equals("EVOLUTIONS") ? StoppingCondition.EVOLUTIONS : StoppingCondition.TIME;
			sv = Integer.parseInt(args[2]);
			debug = Boolean.parseBoolean(args[3]);
		}

		for (int i = 0; i < 60; i++) {
			System.out.println("Run " + i);
			main.run(fm, sc, sv, debug);
		}

//		int exitCode = new CommandLine(new aCaPulCO_Main()).execute(args);
//		System.exit(exitCode);
	}

	@Override
	public void run() {
		String fm = featureModel;
		StoppingCondition sc = stoppingCondition;
		Integer sv = stoppingValue;
		Boolean debug = debugMode;
		run(fm, sc, sv, debug);
	}

	public void run(String fm, StoppingCondition sc, Integer sv, boolean debug) {
		String augment = fm + ".augment";
		String dead = fm + ".dead";
		String mandatory = fm + ".mandatory";
		String seed = fm + ".richseed";
		String metamodel = fm + ".ecore";
		String rules = fm + ".cpcos";

		EPackage metamodel_ = readMetamodel(metamodel);
		List<ConfigurationSearchOperator> operators = readOperatorsFromDirectory(rules, metamodel_);

		System.out.println("Finished loading rules");
		
		Problem p = null;
		Algorithm a = null;
		SolutionSet pop = null;
		try {
			aCaPulCO_Mutation.DEBUG_MODE = debug;
			p = new aCaPulCO_Problem(fm, augment, mandatory, dead, seed);
			ToolInstrumenter toolInstrumenter = new ToolInstrumenter(p.getNumberOfObjectives(),
					p.getNumberOfConstraints(), "ACAPULCO", "acapulco-results", 1);
			a = new aCaPulCO_SettingsIBEA(p).configure(toolInstrumenter, sc, sv, fm, metamodel, rules,
					((aCaPulCO_Problem) p).getNumFeatures(), ((aCaPulCO_Problem) p).getConstraints(), operators, metamodel_);
			pop = a.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("******* END OF RUN! SOLUTIONS: ***");
//		for (int i = 0; i < pop.size(); i++) {
//			Variable v = pop.get(i).getDecisionVariables()[0];
//			System.out.println("Conf" + (i + 1) + ": " + (Binary) v + " ");
//		}

		for (int i = 0; i < pop.size(); i++) {
			Variable v = pop.get(i).getDecisionVariables()[0];
			for (int j = 0; j < 5; j++) {
				System.out.print(pop.get(i).getObjective(j) + " ");
			}
			System.out.println("");
		}
	}

	public static int numViolatedConstraints(Binary b) {

		// IVecInt v = bitSetToVecInt(b);
		int s = 0;
		for (List<Integer> constraint : aCaPulCO_Problem.constraints) {
			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b.getIth(abs - 1) == sign) {
					sat = true;
					break;
				}
			}
			if (!sat) {
				s++;
			}

		}

		return s;
	}

	public static int numViolatedConstraints(Binary b, HashSet<Integer> blacklist) {

		// IVecInt v = bitSetToVecInt(b);
		int s = 0;
		for (List<Integer> constraint : aCaPulCO_Problem.constraints) {
			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b.getIth(abs - 1) == sign) {
					sat = true;
				} else {
					blacklist.add(abs);
				}
			}
			if (!sat) {
				s++;
			}

		}

		return s;
	}

	public static int numViolatedConstraints(boolean[] b) {

		// IVecInt v = bitSetToVecInt(b);
		int s = 0;
		for (List<Integer> constraint : aCaPulCO_Problem.constraints) {

			boolean sat = false;

			for (Integer i : constraint) {
				int abs = (i < 0) ? -i : i;
				boolean sign = i > 0;
				if (b[abs - 1] == sign) {
					sat = true;
					break;
				}
			}
			if (!sat) {
				s++;
			}

		}

		return s;
	}

	private EPackage readMetamodel(String metamodelPath) {

		ResourceSet resourceSet = new ResourceSetImpl();
		EcorePackage.eINSTANCE.getEBoolean();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
				new EcoreResourceFactoryImpl());
		resourceSet.getResources().add(EcorePackage.eINSTANCE.eResource());
		Registry packageRegistry = resourceSet.getPackageRegistry();
		packageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		EPackage pack = null;
		try {
			File sourceMetamodel = new File(metamodelPath);
			XMIResourceImpl resourceMetamodel = new XMIResourceImpl();
			resourceSet.getResources().add(resourceMetamodel);
			resourceMetamodel.load(new FileInputStream(sourceMetamodel), new HashMap<Object, Object>());
			pack = (EPackage) resourceMetamodel.getContents().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pack;
	}
	private List<ConfigurationSearchOperator> readOperatorsFromDirectory(String rulesPath, EPackage metamodel) {
		List<ConfigurationSearchOperator> result = new ArrayList<>();
		File rulesDirectory = new File(rulesPath);
		for (File f : rulesDirectory.listFiles()) {
			if (f.getName().endsWith(".hen")) {
				ConfigurationSearchOperator operator = HenshinFileReader.readConfigurationSearchOperatorFromFile(f.getPath(), metamodel);
				result.add(operator);
			}
		}
		return result;
	}

}
