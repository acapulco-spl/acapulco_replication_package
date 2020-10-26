package acapulco.rulesgeneration;

import java.util.Collections;

import org.eclipse.emf.henshin.model.Rule;

import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram;
import emf.utils.HenshinFileWriter;

public class CpcoGenerator {
	public static void generatorCPCOs(FeatureModel fm, String fmName, String outpath) {
		outpath += "/"+fmName;
		FeatureActivationDiagram ad = new FeatureActivationDiagram(fm); // FM-specific
		FMConfigurationMetamodelGenerator metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName,
				"http://"+fmName);
		
		metamodelGen.generateMetamodel();
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.ecore");
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".ecore");

		for (Feature f : metamodelGen.geteClasses().keySet()) {
			if(f.getName().startsWith("R"))
				continue;
			FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, true); // CPCO-specific
			Rule rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
			HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath+"\\acapulco\\cpcos\\"+rule.getName()+".hen");
			
			sd = ad.calculateSubdiagramFor(f, false); // CPCO-specific
			rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
			HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath+"\\acapulco\\cpcos\\"+rule.getName()+".hen");
			
		}
		
	}
}
