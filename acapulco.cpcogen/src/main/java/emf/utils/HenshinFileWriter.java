package emf.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Mapping;
import org.eclipse.emf.henshin.model.ModelElement;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.Unit;
import org.eclipse.emf.henshin.model.impl.HenshinFactoryImpl;

public class HenshinFileWriter {
	
	public static void writeModuleToPath(List<Unit> units, String path) {
		StringBuilder result = new StringBuilder();
		Module module = HenshinFactoryImpl.eINSTANCE.createModule();
		module.getUnits().addAll(units);
		createStringRepresentation((Module) module, result);
		try {
			java.nio.file.Files.write(new File(path).toPath(), result.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeModuleToPath(Module module, String path) {
		StringBuilder result = new StringBuilder();
		createStringRepresentation((Module) module, result);
		try {
			java.nio.file.Files.write(new File(path).toPath(), result.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createStringRepresentation(Module module, StringBuilder sb) {
		for (Unit unit : module.getUnits()) {
			createStringRepresentation(module, (Rule) unit, sb);
		}
	}

	private static void createStringRepresentation(Module module, Rule rule, StringBuilder sb) {
		sbAppend("Rule ",rule.getName(), sb);

		if (!rule.getAnnotations().isEmpty()) {
			String featureConstraint = rule.getAnnotations().get(0).getValue();
			String featuresAsString = rule.getAnnotations().get(2).getValue().replace(" ", "");
			sbAppend("FMConstraint ",featureConstraint,sb);
			sbAppend("Features ",featuresAsString,sb);
		}
		
		processRuleContents(rule, sb);
		for (Rule multi : rule.getMultiRules()) {
			sbAppend("MultiRule ",multi.getName(), sb);
			processRuleContents(multi, sb);
		}
	}

	private static void sbAppend(String string1, String string2, StringBuilder sb) {
		sb.append(string1);
		sb.append(string2);
		sb.append("\n");
	}

	private static void processRuleContents(Rule rule, StringBuilder sb) {
		for (Mapping m : rule.getMappings()) {
			Node lhsNode = m.getOrigin();
			Node rhsNode = m.getImage();

			sb.append("Node");
			
//			if(!rule.getMultiMappings().isEmpty())
//				System.out.println("a");
				
			if (rule.getKernelRule() != null
					&& rule.getMultiMappings().getOrigin(lhsNode) == null) {
				sb.append("*");				
			}
			
			sb.append(" ");
			sb.append(lhsNode.getType().getName());
			sb.append(": ");
			
			if (rhsNode.getAttributes().isEmpty()) {
				sb.append("noop");
			} else {
				if (!lhsNode.getAttributes().isEmpty()) {
					sb.append(lhsNode.getAttributes().get(0).getValue());
					sb.append("->");
				}
				sb.append(rhsNode.getAttributes().get(0).getValue());
			}
			String pc = getPresenceCondition(lhsNode);
			if (pc != null) {
				sb.append(";");
				sb.append(pc);
			}
			sb.append("\n");
		}
	}


	static String getPresenceCondition(ModelElement element) {
		if (!element.getAnnotations().isEmpty())
			return element.getAnnotations().get(0).getValue();
		else
			return null;
	}
}
