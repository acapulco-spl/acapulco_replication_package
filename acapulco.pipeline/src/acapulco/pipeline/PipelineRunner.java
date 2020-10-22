package acapulco.pipeline;

import java.io.IOException;
import java.nio.file.Paths;

import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.model.FeatureModel;
import acapulco.preparation.converters.PreparationPipeline;
import acapulco.rulesgeneration.CpcoGenerator;

public class PipelineRunner {
	public static void main(String[] args) throws IOException {
		String fmPath = "testdata/ad-test-1.sxfm.xml";
		FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString());

		PreparationPipeline.generateAllFromFm("testdata", "ad-test-1.sxfm", "test1", "generated");

		CpcoGenerator.generatorCPCOs(fm, "test1", "generated");

	}
}
