/*
 * Copyright 2014 Gustavo García Pascual, Mónica Pinto and Lidia Fuentes
 *
 * This file is part of MO-DAGAME
 * *
 * MO-DAGAME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MO-DAGAME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MO-DAGAME.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.uma.mo_dagame.algorithm.main;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.uma.mo_dagame.algorithm.jmetalcustomization.Experiment;
import org.uma.mo_dagame.algorithm.settings.MoDagameIBEAsettings;
import org.uma.mo_dagame.algorithm.settings.MoDagameMOCELLsettings;
import org.uma.mo_dagame.algorithm.settings.MoDagameMOCHCsettings;
import org.uma.mo_dagame.algorithm.settings.MoDagameNSGAIIsettings;
import org.uma.mo_dagame.algorithm.settings.MoDagamePAESsettings;
import org.uma.mo_dagame.algorithm.settings.MoDagameSPEA2settings;
import org.uma.mo_dagame.feature_models.Configuration;
import org.uma.mo_dagame.feature_models.ConfigurationParser;
import org.uma.mo_dagame.feature_models.FeatureModel;
import org.uma.mo_dagame.feature_models.ObjectivesValuesParser;
import org.uma.mo_dagame.feature_models.SxfmParser;

import acapulco.algorithm.instrumentation.ToolInstrumenter;
import acapulco.algorithm.termination.StoppingCondition;
import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.util.JMException;

/**
 * Class implementing a typical experimental study. Five algorithms are
 * compared when solving the ZDT, DTLZ, and WFG benchmarks, and the hypervolume,
 * spread and additive epsilon indicators are used for performance assessment.
 */
public class MoDagameStudy extends Experiment {

    private static final String FILE_SEPARATOR = File.separator;
    private static final int NUMBER_OF_THREADS = 4;

    private StoppingCondition stoppingCondition;
	private int stoppingValue;
	public ToolInstrumenter toolInstrumenter;
	
    public MoDagameStudy(StoppingCondition stoppingCondition, int stoppingValue) {
    	super();
    	this.stoppingCondition = stoppingCondition;
    	this.stoppingValue = stoppingValue;
    }
    
