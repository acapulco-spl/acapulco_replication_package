package acapulco.pipeline;

import java.io.IOException;
import java.nio.file.Paths;

import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.model.FeatureModel;
import acapulco.preparation.PreparationPipeline;
import acapulco.rulesgeneration.MyGenerator;

public class MyGeneratorMain {

	public static void main(String[] args) throws IOException {
		String inputPath = "input";
		String generatedPath = "generated";

		String fmNameInput = "ad-test-1";		String fmNameCanonical = "test1";
//		String fmNameInput = "TankWar";			String fmNameCanonical = "tankwar";
//		String fmNameInput = "WeaFQAs";			String fmNameCanonical = "weafqas";
//		String fmNameInput = "mobile_media2";	String fmNameCanonical = "mobilemedia";
		
		FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(inputPath+"/"+fmNameInput+".sxfm.xml").toString());
		PreparationPipeline.generateAllFromFm(inputPath, fmNameInput, fmNameCanonical, "generated");
		MyGenerator.generatorCPCOs(fm, fmNameCanonical, generatedPath);
		//MyGenerator.generatorCPCOsForFeature(fm, fmNameCanonical, generatedPath, "F1", true);
	}

	

}
