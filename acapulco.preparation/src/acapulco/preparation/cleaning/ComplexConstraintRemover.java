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

import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;
import org.prop4j.Or;

import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFormatManager;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.manager.FileHandler;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMFormat;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;

public class ComplexConstraintRemover {

	public static void main(String[] args) {
		// Load fm
		// String fmPath = "testModel.xml";
//		String fmPath = "fms/kconfig/linux-2.6.33.3.sxfm.xml";
		String fmPath = args[0];
		File fmFile = new File(fmPath);
		IFeatureModel fm = loadSXFM(fmFile);
		printMetrics(fm);

		List<IConstraint> simple = new ArrayList<>();
		for (IConstraint c : fm.getConstraints()) {
			if (isSimple(c))
				simple.add(c);
		}

		System.out.println("Removed " + (fm.getConstraints().size() - simple.size()) + " complex constraints.");
		fm.setConstraints(simple);
		printMetrics(fm);

		// Save clean fm
		File fmOutputFile = new File(fmPath + "-nocomplex.sxfm.xml");
		// System.out.println(fm);
		saveSXFM(fmOutputFile, fm);
	}

	private static boolean isSimple(IConstraint c) {
		if (c.getNode() instanceof Or) {
			Or orNode = (Or) c.getNode();
			Node[] children = orNode.getChildren();
			if (children.length == 2) {
				int nots = 0;
				int literals = 0;

				for (Node child : children) {
					if (child instanceof Not) {
						nots++;
						if ((child.getChildren()[0] instanceof Literal)) {
							literals++;
						}
					} else if (child instanceof Literal) {
						literals++;
					}
				}
				
				if (literals < 2 || nots < 1) {
					return false;
				} else return true;
			}
		}
		return false;
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