    /**
     * Configures the algorithms in each independent run
     *
     * @param problemName  The problem to solve
     * @param problemIndex
     * @throws ClassNotFoundException
     */
    public void algorithmSettings(String problemName, int problemIndex, Algorithm[] algorithm)
            throws ClassNotFoundException {
        try {
            int numberOfAlgorithms = algorithmNameList_.length;

            HashMap[] parameters = new HashMap[numberOfAlgorithms];

            for (int i = 0; i < numberOfAlgorithms; i++) {
                parameters[i] = new HashMap();
            } // for

            if (paretoFrontFile_[problemIndex] != null &&
                    !paretoFrontFile_[problemIndex].equals("")) {
                for (int i = 0; i < numberOfAlgorithms; i++) {
                    parameters[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
                }
            }

            String modelFile = experimentBaseDirectory_ + FILE_SEPARATOR + "models" +
                    FILE_SEPARATOR + problemName + ".xml";
            String seedFile = experimentBaseDirectory_ + FILE_SEPARATOR + "seeds" + FILE_SEPARATOR +
                    problemName + ".csv";
            String objFile = experimentBaseDirectory_ + FILE_SEPARATOR + "models" +
                    FILE_SEPARATOR + problemName + ".obj";

            FeatureModel fm = SxfmParser.parse(modelFile);
            ObjectivesValuesParser.parse(objFile, fm);
            Configuration seed = ConfigurationParser.parse(seedFile, fm);

            algorithm[0] =
                    new MoDagameNSGAIIsettings(problemName, fm, seed).configure(parameters[0]);
            

            //Configure an algorithm run with an instrumenter and termination condition type
    		this.toolInstrumenter = new ToolInstrumenter(fm.getNumberOfObjectives(), 0, "MoDagame", "modagame-results", 1);
    		
            algorithm[1] = new MoDagameIBEAsettings(problemName, fm, seed, this.stoppingCondition, this.stoppingValue, toolInstrumenter).configure(parameters[1]);
    		//algorithm[1] = new MoDagameIBEAsettings(problemName, fm, seed, this.stoppingCondition, this.stoppingValue, toolInstrumenter).configure();
            
            //toolInstrumenter.serialiseAccumulator();
            
            
            algorithm[2] =
                    new MoDagameMOCHCsettings(problemName, fm, seed).configure(parameters[2]);
            algorithm[3] =
                    new MoDagameMOCELLsettings(problemName, fm, seed).configure(parameters[3]);
            algorithm[4] = new MoDagamePAESsettings(problemName, fm, seed).configure(parameters[4]);
            algorithm[5] =
                    new MoDagameSPEA2settings(problemName, fm, seed).configure(parameters[5]);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MoDagameStudy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MoDagameStudy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMException ex) {
            Logger.getLogger(MoDagameStudy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MoDagameStudy.class.getName()).log(Level.SEVERE, null, ex);
        }
    } // algorithmSettings

    /**
     * Main method
     *
     * @param args
     * @throws JMException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
    	//args = new String[3];
    	//args[0] = "BerkeleyDBMemory";
    	//args[1] = "C:/git/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases";
    	//args[2] = "1";
 
        // Check arguments
        if (args.length != 5) {
            System.err.println("Usage: MoDagameStudy experimentName baseDirectory independentRuns stoppingCondition('time'' or 'evols') stoppingValue");
            return;
        }

        StoppingCondition stoppingCondition = StoppingCondition.EVOLUTIONS;;
        if (args[3].equalsIgnoreCase("time")) {
        	stoppingCondition = StoppingCondition.TIME;
        } else if (args[3].equalsIgnoreCase("evols")) {
        	stoppingCondition = StoppingCondition.EVOLUTIONS;
        }
        
        
        MoDagameStudy exp = new MoDagameStudy(stoppingCondition, Integer.parseInt(args[4]));
        exp.experimentName_ = args[0];
        exp.experimentBaseDirectory_ = args[1] + FILE_SEPARATOR + exp.experimentName_;

        exp.algorithmNameList_ = new String[]{"MoDagameNSGAII", "MoDagameIBEA", "MoDagameMOCHC", "MoDagameMOCell", "MoDagamePAES", "MoDagameSPEA2"};

        File modelsFolder = new File(exp.experimentBaseDirectory_ +
                FILE_SEPARATOR + "models");
        String[] files = modelsFolder.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }

        });
        for (int i = 0; i < files.length; i++) {
            files[i] = files[i].substring(0, files[i].lastIndexOf("."));
        }

        exp.problemList_ = files;

        // Generate Pareto front automatically
        exp.paretoFrontFile_ = new String[exp.problemList_.length];
        exp.paretoFrontDirectory_ = "";

        // Time Indicator is a custom one implemented as an extension of the
        // jMetal Experiment class
        exp.indicatorList_ = new String[]{"HV", "GD", "TIME"};

        int numberOfAlgorithms = exp.algorithmNameList_.length;

        exp.algorithmSettings_ = new Settings[numberOfAlgorithms];

        exp.independentRuns_ = Integer.parseInt(args[2]);

        exp.initExperiment();

        // Run the experiments
        // JABIER try and catch
        try {
			exp.runExperiment(NUMBER_OF_THREADS, true);
		} catch (JMException e) {
			e.printStackTrace();
		}

        exp.generateQualityIndicators(8);

        // Generate latex tables
        exp.generateLatexTables();

        // Configure the R scripts to be generated
        int rows = 1;
        int columns = 1;
        String prefix;
        String[] problems;
        boolean notch = false;

        int i = 0;
        do {
            int j = i + 5 <= exp.problemList_.length ? i + 5 : exp.problemList_.length;
            problems = new String[j - i];
            for (int k = 0; k < j - i; k++) {
                problems[k] = exp.problemList_[i + k];
            }

            prefix = new String("PROBLEMS-" + i + "-" + (j - 1));
            exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch, exp);
            exp.generateRWilcoxonScripts(problems, prefix, exp);

            i = j;
        } while (i < exp.problemList_.length);

        // Applying Friedman test
//        Friedman test = new Friedman(exp);
//        test.executeTest("HV");
//        test.executeTest("GD");
//        test.executeTest("TIME");

        // Generate specifications file for Matlab A12 script
        exp.generateExperimentSpecifications();
    } // main

    private void generateExperimentSpecifications() {
        File f = new File(experimentBaseDirectory_ + FILE_SEPARATOR + "specifications");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(f));
            writer.println(experimentName_);
            String algorithms = algorithmNameList_[0];
            for (int i = 1; i < algorithmNameList_.length; i++) {
                algorithms += " " + algorithmNameList_[i];
            }
            writer.println(algorithms);
            String problems = problemList_[0];
            for (int i = 1; i < problemList_.length; i++) {
                problems += " " + problemList_[i];
            }
            writer.println(problems);
            if (indicatorList_.length > 0) {
                String qIndic = indicatorList_[0];
                for (int i = 1; i < indicatorList_.length; i++) {
                    qIndic += " " + indicatorList_[i];
                }
                writer.println(qIndic);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // StandardStudy


