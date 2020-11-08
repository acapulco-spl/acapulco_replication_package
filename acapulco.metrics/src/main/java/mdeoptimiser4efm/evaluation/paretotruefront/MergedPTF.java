package mdeoptimiser4efm.evaluation.paretotruefront;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generate the pareto true front from the results of all tools considering the pareto fronts from all runs.
 *
 */
public class MergedPTF {
	static String[] tools1 = { "ACAPULCO", "MODAGAME", "SATIBEA" };
	static String[] tools2 = { "Acapulco",  "Modagame", "Satibea" };
	//static String[] cases = { "Wget", "TankWar", "mobile_media2", "WeaFQAs", "linuxbase", "busyBox", "embtoolkit", "cdl_ea2468", "linuxdist", "linux26", "automotive2_1"
	//static String[] cases = { "Wget", "TankWar", "mobile_media2", "WeaFQAs", "linuxbase", "busyBox", "embtoolkit", "cdl_ea2468", "linuxdist"
	static String[] cases = { "linux"
	};

	private static String fileFormatIn(String tool1, String tool2, String _case) {
		return "data\\" + tool1 + "\\" + tool2 + "_" + _case + "_30runs_results.dat";
	}

	private static String fileFormatOut(String _case) {
		return "output" + _case;
	}

	public static void main(String[] args) throws Exception {
		for (String _case : cases) {
			System.out.println("# ** Results from case: " + _case + " **");
			Map<String, List<String>> pfMap = new HashMap<>();
			for (int i = 0; i < tools1.length; i++) {
				fillMaps(_case, pfMap, i);
			}
			
			buildMergePTF(_case, pfMap);
		}
	}
	
	/***
	 * Fill the data structure for a particular tool and case study, to build the pareto true front.
	 * 
	 * @param _case
	 * @param pfMap
	 * @param i
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void fillMaps(String _case, Map<String, List<String>> pfMap, int i) throws FileNotFoundException, IOException {
		String tool = tools1[i];
		List<String> pfs = new ArrayList<>();
		pfMap.put(tool, pfs);
		String path = fileFormatIn(tool, tools2[i], _case);
		BufferedReader br = new BufferedReader(new FileReader(path));

		try {
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				String pf = line.substring(line.indexOf("\"")+1, line.length()-1);
				//System.out.println(pf);
				//String[] lineSplit = line.split(",");
				//pfs.add(lineSplit[6]);
				pfs.add(pf);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
	}
	
	/**
	 * Build the pareto TRUE front from all pareto fronts taken from all runs.
	 * 
	 * @param _case
	 * @param pfMap
	 * @throws Exception
	 */
	private static void buildMergePTF(String _case, Map<String, List<String>> pfMap) throws Exception {
		Set<Set<Point>> paretoFronts = new HashSet<Set<Point>>();
		for (List<String> runs : pfMap.values()) {
			for (String pf : runs) {
				Set<Point> ptfPoints = new HashSet<Point>();
				double[][] ptf = solutionSetToArray(pf);
				if (ptf != null) {
					for (double[] p : ptf) {
						ptfPoints.add(new Point(p));
					}	
				}
				
				paretoFronts.add(ptfPoints);
			}
		}
		
		Set<Point> ptf = createPTF(paretoFronts);
		
		
		// check validity
		for (Set<Point> pf : paretoFronts) {
			if (!checkIfParetoTrueFrontValid(pf, ptf)) {
				throw new Exception("pareto true front not valid!");
			}
		}
		
		// Write ptf
		String ptfRepresentation = getParetoFrontRepresentation(ptf);
		
		Path path = Paths.get(_case + "-paretotruefront.txt");
		Files.deleteIfExists(path);
		Files.createFile(path);
		byte[] strToBytes = ptfRepresentation.getBytes();
	    Files.write(path, strToBytes);
	}
	
