package emf.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.Unit;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
//import org.sidiff.common.henshin.HenshinModuleAnalysis;
//import org.sidiff.serge.util.RuleSemanticsChecker;

public class HenshinUtils {
	
	/**
	 * Serialise the given Henshin module to the given location
	 * 
	 * @param module
	 * @param path
	 */
	public static void serializeModule(Module module, String path) {
		try {
			HenshinResourceSet resourceSet = new HenshinResourceSet(path);
			
			XMLResource resource = (XMLResource) resourceSet.createResource(URI.createFileURI(String.format("%s.henshin", module.getName())));
			resource.getDefaultSaveOptions().put(XMIResource.OPTION_ENCODING, "UTF-8");
			
			//org.eclipse.emf.ecore.xmi.XMLResource.setEncoding(String);
			if (resource.isLoaded()) {
				resource.getContents().clear();
			}
			
			resource.getContents().add(module);
			resource.save(Collections.EMPTY_MAP);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Module loadModule(String filePath) {
		HenshinResourceSet resourceSet = new HenshinResourceSet();
		return resourceSet.getModule(filePath);
	}
	
//	public static boolean isEqual(Module left, Module right) {	
//		int nRulesLeft = HenshinModuleAnalysis.getAllRules(left).size();
//		int nRulesRight = HenshinModuleAnalysis.getAllRules(right).size();
//		
//		if (nRulesLeft != nRulesRight) {
//			return false;
//		} else {
//			for (int i = 0; i < nRulesLeft; i++) {
//				Rule leftRule = HenshinModuleAnalysis.getAllRules(left).get(i);
//				Rule rightRule = HenshinModuleAnalysis.getAllRules(right).get(i);
//
//				RuleSemanticsChecker rsc = new RuleSemanticsChecker(leftRule, rightRule);
//				if (!rsc.isEqual()) {
//					return false;
//				}
//			}	
//		}
//		return true;
//	}

	public static void serializeModuleOptimized(Module actModule, String serializeDir) {
		Path currentDir = Paths.get(".");
		Path path = Paths.get(currentDir.toAbsolutePath().getParent().toString(), serializeDir, actModule.getName() + ".hen");
		EObject a;
		List<Unit> units = new ArrayList<>();
		units.addAll(actModule.getUnits());
		HenshinFileWriter.writeModuleToPath(units, path.toString());
	}
	
}
