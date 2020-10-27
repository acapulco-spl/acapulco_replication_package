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
		String fmName = "test1";
		String fmPath = "ad-test-1";

		if (prepare) {
			FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(inputPath+"/"+fmPath+".sxfm.xml").toString());
			PreparationPipeline.generateAllFromFm(inputPath, fmPath, fmName, "generated");
			CpcoGenerator.generatorCPCOs(fm, fmName, generatedPath);
		}

		if (run) {
			aCaPulCO_Main acapulcoSearch = new aCaPulCO_Main();
			StoppingCondition sc = StoppingCondition.EVOLUTIONS;
			Integer sv = 50;
			boolean debug = true;
			String fullFmPath = generatedPath + "/" + fmName + "/acapulco/" + fmName + ".dimacs";
			acapulcoSearch.run(fullFmPath, sc, sv, debug);
		}
	}
}
