package acapulco.rulesgeneration;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.emf.henshin.model.Rule;

import acapulco.featuremodel.FeatureModelHelper;
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
		System.out.println(outpath);
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.ecore");
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+fmName+".ecore");
		
		FeatureModelHelper helper = new FeatureModelHelper(fm);
		List<Feature> trueOptional = new ArrayList<>(helper.getFeatures());
		trueOptional.removeAll(helper.getAlwaysActiveFeatures());
		
		for (Feature f : trueOptional) {
			FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, true); // CPCO-specific
			Rule rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
			HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
			
			sd = ad.calculateSubdiagramFor(f, false); // CPCO-specific
			rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
			HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
			
		}
		
	}
}