	/**
	 * Create the pareto TRUE front from a set of pareto front.
	 * 
	 * @param paretoFronts
	 * @return
	 */
	private static Set<Point> createPTF(Set<Set<Point>> paretoFronts) {
		Set<Point> result = new HashSet<Point>();
		paretoFronts.forEach(p -> result.addAll(p));
		
		Set<Point> remove = new HashSet<Point>();
		for (Point i : result) {
			for (Point j : result) {
				if (!i.equals(j) && dominates(i.getPoint(), j.getPoint())) {
					remove.add(j);
				}
			}
		}
		result.removeAll(remove);
		return result;
	}
	
	/**
	 * P1 dominates P2.
	 * 
	 * @param p1
	 * @param p2
	 * @return		True if P1 dominates P2.
	 */
	private static boolean dominates(double[] p1, double[] p2) {
		if (Arrays.equals(p1, p2)) {
			return false;
		}
	           
		boolean dominates = true;
		for (int i = 0; i < p1.length; i++) {
			double v1 = p1[i];
			double v2 = p2[i];
			if (v1 > v2) {
				dominates = false;
			}
			
		}
		return dominates;
	}
	
	/**
	 * Given a pareto front and a pareto true front, check if all solutions in the pareto true front completely dominates the solutions of the pareto front.
	 *  
	 * @param paretoFront
	 * @param paretoTrueFront
	 * @return	True if the pareto true front is valid (i.e., it dominates the pareto front).
	 */
	public static  boolean checkIfParetoTrueFrontValid(Set<Point> paretoFront, Set<Point> paretoTrueFront) {
		for (Point ptfP : paretoTrueFront) {
			for (Point pfP : paretoFront) {
				if (dominates(pfP.getPoint(), ptfP.getPoint())) {
					return false;
				}
			}
		}
		return true;
	} 
	
	/**
	 * Convert a pareto front from a Point-object based representation to a string representation.
	 * @param pf
	 * @return
	 */
	private static String getParetoFrontRepresentation(Set<Point> pf) {
		String sPF = "[]";
		if (pf != null) {
			sPF = "[";
			for (Point p : pf) {
				sPF += "[";	
				for (int c = 0; c < p.getPoint().length; c++) {
					sPF += p.getPoint()[c] + ",";
				}
				sPF = sPF.substring(0, sPF.length()-1) + "], ";
			}
			if (sPF.length() > 1) {
				sPF = sPF.substring(0, sPF.length()-2) + "]";	
			} else {
				sPF += "]";
			}
		}
		return sPF;
	}
	
	/**
	 * Convert a pareto front from an array representation to a string representation.
	 * 
	 * @param pf
	 * @return
	 */
	public static String getParetoFrontRepresentation(double[][] pf) {
		String sPF = "\"[]\"";
		if (pf != null) {
			sPF = "\"[";
			for (int r = 0; r < pf.length; r++) {
				sPF += "[";	
				for (int c = 0; c < pf[r].length; c++) {
					sPF += pf[r][c] + ",";
				}
				sPF = sPF.substring(0, sPF.length()-1) + "],";
			}
			sPF = sPF.substring(0, sPF.length()-1) + "]\"";
		}
		return sPF;
	}
	
	/**
	 * Convert a pareto front from a string representation to an array representation.
	 * @param solutionSet
	 * @return
	 */
	private static double[][] solutionSetToArray(String solutionSet) {
		if (solutionSet.equals("[]")) {
			return null;
		}
		String[] sols = null;
		if (!solutionSet.contains(", ")) {
			solutionSet = solutionSet.replaceAll(",\\[", ", \\[");
		}
		sols = solutionSet.split(", ");
		//String[] sols = solutionSet.split(", ");
		double[][] results = new double[sols.length][];
		for (int s = 0; s < sols.length; s++) { 
			String[] values = sols[s].split(",");
			results[s] = new double[values.length];
			for (int v = 0; v < values.length; v++) {
				String val = values[v].replaceAll("\\[", "").replaceAll("\\]", "");
				results[s][v] = Double.parseDouble(val);
			}
		}
		return results;
	}
	
	
	
}
