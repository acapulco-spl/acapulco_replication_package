package acapulco.pipeline;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.uma.mo_dagame.algorithm.main.MoDagameStudy;

import acapulco.aCaPulCO_Main;
import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.model.FeatureModel;
import acapulco.preparation.PreparationPipeline;
import acapulco.rulesgeneration.CpcoGenerator;
import mdeoptimiser4efm.algorithm.termination.StoppingCondition;
import satibea.SATIBEA_Main;

public class PipelineRunner {
	public static void main(String[] args) throws IOException {
//		boolean prepare = true; 		boolean run = false;
		boolean prepare = false ; 		boolean run = true;

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
			System.out.println("Running ACAPULCO...");
			acapulcoSearch.run(fullFmPath, sc, sv, debug);
			
			
			/*
			 *  SATIBEA execution with external Java .jar because of incompatibility with SAT4J library (MiniSAT solver).
			 *  Between library in thirdpartyplugins and SATIBEA libs.
			 */
			String[] satibeaArgs = new String[]{"-fm", generatedPath + "/" + fmNameCanonical + "/satibea/" + fmNameCanonical + ".dimacs", "-sc", sc.toString() , "-sv", sv.toString()};
			
			//String satibeaCommand = "java -jar SATIBEA.jar -fm " + generatedPath + "/" + fmNameCanonical + "/satibea/" + fmNameCanonical + ".dimacs" + " -sc " + sc.toString() + " -sv " + sv.toString();
			try {
				System.out.println("Running SATIBEA...");
				//runCLI(".", satibeaCommand);
				SATIBEA_Main.main(satibeaArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/**
			 * Modagame execution.
			 * 
			 */
			// Usage: MoDagameStudy experimentName baseDirectory independentRuns stoppingCondition('time'' or 'evols') stoppingValue
			String modagameSC = sc.equals(StoppingCondition.TIME) ? "time" : "evols";
			File file = new File(generatedPath + "/" + fmNameCanonical + "/modagame");
			System.out.println(file.getAbsolutePath());
			String[] modagameArgs = new String[]{fmNameCanonical, file.getAbsolutePath(), "1", modagameSC, sv.toString()};
					
			try {
				System.out.println("Running MODAGAME...");
				MoDagameStudy.main(modagameArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
