package acapulco.featureide.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import de.ovgu.featureide.fm.core.ConstraintAttribute;
import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.Constraint;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFormatManager;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.io.IFeatureModelFormat;
import de.ovgu.featureide.fm.core.io.manager.FileHandler;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;
import acapulco.featureide.FeatureIDE2FM;
import de.ovgu.featureide.fm.core.configuration.Configuration;

/**
 * Feature IDE Utils
 * 
 */
public class FeatureIDEUtils {
	
//	public static List<String> getValidConfiguration(File fmFeatureIDE) throws TimeoutException {
//		IFeatureModel fm = FeatureIDEUtils.load(fmFeatureIDE);
//		final Configuration conf = new Configuration(fm);
//		List<List<String>> solution = conf.getSolutions(1);
//		if (!solution.isEmpty()) {
//			return solution.get(0);
//		} else {
//			return new ArrayList<String>();
//		}
//	}
	
	/**
	 * Load
	 * 
	 * @param file
	 */
	public static IFeatureModel load(File file) {
		String content = getStringOfFile(file);
		IFeatureModel featureModel = new FeatureModel(DefaultFeatureModelFactory.ID);
		IFeatureModelFormat format = FMFormatManager.getInstance().getFormatByContent(content, file.getName());
		FileHandler.loadFromString(content, featureModel, format);
		return featureModel;
	}

	/**
	 * Remove redundant constraints
	 * 
	 * @param fm
	 */
	public static void removeRedundantConstraints(IFeatureModel fm) {
		FeatureModelAnalyzer analyzer = fm.getAnalyser();
		analyzer.calculateRedundantConstraints = true;
		analyzer.calculateTautologyConstraints = false;
		analyzer.calculateDeadConstraints = false;
		analyzer.calculateFOConstraints = false;
		HashMap<Object, Object> o = analyzer.analyzeFeatureModel(new NullMonitor());
		for (Entry<Object, Object> entry : o.entrySet()) {
			if (entry.getKey() instanceof Constraint) {
				if (entry.getValue() instanceof ConstraintAttribute) {
					if ((ConstraintAttribute) entry.getValue() == ConstraintAttribute.REDUNDANT) {
						fm.removeConstraint((Constraint) entry.getKey());
					}
				}
			}
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
		if (string.length() > 0) // If the file is empty the -1 causes an
									// exception
			string.setLength(string.length() - 1);
		return string.toString();
	}

	public static void saveEObject(URI uri, EObject eObject) throws IOException {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("uml", new XMIResourceFactoryImpl());

		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(uriToEMFURI(uri));
		resource.getContents().add(eObject);
		resource.save(Collections.EMPTY_MAP);
	}

	public static org.eclipse.emf.common.util.URI uriToEMFURI(URI uri) {
		return org.eclipse.emf.common.util.URI.createURI(uri.toString());
	}

	public static acapulco.model.FeatureModel loadFeatureModel(String featureIDE) {
		return loadFeatureModel(Paths.get(featureIDE).toFile());
	}
	
	public static acapulco.model.FeatureModel loadFeatureModel(File featureIDE) {
		
		IFeatureModel fm = FeatureIDEUtils.load(featureIDE);
		return FeatureIDE2FM.create(fm);
	}

}
