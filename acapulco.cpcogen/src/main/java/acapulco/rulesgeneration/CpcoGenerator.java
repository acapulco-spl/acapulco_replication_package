package acapulco.rulesgeneration;

import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.emf.henshin.model.Rule;

import acapulco.featuremodel.FeatureModelHelper;
import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.preparation.cleaning.Cleaner;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram;
import de.ovgu.featureide.fm.core.FeatureModelAnalyzer;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.job.monitor.NullMonitor;
import emf.utils.HenshinConfigurator;
import emf.utils.HenshinFileWriter;

public class CpcoGenerator {
	public static void generatorCPCOs(FeatureModel fm, String fmName, String outpath, String originalInputFMPath) {
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
		
		// Remove dead features:
		File fmFile = new File(Paths.get(originalInputFMPath).toString());
		System.out.println("file: " + fmFile.getPath());
		IFeatureModel originalFM = Cleaner.loadSXFM(fmFile);
		FeatureModelAnalyzer operator = new FeatureModelAnalyzer(originalFM);
		List<String> deadFeatures = operator.getDeadFeatures(new NullMonitor<LiteralSet>()).stream().map(f -> f.getName()).collect(Collectors.toList());
		
		System.out.println("+++++++++++++++++++++++++++++ Dead features ***+++++++++++++++++++++++++++++++++++");
		for (String f : deadFeatures) {
			System.out.println("Dead feature: " + f);
		}
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		for (Feature f : trueOptional) {
			if (!deadFeatures.contains(f.getName())) {
				System.out.println("Generating Act CPCO for feature: " + f.getName());	
				FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, true); // CPCO-specific
				Rule rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
				rule = HenshinConfigurator.removeVariability(rule);
				HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
				
				System.out.println("Generating De CPCO for feature: " + f.getName());
				sd = ad.calculateSubdiagramFor(f, false); // CPCO-specific
				rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
				rule = HenshinConfigurator.removeVariability(rule);
				HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
			}
		}
	}
}
