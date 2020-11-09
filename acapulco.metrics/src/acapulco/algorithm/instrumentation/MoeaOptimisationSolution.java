package acapulco.algorithm.instrumentation;

import org.moeaframework.core.Solution;

public class MoeaOptimisationSolution extends Solution {

  public MoeaOptimisationSolution(MoeaOptimisationSolution moeaOptimisationSolution) {
    super(moeaOptimisationSolution);
  }

  public MoeaOptimisationSolution(int numberOfObjectives, int numberOfConstraints) {
    super(1, numberOfObjectives, numberOfConstraints);
  }

  public MoeaOptimisationSolution copy() {
    return new MoeaOptimisationSolution(this);
  }

  public String toString() {

    StringBuilder sb = new StringBuilder();

    fitnessLoop(sb, getObjectives());
    fitnessLoop(sb, getConstraints());

    return sb.toString();
  }

  private StringBuilder fitnessLoop(StringBuilder sb, double[] vector) {

    if (vector.length > 0) {

      sb.append("[");

      for (int i = 0; i < vector.length; i++) {
        sb.append(vector[i]);

        if (i < vector.length - 1) {
          sb.append(",");
        }
      }

      sb.append("]");
    }
    return sb;
  }
}
