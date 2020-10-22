package acapulco.preparation.converters;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.dimacs.DIMACSFormat;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;

public class MainModagameToSatibea {
	public static void main(String[] args) {
		String name, inPath, outPath;
		
		if (args.length == 0) {
			name = "automotive2_1.sxfm.xml-nocomplex.sxfm.xml.clean.sxfm";
			inPath = "modagame/";
			outPath = "satibea/";
		} else {
			name = args[0];
			inPath = args[1];
			outPath = args[2];
		}

		// Inputs
		File fmFile = new File(inPath + name + ".xml");
		File fmObjFile = new File(inPath + name + ".obj");

		// Outputs
		File outputFMFile = new File(outPath + name + ".dimacs");
		File outputObjFile = new File(outPath + name + ".dimacs.augment");
		File outputDeadFile = new File(outPath + name + ".dimacs.dead");
		File outputMandatoryFile = new File(outPath + name + ".dimacs.mandatory");

		IFeatureModel fm = Utils.loadSXFM(fmFile);

		DIMACSFormat format = new DIMACSFormat();
		String fmString = format.write(fm);
		Utils.writeStringToFile(outputFMFile, fmString);

		Map<String, String> dimacsNumberToF = new LinkedHashMap<String, String>();
		Map<String, String> fToDimacsNumber = new LinkedHashMap<String, String>();
		for (String line : Utils.getLinesOfFile(outputFMFile)) {
			if (line.startsWith("p cnf")) {
				break;
			}
			String[] lineSplit = line.split(" ");
			dimacsNumberToF.put(lineSplit[1], lineSplit[2]);
			fToDimacsNumber.put(lineSplit[2], lineSplit[1]);
		}

		Map<String, String> featureToQAs = new LinkedHashMap<String, String>();
		for (String line : Utils.getLinesOfFile(fmObjFile)) {
			int i = line.indexOf(" ");
			// remove parenthesis
			String fName = line.substring(1, i - 1);
			featureToQAs.put(fName, line.substring(i + 1));
		}

		// write augment file
		StringBuffer obj = new StringBuffer();
		obj.append("#FEATURE_INDEX Usability Battery Footprint\n");
		for (int i = 1; i <= dimacsNumberToF.size(); i++) {
			obj.append(i + " ");
			String fName = dimacsNumberToF.get(i + "");
			String qas = featureToQAs.get(fName.toLowerCase());
			if (qas == null) {
				System.err.println(fName + " no QAs found! Maybe abstract feature.");
				qas = "0.0 0.0 0.0";
			}
			obj.append(qas);
			obj.append("\n");
		}
		// remove last new line
		obj.setLength(obj.length() - 1);
		Utils.writeStringToFile(outputObjFile, obj.toString());

		// dead features
		StringBuffer deadString = new StringBuffer();
		FeatureModelAnalyzer operator = new FeatureModelAnalyzer(fm);
		List<IFeature> deadfs = operator.getDeadFeatures(new NullMonitor<LiteralSet>());
		// one id number per line
		for (IFeature f : deadfs) {
			deadString.append(fToDimacsNumber.get(f.getName()) + "\n");
		}
		if (!deadfs.isEmpty()) {
			deadString.setLength(deadString.length() - 1);
		}
		Utils.writeStringToFile(outputDeadFile, deadString.toString());

		// mandatory features
		StringBuffer mandatoryString = new StringBuffer();
		List<IFeature> mandatory = operator.getCoreFeatures(new NullMonitor<LiteralSet>());
		// one id number per line
		for (IFeature f : mandatory) {
			mandatoryString.append(fToDimacsNumber.get(f.getName()) + "\n");
		}
		if (!deadfs.isEmpty()) {
			mandatoryString.setLength(mandatoryString.length() - 1);
		}
		Utils.writeStringToFile(outputMandatoryFile, mandatoryString.toString());
	}

}
