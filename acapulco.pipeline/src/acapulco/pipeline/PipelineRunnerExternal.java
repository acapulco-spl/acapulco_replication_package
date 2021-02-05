package acapulco.pipeline;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.uma.mo_dagame.algorithm.main.MoDagameStudy;

import acapulco.aCaPulCO_Main;
import acapulco.algorithm.termination.StoppingCondition;
import acapulco.evaluation.output.ParseResults;
import acapulco.evaluation.output.Results;
import acapulco.evaluation.paretotruefront.MergedPTF;
import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.model.FeatureModel;
import acapulco.preparation.PreparationPipeline;
import acapulco.rulesgeneration.CpcoGenerator;
import satibea.SATIBEA_Main;

public class PipelineRunnerExternal {
	private static final String ACAPULCO_TOOL_NAME = "acapulco";
	private static final String MODAGAME_TOOL_NAME = "modagame";
	private static final String SATIBEA_TOOL_NAME = "satibea";
	private static String[] tools1 = {ACAPULCO_TOOL_NAME, MODAGAME_TOOL_NAME, SATIBEA_TOOL_NAME};
	
	private static final String inputPath = "input";
	private static final String outputPath = "output";
	private static final String generatedPath = "generated";
	
	
	public static void main(String[] args) throws IOException {
		// Command line parser
		Options options = new Options();

		Option option1 = new Option("p", "prepare", false, "Prepare all the artifacts for all tools.");
        option1.setRequired(false);
        options.addOption(option1);
        
        Option option2 = new Option("fm", "fmName", true, "Name of the feature model.");
        option2.setRequired(true);
        options.addOption(option2);
        
        Option option3 = new Option("t", "tool", true, "Tool to execute: 'acapulco', 'modagame', 'satibea'. (Required if not prepare and not all)");
        option3.setRequired(false);
        options.addOption(option3);
        
        Option option4 = new Option("sc", "stoppingcondition", true, "Stopping condition: 'evols' (default), 'time'.");
        option4.setRequired(false);
        options.addOption(option4);
        
        Option option5 = new Option("sv", "stoppingvalue", true, "Stopping value for each experiment (time in seconds or number of evolutions) (default 50).");
        option5.setRequired(false);
        options.addOption(option5);
        
        Option option6 = new Option("r", "runs", true, "Number of runs for each experiment (default 1).");
        option6.setRequired(false);
        options.addOption(option6);
        
        Option option7 = new Option("a", "allTools", false, "Execute all tools (and calculate PTF and HVs).");
        option7.setRequired(false);
        options.addOption(option7);
        
        Option option8 = new Option("ptf", "ptfOption", false, "Calculate PTF and HVs (in case 'allTools' option not choosen and tools individually executed).");
        option8.setRequired(false);
        options.addOption(option8);
        
        
        String header = "Execute the pipeline.\n\n";
        String footer = "\nPlease report issues at https://github.com/alxbrd/mdeoptimiser4fm";
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;        
        
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java -jar Pipeline.jar", header, options, footer, true);
            System.exit(1);
        }

        // Preparation
        boolean prepare = cmd.hasOption("prepare");
        String fmNameInput = cmd.getOptionValue("fmName").toLowerCase();
		
