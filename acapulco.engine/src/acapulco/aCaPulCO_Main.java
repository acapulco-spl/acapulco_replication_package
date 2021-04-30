package acapulco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

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

public class aCaPulCO_Main extends AbstractExecutor {

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
		String rules = fm + ".cpcos";
		Map<String, Integer> featureName2index = readFeatureNameMapFromFile(fm);
		List<ConfigurationSearchOperator> operators = readOperatorsFromDirectory(rules, featureName2index);
		run(fm, sc, sv, debug, operators);
	}

	public void run(String fm, StoppingCondition sc, Integer sv, boolean debug,
			List<ConfigurationSearchOperator> operators) {
		String augment = fm + ".augment";
		String dead = fm + ".dead";
		String mandatory = fm + ".mandatory";
		String seed = fm + ".richseed";

		Problem p = null;
		Algorithm a = null;
		SolutionSet pop = null;
		try {
			aCaPulCO_Mutation.DEBUG_MODE = debug;
			p = new aCaPulCO_Problem(fm, augment, mandatory, dead, seed);
			ToolInstrumenter toolInstrumenter = new ToolInstrumenter(p.getNumberOfObjectives(),
					p.getNumberOfConstraints(), "ACAPULCO", "acapulco-results", 1);
			a = new aCaPulCO_SettingsIBEA(p).configure(toolInstrumenter, sc, sv, fm,
					((aCaPulCO_Problem) p).getNumFeatures(), ((aCaPulCO_Problem) p).getConstraints(), operators);
			pop = a.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("******* END OF RUN! SOLUTIONS: ***");

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

	private Map<String, Integer> readFeatureNameMapFromFile(String fm) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		BufferedReader objReader = null;
		try {
			String line;
			objReader = new BufferedReader(new FileReader(fm));
			boolean done = false;
			while (!done && (line = objReader.readLine()) != null) {
				if (line.startsWith("c")) {
					String[] lineSplit = line.split(" ");
					result.put(lineSplit[2], Integer.parseInt(lineSplit[1]));
				} else {
					done = true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objReader != null)
					objReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public List<ConfigurationSearchOperator> readOperatorsFromDirectory(String rulesPath,
			Map<String, Integer> featureName2index) {
		List<ConfigurationSearchOperator> result = new ArrayList<>();
		File rulesDirectory = new File(rulesPath);
		for (File f : rulesDirectory.listFiles()) {
			if (f.getName().endsWith(".hen")) {
				ConfigurationSearchOperator operator = HenshinFileReader
						.readConfigurationSearchOperatorFromFile(f.getPath(), featureName2index);
				result.add(operator);
			}
		}
		return result;
	}

}
