package acapulco;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;
import org.eclipse.emf.henshin.model.Unit;

import jmetal.core.Solution;
import jmetal.encodings.variable.ArrayInt;
import jmetal.operators.crossover.Crossover;
import jmetal.util.JMException;

/**
 *
 * @author chris
 */
public class aCaPulCO_Crossover extends Crossover {

	private Double crossoverProbability_ = null;
	private aCaPulCO_Mutation mutation;

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	public aCaPulCO_Crossover(HashMap<String, Object> parameters, aCaPulCO_Mutation mutation) {
		super(parameters);
		if (parameters.get("probability") != null)
			crossoverProbability_ = (Double) parameters.get("probability");
		this.mutation = mutation;
	} // SinglePointCrossover

	/**
	 * Constructor Creates a new instance of the single point crossover operator
	 */
	// public SinglePointCrossover(Properties properties) {
	// this();
	// } // SinglePointCrossover


	@Override
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;
		Solution[] offSpring;
		offSpring = doCrossover(crossoverProbability_, parents[0], parents[1]);
		
		for (int i = 0; i < offSpring.length; i++) {
			offSpring[i].setCrowdingDistance(0.0);
			offSpring[i].setRank(0);
		}
		return offSpring;
	}
	/**
	 * Perform the crossover operation.
	 * 
	 * @param probability Crossover probability
	 * @param parent1     The first parent
	 * @param parent2     The second parent
	 * @return An array containig the two offsprings
	 * @throws JMException
	 */
	public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {
		Solution[] offSpring = new Solution[2];
		offSpring[0] = mate(parent1, parent2);
		offSpring[1] = mate(parent2, parent1);
		return offSpring;
	} // execute
	
	private Solution mate(Solution parent1, Solution parent2) {
		Solution child = new Solution(parent1);
		int[] appliedChildArray = ((ArrayInt) child.getDecisionVariables()[1]).array_;
		Set<Integer> appliedChildren = new HashSet<Integer>(appliedChildArray.length);
		for (int i : appliedChildArray)
		    appliedChildren.add(i);
		
		ArrayInt appliedParent2 = (ArrayInt) parent2.getDecisionVariables()[1];
	
		if (appliedParent2 != null && appliedParent2.array_ != null) {			
			for (int ruleIndex : appliedParent2.array_) {
				if (!appliedChildren.contains(ruleIndex)) {
					mutation.applyCpcoRuleToSolution(child, ruleIndex);
				}
			}
		}
		return child;
	}


}