package acapulco.engine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Line;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.henshin.model.Annotation;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.ModelElement;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.Unit;

import acapulco.engine.variability.ConfigurationSearchOperator;

public class HenshinFileReader {

	private static void nodeTreatment(Map<String, Integer> featureName2index, ConfigurationSearchOperator currentOperator, String line) {
		int offset = line.startsWith("Node*")?5:4;
		String[] split = line.substring(offset).split(": ");
		String typeName = split[0].trim();
		Integer type = featureName2index.get(typeName);
		if (type == null)
			return;
//			throw new RuntimeException("Could not find class for type name: "+typeName);
		
		String[] split1 = split[1].split(";");
		String attr = split1[0];
		if (!attr.equals("noop")) {
			if (attr.contains("->")) {
				String[] valueSplit = attr.split("->");
				attr = valueSplit[1];
			}
		}
		
		currentOperator.addDecision(type, Boolean.parseBoolean(attr));
	}


	public static ConfigurationSearchOperator readConfigurationSearchOperatorFromFile(List<String> content, Map<String, Integer> featureName2index) {
		String operatorName = content.get(0).split(" ")[1];
		String rootFeatureName = operatorName.split("_")[1];
		ConfigurationSearchOperator result = new ConfigurationSearchOperator(featureName2index.get(rootFeatureName));
		result.setName(operatorName);
		
		for (int i = 1; i<content.size(); i++) {
			String line = content.get(i);
			 if (line.startsWith("Node")) {
				nodeTreatment(featureName2index, result, line);
			}
		}
		return result;
	}
	

	public static ConfigurationSearchOperator readConfigurationSearchOperatorFromFile(String ruleLocation,  Map<String, Integer> featureName2index) {
		List<String> content = null;
		try {
			content = Files.readAllLines(new File(ruleLocation).toPath(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readConfigurationSearchOperatorFromFile(content, featureName2index);
	}

}
