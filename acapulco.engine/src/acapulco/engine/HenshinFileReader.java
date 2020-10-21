package acapulco.engine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class HenshinFileReader {
	public static Module readModuleFromFile(List<String> contentLines, EPackage metamodel) {
		Module m = HenshinFactory.eINSTANCE.createModule();
		Map<String,EClass> names2classes = new HashMap<>();
		for (EClassifier cl : metamodel.getEClassifiers()) {
			names2classes.put(cl.getName(), (EClass) cl);
		}
		
		Rule currentRule = null;
		Map<String, Node> kernelNodesLhs = new HashMap<>();
		Map<String, Node> kernelNodesRhs = new HashMap<>();
		boolean inMulti = false;
		for (String line : contentLines) {
			if (line.startsWith("Rule")) {
				currentRule = HenshinFactory.eINSTANCE.createRule(line.split(" ")[1]);
				m.getUnits().add(currentRule);
				kernelNodesLhs.clear();
				kernelNodesRhs.clear();
				inMulti = false;
			} else if (line.startsWith("FMConstraint")) {
				Annotation ann = HenshinFactory.eINSTANCE.createAnnotation();
				ann.setKey("featureModel");
				ann.setValue(line.substring(13));
				currentRule.getAnnotations().add(ann);
				currentRule.getAnnotations().add(HenshinFactory.eINSTANCE.createAnnotation());
			} else if (line.startsWith("Features")) {
				Annotation ann = HenshinFactory.eINSTANCE.createAnnotation();
				ann.setKey("features");
				ann.setValue(line.substring(9));
				currentRule.getAnnotations().add(ann);				
			} else if (line.startsWith("Node")) {
				nodeTreatment(names2classes, currentRule, line, kernelNodesLhs, kernelNodesRhs, inMulti);
			} else if (line.startsWith("MultiRule")) {
				Rule kernelRule = inMulti ? currentRule.getKernelRule() : currentRule;				
				currentRule = HenshinFactory.eINSTANCE.createRule(line.split(" ")[1]);
				kernelRule.getMultiRules().add(currentRule);
				inMulti = true;
			}
		}
		
		
		return m;
	}

	private static void nodeTreatment(Map<String, EClass> names2classes, Rule currentRule, String line, Map<String, Node> kernelNodesLhs, Map<String, Node> kernelNodesRhs, boolean inMulti) {
		int offset = line.startsWith("Node*")?5:4;
		String[] split = line.substring(offset).split(": ");
		String typeName = split[0].trim();
		EClass type = names2classes.get(typeName);
		if (type == null)
			throw new RuntimeException("Could not find class for type name: "+typeName);
		
		Node lhsNode = HenshinFactory.eINSTANCE.createNode(currentRule.getLhs(),type,"");
		Node rhsNode = HenshinFactory.eINSTANCE.createNode(currentRule.getRhs(),type,"");
		currentRule.getMappings().add(lhsNode,rhsNode);
		
		String[] split1 = split[1].split(";");
		String attr = split1[0];
		if (!attr.equals("noop")) {
			EAttribute attrType = type.getEAllAttributes().get(0);
			if (attr.contains("->")) {
				String[] valueSplit = attr.split("->");
				HenshinFactory.eINSTANCE.createAttribute(lhsNode, attrType, valueSplit[0]);
				attr = valueSplit[1];
			}
			HenshinFactory.eINSTANCE.createAttribute(rhsNode, attrType, attr);					
		}
		
		
		if (split1.length > 1) {
			String pc = split1[1];
			setPresenceCondition(lhsNode, pc);
			setPresenceCondition(rhsNode, pc);
		}
		
		if (inMulti) {
			if (!line.startsWith("Node*")) {
				Node lhsNodeKernel = kernelNodesLhs.get(typeName); 
				Node rhsNodeKernel = kernelNodesRhs.get(typeName); 
				currentRule.getMultiMappings().add(lhsNode, lhsNodeKernel);
				currentRule.getMultiMappings().add(rhsNode, rhsNodeKernel);				
			} 
		} else {
			kernelNodesLhs.put(typeName, lhsNode);
			kernelNodesRhs.put(typeName, lhsNode);
		}
	}

	static void setPresenceCondition(ModelElement element, String value) {
		Annotation ann = null;
		if (!element.getAnnotations().isEmpty())
			ann = element.getAnnotations().get(0);
		else {
			ann = HenshinFactory.eINSTANCE.createAnnotation();
			ann.setKey("presenceCondition");
			element.getAnnotations().add(ann);
		}
		ann.setValue(value);
	}

	public static Module readModuleFromFile(String ruleLocation, EPackage metamodel) {
		List<String> content = null;
		try {
			content = Files.readAllLines(new File(ruleLocation).toPath(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readModuleFromFile(content, metamodel);
	}
}
