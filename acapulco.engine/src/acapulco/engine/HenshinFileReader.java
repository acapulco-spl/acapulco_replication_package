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

	private static void nodeTreatment(Map<String, EClass> names2classes, ConfigurationSearchOperator currentOperator, String line) {
		int offset = line.startsWith("Node*")?5:4;
		String[] split = line.substring(offset).split(": ");
		String typeName = split[0].trim();
		EClass type = names2classes.get(typeName);
		if (type == null)
			throw new RuntimeException("Could not find class for type name: "+typeName);
		
		String[] split1 = split[1].split(";");
		String attr = split1[0];
		if (!attr.equals("noop")) {
			EAttribute attrType = type.getEAllAttributes().get(0);
			if (attr.contains("->")) {
				String[] valueSplit = attr.split("->");
				attr = valueSplit[1];
			}
		}
		
		currentOperator.addDecision(type, Boolean.parseBoolean(attr));
	}


	public static ConfigurationSearchOperator readConfigurationSearchOperatorFromFile(List<String> content, EPackage metamodel) {
		Map<String,EClass> names2classes = new HashMap<>();
		for (EClassifier cl : metamodel.getEClassifiers()) {
			names2classes.put(cl.getName(), (EClass) cl);
		}
		
		
		String operatorName = content.get(0).split(" ")[1];
		String rootFeatureName = operatorName.split("_")[1];
		ConfigurationSearchOperator result = new ConfigurationSearchOperator(names2classes.get(rootFeatureName));
		result.setName(operatorName);
		
		for (int i = 1; i<content.size(); i++) {
			String line = content.get(i);
			 if (line.startsWith("Node")) {
				nodeTreatment(names2classes, result, line);
			}
		}
		return result;
	}
	

	public static ConfigurationSearchOperator readConfigurationSearchOperatorFromFile(String ruleLocation, EPackage metamodel) {
		List<String> content = null;
		try {
			content = Files.readAllLines(new File(ruleLocation).toPath(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readConfigurationSearchOperatorFromFile(content, metamodel);
	}

}
