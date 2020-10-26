package acapulco.preparation.converters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.sxfm.SXFMFormat;

public class Utils {
	
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

	/**
	 * Load fm in fama
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
}
