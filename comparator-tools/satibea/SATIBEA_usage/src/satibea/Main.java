package satibea;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.encodings.variable.Binary;
import mdeoptimiser4efm.algorithm.instrumentation.ToolInstrumenter;
import mdeoptimiser4efm.algorithm.termination.StoppingCondition;

public class Main {
	public static void main(String[] args) {
		
		String fm = "../SATIBEA_cases/BerkeleyDBMemory.dimacs";
		
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		runConfiguration(fm, StoppingCondition.TIME, 1000);
		runConfiguration(fm, StoppingCondition.EVOLUTIONS, 10);
	}

	
	public static void runConfiguration(String fm, StoppingCondition stoppingCondition, Integer stoppingValue) {
		String augment = fm + ".augment";
		String dead = fm + ".dead";
		String mandatory = fm + ".mandatory";
		String seed = fm + ".richseed";
		
		try {
			
			Problem p = new SATIBEA_Problem(fm, augment, mandatory, dead, seed);
			ToolInstrumenter toolInstrumenter = new ToolInstrumenter(p.getNumberOfObjectives(), 
					p.getNumberOfConstraints(), "SATIBEA", "satibea-results", 1);
			
			// Algorithm a = new
			// ASE_SettingsIBEA(p).configureASE2013(Integer.parseInt(args[1]));
			// for the new operator:
			Algorithm a = new SATIBEA_SettingsIBEA(p).configureSATIBEA(toolInstrumenter, stoppingCondition, stoppingValue, fm,
					((SATIBEA_Problem) p).getNumFeatures(), ((SATIBEA_Problem) p).getConstraints());

			SolutionSet pop = a.execute();
			
			toolInstrumenter.serialiseAccumulator();
			
			File fmFile = new File(fm);
			List<String> fmString = getLinesOfFile(fmFile);

			// for (int i = 0; i < pop.size(); i++) {
			// Variable v = pop.get(i).getDecisionVariables()[0];
			// Binary binary = (Binary)v;
			// System.out.println("Conf" + (i + 1) + ": " + (Binary) v + " ");
			//
			// System.out.print("[");
			// for(int bi = 0; bi < binary.getNumberOfBits(); bi++) {
			// if(binary.getIth(bi)) {
			// System.out.print(fmString.get(bi).substring(fmString.get(bi).lastIndexOf("
			// "), fmString.get(bi).length()) + ",");
			// }
			// }
			// System.out.println("]");
			// }

			for (int i = 0; i < pop.size(); i++) {
				Variable v = pop.get(i).getDecisionVariables()[0];
				// valid config?
				if (pop.get(i).getObjective(0) == 0.0) {

					Binary binary = (Binary) v;
					System.out.println("Conf" + (i + 1) + ": " + (Binary) v + " ");

					System.out.print("[");
					for (int bi = 0; bi < binary.getNumberOfBits(); bi++) {
						if (binary.getIth(bi)) {
							System.out.print(fmString.get(bi).substring(fmString.get(bi).lastIndexOf(" ") + 1,
									fmString.get(bi).length()) + ",");
						}
					}
					System.out.println("]");
					
					for (int j = 0; j < pop.get(i).getNumberOfObjectives(); j++) {
						System.out.print(pop.get(i).getObjective(j) + " ");
					}
					System.out.println("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get lines of a file
	 * 
	 * @param file
	 * @return list of strings
	 */
	public static List<String> getLinesOfFile(File file) {
		List<String> lines = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				lines.add(strLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

}
