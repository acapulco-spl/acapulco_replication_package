package acapulco.tool.executor;

import acapulco.algorithm.termination.StoppingCondition;
import picocli.CommandLine.Option;

/**
 * Define an abstract executor to reuse across all the tools that can run search
 * over feature models. This enforces the same kind of signature for all the
 * tools extending this class
 * 
 * 
 * Once implemented, the tool will be runnable with the: -sc flag to support
 * stopping condition as TIME or EVOLUTIONS -sv flag to support the value for
 * the stopping condition. Expressed as an integer -fm the path to the feature
 * model to pass to the tool
 *
 */
public abstract class AbstractExecutor implements Runnable {

	@Option(names = { "-sc",
			"--stopping-condition" }, required = true, description = "Supported values: ${COMPLETION-CANDIDATES}")
	protected StoppingCondition stoppingCondition;

	@Option(names = { "-sv", "--stopping-value" }, required = true, description = "Stopping condition integer value")
	protected Integer stoppingValue;

	@Option(names = { "-fm", "--feature-model" }, required = true, description = "Feature model path")
	protected String featureModel;

	@Option(names = { "-d", "--debug" }, required = false, description = "Debug mode")
	protected boolean debugMode;

	@Override
	public abstract void run();
}
