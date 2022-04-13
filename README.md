# Consistency-Preserving Operators for Efficient Product Line Configuration

**Replication package**  [![DOI](https://zenodo.org/badge/306005002.svg)](https://zenodo.org/badge/latestdoi/306005002)

This repository contains all the resources and artifacts of the paper entitled *We're Not Gonna Break It! Consistency-Preserving Operators for Efficient Product Line Configuration*.

Consistency-preserving configuration operators (CPCOs) are mutation  operators  that  bundle  coherent  sets of  changes in a feature model configuration,  specifically,  the  activation  or  deactivation  of  a particular feature together with other (de)activations that are needed to preserve validity.


## Project structure
This repository is organized into three parts:
- **aCaPulCO**: A  new  search-based optimization tool  based  on  the CPCOs. aCaPulCO implements the IBEA algorithm with custom mutation and crossover operators that apply the CPCOs to a binary vector-based encoding o fconfigurations. CPCOs are represented as transformation rules in the model transformation language [Henshin](https://www.eclipse.org/henshin/).
- **Comparator tools**: The two state-of-the-art tools for multi-objective optimization in software product lines: [MODAGAME](http://caosd.lcc.uma.es/famware/tools/) and [SATIBEA](https://research.henard.net/SPL/ICSE_2015/). This contains the adaptations performed in terms of quality attributes, algorithms and parameters settings, in order to enable a fair comparison between the tools.
- **Results**: Complete experimentation results.

The image below explains the project structure ([editable source](https://docs.google.com/presentation/d/1zrxTfQnYK6VBkblaSKF1hr0gAGKRlGz8-E12-qcwU3g/edit#slide=id.p)).
<img src="acapulco_structure.png" alt="drawing" width="800"/>

## Requirements
- Java JDK 13+
- Eclipse 2020 (4.18.0)+
- Python 3+ (only for statistical analysis tests)

## Execution and experiment replication
1. Clone/Download this repository.
2. Import all projects into Eclipse.
3. Dependencies.
    - [Eclipse Henshin](https://www.eclipse.org/henshin/) is needed. Version 1.7.0 was used. The required plugin is `org.eclipse.emf.henshin.model`.
    - [Eclipse Xtend](https://www.eclipse.org/xtend) is needed to build the `acapulco.cpcogen` project. Version 2.26.0 was used.
4. The entrypoint is located at *acapulco.pipeline.PipelineRunnerExternal*. Execute it with the following program arguments:
    - To generate all required artifacts for the tools (models, quality attributes,...) including our CPCOs for aCaPulCO:

        `-p -fm <feature_model_name>`

        Example: `-p -fm WeaFQAs` (without the extension file).

        This takes the feature model located in the `input` folder and generates all artifacts in the `generated` folder, both at the *acapulco.pipeline* project.
    
    - To execute the experiments for a specific tool (aCaPulCO, MODAGAME, or SATIBEA):

        `-fm <feature_model_name> -t <tool_name> -sc <stopping_condition> -sv <stopping_value> -r <runs>`

        where:

        `<feature_model_name>` is the name of the feature model without the extension file (as in the previous use case).

        `<tool_name>` can be `acapulco`, `modagame`, or `satibea`.

        `<stopping_condition>`: use `evols` for a number of evolutions (default), or `time` for a specific timeout.

        `<stopping_value>` is an integer number: in case of `evols` as stopping condition it represents the number of evolutions (default 50), in case of `time` it represents the timeout in milliseconds.

        `<runs>`: an integer specifing the number of runs to be performed.

        Example: `-fm WeaFQAs -t acapulco -sc evols -sv 50 -r 30`

        This will execute the optimization search and generate raw results in the `output` folder.

    - Generate the metrics (hypervolume, generational distance, and execution times) and calculate statistics (medians, means, and standard deviations) from the raw results. This last step should be done after executing the search with all tools: 
       
        `-fm <feature_model_name> -ptf -r <runs>`

        Example: `-fm WeaFQAs -ptf -r 30`.
        
        This will generate the final results in the `output` folder. Two result files are generated for each tool:

        `acapulco_weafqas_30runs_results.dat` contains the results for each run performed.

        `acapulco_weafqas_statsResults.dat` contains the summary of the results with statistics.
        
    - To execute the statistical analysis tests in order to compare the HV-values and the runtime of the three algorithms, use the Python scripts `stests.py` located at *acapulco.statisticstests*:
  
        `python stests.py`

        This applies the Mann-Whitney  U  test using the *thescipy.stats.mannwhitneyu* package of [SciPy.org](https://www.scipy.org/). The script reports p-values,  where  a  value  below  0.05  means  that  the comparison is statistically significant at the 95% confidence level.
