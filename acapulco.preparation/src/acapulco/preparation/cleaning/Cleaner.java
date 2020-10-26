package acapulco.preparation.cleaning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFormatManager;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.manager.FileHandler;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMFormat;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;

public class Cleaner {

	public static void main(String[] args) {
//		String fmPath = "fms/automotive2_1.sxfm.xml-nocomplex.sxfm.xml";

		// Load fm
		String fmPath = args[0];
		File fmFile = new File(fmPath);
		IFeatureModel fm = loadSXFM(fmFile);
		printMetrics(fm);

		FeatureModelAnalyzer operator = new FeatureModelAnalyzer(fm);

		List<IFeature> dead = operator.getDeadFeatures(new NullMonitor<LiteralSet>());
		// System.out.println("Dead features: " + dead);

		for (IFeature deadF : dead) {
			fm.deleteFeature(deadF);
		}

		List<IFeature> falseOpt = operator.getFalseOptionalFeatures(new NullMonitor<List<LiteralSet>>());
		// System.out.println("False optional features: " + falseOpt);

		for (IFeature falseOptF : falseOpt) {
			IFeatureStructure parent = falseOptF.getStructure().getParent();
			if (parent.isOr() || parent.isAlternative()) {
				// remove it from the group, add it as mandatory
				IFeature newGroup = new Feature(falseOptF.getFeatureModel(), parent.getFeature().getName() + "_group");
				newGroup.getStructure().setAbstract(true);
				newGroup.getStructure().setMandatory(true);

				if (parent.isOr()) {
					newGroup.getStructure().setOr();
				} else if (parent.isAlternative()) {
					newGroup.getStructure().setAlternative();
				}

				for (IFeatureStructure child : parent.getChildren()) {
					if (child != falseOptF.getStructure()) {
						newGroup.getStructure().addChild(child);
					}
				}

				for (IFeatureStructure child : newGroup.getStructure().getChildren()) {
					parent.removeChild(child);
				}

				parent.addChild(newGroup.getStructure());
				parent.changeToAnd();
				falseOptF.getStructure().setMandatory(true);

			} else {
				falseOptF.getStructure().setMandatory(true);
			}
		}

		List<IFeature> falseOpt3 = operator.getFalseOptionalFeatures(new NullMonitor<List<LiteralSet>>());
		System.out.println("False optional features: " + falseOpt3);

		List<IFeature> dead2 = operator.getDeadFeatures(new NullMonitor<LiteralSet>());
		System.out.println("Dead features: " + dead2);

		// Remove constraints with features that were removed
		List<IConstraint> noLongerValidConstraints = new ArrayList<IConstraint>();
		for (IConstraint c : fm.getConstraints()) {
			for (IFeature f : c.getContainedFeatures()) {
				if (fm.getFeature(f.getName()) == null) {
					noLongerValidConstraints.add(c);
					break;
				}
			}
		}

		for (IConstraint constraint : noLongerValidConstraints) {
			fm.removeConstraint(constraint);
		}

		// Save clean fm
		File fmOutputFile = new File(fmPath + ".clean.xml");
		// System.out.println(fm);
		saveFM(fmOutputFile, fm);

		// second load because I had errors using the same fm
		IFeatureModel fm2 = load(getStringOfFile(fmOutputFile));
		FeatureModelAnalyzer operator2 = new FeatureModelAnalyzer(fm2);
		List<IConstraint> redundantConstraints = operator2.getRedundantConstraints(new NullMonitor<List<LiteralSet>>());
		System.out.println("Redundant constraints: " + redundantConstraints);

		for (IConstraint constraint : redundantConstraints) {
			fm2.removeConstraint(constraint);
		}

		FeatureModelAnalyzer operator3 = new FeatureModelAnalyzer(fm2);
		List<IConstraint> redundantConstraints2 = operator3
				.getRedundantConstraints(new NullMonitor<List<LiteralSet>>());
		System.out.println("Redundant constraints: " + redundantConstraints2);

		// System.out.println(fm2);
		printMetrics(fm2);
		fmOutputFile = new File(fmPath + ".clean.sxfm.xml");
		saveSXFM(fmOutputFile, fm2);
	}

	public static void printMetrics(IFeatureModel fm) {
		System.out.println("Features " + fm.getFeatures().size());
		int groups = 0;
		for (IFeature f : fm.getFeatures()) {
			if (f.getStructure().isOr() || f.getStructure().isAlternative()) {
				groups++;
			}
		}
		System.out.println("Groups " + groups);
		FeatureModelAnalyzer operator = new FeatureModelAnalyzer(fm);
		System.out.println("Core " + operator.getCoreFeatures(new NullMonitor<LiteralSet>()).size());
		System.out.println("CTCs " + fm.getConstraints().size());
	}

	/**
	 * Load fm in sxfm
	 * 
	 * @param file
	 */
	public static IFeatureModel loadSXFM(File file) {
		SXFMFormat format = new SXFMFormat();
		IFeatureModel featureModel = new FeatureModel(DefaultFeatureModelFactory.ID);
		format.read(featureModel, getStringOfFile(file));
		return featureModel;
	}

	/**
	 * Load fm as string
	 * 
	 * @param file
	 */
	public static IFeatureModel load(String content) {
		IFeatureModel featureModel = new FeatureModel(DefaultFeatureModelFactory.ID);
		FMFormatManager.getInstance().addExtension(new XmlFeatureModelFormat());
		IPersistentFormat<IFeatureModel> format = FMFormatManager.getInstance().getFormatByContent(content, "a.xml");
		FileHandler.loadFromString(content, featureModel, format);
		return featureModel;
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
			br.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * Get string
	 * 
	 * @param file
	 * @return
	 */
	public static String getStringOfFile(File file) {
		StringBuilder string = new StringBuilder();
		for (String line : getLinesOfFile(file)) {
			string.append(line + "\n");
		}
		if (string.length() > 0) // If the file is empty the -1 causes an exception
			string.setLength(string.length() - 1);
		return string.toString();
	}

	public static void saveFM(File newFile, IFeatureModel fm) {
		XmlFeatureModelFormat format = new XmlFeatureModelFormat();
		String fmString = format.write(fm);
		writeStringToFile(newFile, fmString);
	}

	public static void saveSXFM(File newFile, IFeatureModel fm) {
		SXFMFormat format = new SXFMFormat();
		String fmString = format.write(fm);
		writeStringToFile(newFile, fmString);
	}

	public static void writeStringToFile(File file, String text) {
		try {
			BufferedWriter output;
			output = new BufferedWriter(new FileWriter(file, false));
			output.append(text);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