        if (prepare) {
        	System.out.println("Generating artifacts...");
			FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(inputPath+"/"+fmNameInput+".sxfm.xml").toString());
			PreparationPipeline.generateAllFromFm(inputPath, fmNameInput, fmNameInput, "generated");
			CpcoGenerator.generatorCPCOs(fm, fmNameInput, generatedPath);
			System.out.println("Done!");
			System.exit(0);
		}
     
        // Tool execution
        boolean allTools = cmd.hasOption("allTools");
        boolean ptfOption = cmd.hasOption("ptfOption");
        if (!cmd.hasOption("tool") && !allTools && !ptfOption) {
        	System.out.println("You must specified at least one of these options: 'tool', 'allTools', 'ptfOption'");
            formatter.printHelp("java -jar Pipeline.jar", header, options, footer, true);
            System.exit(1);
        }
        
        String sTool = cmd.hasOption("tool") ? cmd.getOptionValue("tool") : "None";
        String stoppingcondition = cmd.hasOption("stoppingcondition") ? cmd.getOptionValue("stoppingcondition"): "evols";
        String stoppingvalue = cmd.hasOption("stoppingvalue") ? cmd.getOptionValue("stoppingvalue") : "50";     
		Integer runs = cmd.hasOption("runs") ? Integer.parseInt(cmd.getOptionValue("runs")) : 1;
        
        StoppingCondition sc = StoppingCondition.EVOLUTIONS;
        if (stoppingcondition.equalsIgnoreCase("time")) {
        	sc = StoppingCondition.TIME;
        } else if (stoppingcondition.equalsIgnoreCase("evols")) {
        	sc = StoppingCondition.EVOLUTIONS;
        }
        
		Integer sv = Integer.parseInt(stoppingvalue);
		
        if (allTools || sTool.equalsIgnoreCase(ACAPULCO_TOOL_NAME)) {
        	aCaPulCO_Main acapulcoSearch = new aCaPulCO_Main();
			String fullFmPath = generatedPath + "/" + fmNameInput + "/acapulco/" + fmNameInput + ".dimacs";
			
			try {
				System.out.println("Running ACAPULCO...");
				for (int i = 1; i <= runs; i++) {
					System.out.println("ACAPULCO Run " + i + "/" + runs + "...");
					acapulcoSearch.run(fullFmPath, sc, sv, false);
					Thread.sleep(1000);
					System.gc();
				}
				
				parseResults(ACAPULCO_TOOL_NAME, fmNameInput, runs, null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        if (allTools || sTool.equalsIgnoreCase(MODAGAME_TOOL_NAME)) {
        	String modagameSC = sc.equals(StoppingCondition.TIME) ? "time" : "evols";
			File file = new File(generatedPath + "/" + fmNameInput + "/modagame");
			String[] modagameArgs = new String[]{fmNameInput, file.getAbsolutePath(), "1", modagameSC, sv.toString()};
					
			try {
				System.out.println("Running MODAGAME...");
				
				for (int i = 1; i <= runs; i++) {
					System.out.println("MODAGAME Run " + i + "/" + runs + "...");
					MoDagameStudy.main(modagameArgs);
					Thread.sleep(1000);
					System.gc();
				}
				
				parseResults(MODAGAME_TOOL_NAME, fmNameInput, runs, null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
        }
        
        if (allTools || sTool.equalsIgnoreCase(SATIBEA_TOOL_NAME)) {
        	String[] satibeaArgs = new String[]{"-fm", generatedPath + "/" + fmNameInput + "/satibea/" + fmNameInput + ".dimacs", "-sc", sc.toString() , "-sv", sv.toString()};
        
        	try {	
        		System.out.println("Running SATIBEA...");
				
				for (int i = 1; i <= runs; i++) {
					System.out.println("SATIBEA Run " + i + "/" + runs + "...");
					SATIBEA_Main.main(satibeaArgs);
					Thread.sleep(1000);
					System.gc();
				}

				parseResults(SATIBEA_TOOL_NAME, fmNameInput, runs, null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        // Calculate true pareto front
        if (allTools || ptfOption) {
        	List<String> tools = new ArrayList<String>();
        	try {
        		System.out.println("Parsing Acapulco results...");
            	parseResults(ACAPULCO_TOOL_NAME, fmNameInput, runs, null);
            	tools.add(ACAPULCO_TOOL_NAME);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	try {
        		System.out.println("Parsing Modagame results...");
            	parseResults(MODAGAME_TOOL_NAME, fmNameInput, runs, null);
            	tools.add(MODAGAME_TOOL_NAME);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	try {
        		System.out.println("Parsing Satibea results...");
            	parseResults(SATIBEA_TOOL_NAME, fmNameInput, runs, null);
            	tools.add(SATIBEA_TOOL_NAME);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	
        	String[] toolsNames = tools.toArray(new String[0]);
        	System.out.println("Generating pareto true front from results (" + runs + " runs)...");
            Map<String, List<String>> pfMap = new HashMap<>();
    		for (int i = 0; i < toolsNames.length; i++) {
    			MergedPTF.fillMaps(fmNameInput, toolsNames[i], pfMap, runs);
    		}
    		
    		try {
				MergedPTF.buildMergePTF(fmNameInput, pfMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		
    		// Calculate hypervolumes
            System.out.println("Calculating hypervolumes using the pareto true front (" + runs + " runs)...");
    		
            try {
    			String ptfSolutionSet = Files.readString(Paths.get(outputPath + "/" + fmNameInput + "-paretotruefront.txt"));
    			double[][] ptf = Results.solutionSetToArray(ptfSolutionSet);
    			
    			for (int i = 0; i < toolsNames.length; i++) {
    				parseResults(toolsNames[i], fmNameInput, runs, ptf);
        		}
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        }
       
        System.out.println("Done!");
	}
	
	private static void parseResults(String sTool, String fmNameInput, int runs, double[][] ptf) {
		 // Parse results
        System.out.println("Parsing results for " + sTool + "...");
        try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        Results results = new Results();
		List<String> resultFilepaths = ParseResults.getAllResultsFilepath(outputPath, sTool, runs);
    	for (int i = 1; i <= runs; i++) {
    		System.out.println(resultFilepaths.get(i-1));
    		results.addRun(i, ParseResults.getResults(resultFilepaths.get(i-1), sTool));
    	}
    	
    	// Serialize the results
        String statsResultsFilename = outputPath + "/" + sTool + "_" + fmNameInput + "_statsResults.dat";
        String runsResultsFilename = outputPath + "/" + sTool + "_" + fmNameInput + "_" + runs + "runs_results.dat"; 
        if (ptf == null) {
        	results.saveResults(statsResultsFilename);
            results.saveRunsResults(runsResultsFilename);
        } else {
        	try {
				results.saveResults(statsResultsFilename, ptf);
				results.saveRunsResults(runsResultsFilename, ptf);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
	}
}
