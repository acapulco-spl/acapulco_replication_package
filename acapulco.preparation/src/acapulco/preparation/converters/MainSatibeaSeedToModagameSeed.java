package acapulco.preparation.converters;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainSatibeaSeedToModagameSeed {

	public static void main(String[] args) {
		String name, inPath, outPath;

		if (args.length == 0) {
			name = "automotive2_1.sxfm.xml-nocomplex.sxfm.xml.clean.sxfm";
			inPath = "satibea/";
			outPath = "modagame/";
		} else {
			name = args[0];
			inPath = args[1];
			outPath = args[2];
		}

		
		// Inputs
		File fmFile = new File(inPath + name + ".dimacs");
		File fmSeedFile = new File(inPath + name + ".dimacs.richseed");

		// Output
		File outputSeedFile = new File(outPath + name + ".csv");

		Map<String, String> dimacsNumberToF = new HashMap<String, String>();
		for (String line : Utils.getLinesOfFile(fmFile)) {
			if (line.startsWith("p cnf")) {
				break;
			}
			String[] lineSplit = line.split(" ");
			dimacsNumberToF.put(lineSplit[1], lineSplit[2]);
		}

		String dimacsSeed = Utils.getStringOfFile(fmSeedFile);

		StringBuffer csv = new StringBuffer();
		String[] split = dimacsSeed.split(" ");
		for (String s : split) {
			
			if (!s.trim().startsWith("-")) {
				if (dimacsNumberToF.get(s.trim()).isBlank())
					System.out.println("ha");
				
				csv.append(dimacsNumberToF.get(s.trim()));
				csv.append("\n");
			}
		}

		Utils.writeStringToFile(outputSeedFile, csv.toString());
	}
}
