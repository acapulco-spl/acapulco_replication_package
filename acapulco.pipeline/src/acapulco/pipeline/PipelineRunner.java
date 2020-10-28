package acapulco.pipeline;

import java.io.IOException;
import java.nio.file.Paths;
import acapulco.aCaPulCO_Main;
import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.model.FeatureModel;
import acapulco.preparation.PreparationPipeline;
import acapulco.rulesgeneration.CpcoGenerator;
import mdeoptimiser4efm.algorithm.termination.StoppingCondition;

public class PipelineRunner {
	public static void main(String[] args) throws IOException {
		boolean prepare = true; 		boolean run = false;
//		boolean prepare = false ; 		boolean run = true;

		String inputPath = "input";
		String generatedPath = "generated";

		String fmNameInput = "ad-test-1";		String fmNameCanonical = "test1";
//		String fmNameInput = "TankWar";			String fmNameCanonical = "tankwar";
//		String fmNameInput = "WeaFQAs";			String fmNameCanonical = "weafqas";
//		String fmNameInput = "mobile_media2";	String fmNameCanonical = "mobilemedia";
		
		if (prepare) {
			FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(inputPath+"/"+fmNameInput+".sxfm.xml").toString());
			PreparationPipeline.generateAllFromFm(inputPath, fmNameInput, fmNameCanonical, "generated");
			CpcoGenerator.generatorCPCOs(fm, fmNameCanonical, generatedPath);
		}

		if (run) {
			aCaPulCO_Main acapulcoSearch = new aCaPulCO_Main();
			StoppingCondition sc = StoppingCondition.EVOLUTIONS;
			Integer sv = 50;
			boolean debug = true;
			String fullFmPath = generatedPath + "/" + fmNameCanonical + "/acapulco/" + fmNameCanonical + ".dimacs";
			acapulcoSearch.run(fullFmPath, sc, sv, debug);
		}
	}
}
