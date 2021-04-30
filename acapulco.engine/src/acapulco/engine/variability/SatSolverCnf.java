package acapulco.engine.variability;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

/**
 * SAT solving assuming a given String that is already in CNF
 * 
 * @author Daniel Strüber
 * @author Sven Peldszus
 *
 */
public class SatSolverCnf {
	// Maximal number of solutions computed when "getAlLSolutions".
	// -1 = no cap
	private static int MAX_SOLUTION_CAP = 1;

	static Map<String, List<List<String>>> cachedSolutions = new HashMap<>();

	static Map<String, Boolean> cachedSatisfiable = new HashMap<>();

	private static List<String> solution;

	public static List<List<String>> getAllSolutions(String expr) {
		return getAllSolutions(expr, MAX_SOLUTION_CAP);
	}

	public static List<List<String>> getAllSolutions(String expr, int maxSolutions) {
		List<List<String>> result = cachedSolutions.get(expr);
		if (result == null) {
			result = calculateAllSolutions(expr, maxSolutions);
			cachedSolutions.put(expr, result);
		}
		return result;
	}


	private static List<List<String>> calculateAllSolutions(String expr, int maxSolutions) {
		ModelIteratorInfo mii = createIteratorFor(expr);
	
		if (mii == null) {
			return new ArrayList<>();
		}
	
		List<List<String>> result = new ArrayList<>();
	
		int counter = 0;
		try {
			while (mii.mi.isSatisfiable()) {
				int[] model = mii.mi.model();
				List<String> sol = new LinkedList<>();
	
				for (int i : model) {
					if (i > 0) {
						String ps = mii.reverseIndex.get(i);
						if (ps != null) {
							sol.add(ps);
						}
					}
				}
	
				result.add(sol);
	
				counter++;
				if (counter == maxSolutions) {
					break;
				}
			}
			return result;
		} catch (TimeoutException e) {
			throw new RuntimeException("Timeout during evaluation of satisfiability.");
		}
	}

	private static boolean parseCnf(String expr, Set<Set<Literal>> clauses, Set<String> symbols) {
		if (expr == null || expr.isEmpty() || expr.contains("xor("))
			throw new RuntimeException("Misformed input for CNF parsing: " + expr);
	
		Deque<Character> parenthesis = new LinkedList<>();
	
		char[] exprArr = expr.toCharArray();
		boolean foundAndOperator = false;
		boolean foundOrOperator = false;
		boolean mustOnlyContainAndOperators = false;
		boolean mustOnlyContainOrOperators = false;
	
		StringBuffer currentPartialSymbol = new StringBuffer();
		Set<Literal> currentClause = new HashSet<>();
		int sign = 0; // 0 = yet-undefined
	
		for (int i = 0; i < exprArr.length; i++) {
			char ch = exprArr[i];
			switch (ch) {
			case '&':
				if (!parenthesis.isEmpty() && foundOrOperator) {
					if (mustOnlyContainOrOperators) {
						throw new RuntimeException("Misformed input for CNF parsing: " + expr);
					} else {
						mustOnlyContainAndOperators = true;
					}
				} else if (mustOnlyContainOrOperators) {
					throw new RuntimeException("Misformed input for CNF parsing: " + expr);
				}
				foundAndOperator = true;
	
				String symbol = currentPartialSymbol.toString();
				symbols.add(symbol);
				currentClause.add(new Literal(sign == 1, symbol));
				clauses.add(currentClause);
	
				sign = 0;
				currentClause = new HashSet<>();
				currentPartialSymbol = new StringBuffer();
				break;
			case '|':
				if (parenthesis.isEmpty()) {
					if (foundAndOperator || mustOnlyContainAndOperators) {
						throw new RuntimeException("Misformed input for CNF parsing: " + expr);
					} else {
						mustOnlyContainOrOperators = true;
					}
				}
				foundOrOperator = true;
	
				symbol = currentPartialSymbol.toString();
				symbols.add(symbol);
				Literal literal = new Literal(sign == 1, symbol);
				currentClause.add(literal);
	
				sign = 0;
				currentPartialSymbol = new StringBuffer();
				break;
			case '(':
				parenthesis.add(exprArr[i]);
				break;
			case ')':
				if (parenthesis.isEmpty()) {
					throw new RuntimeException("Misformed input for CNF parsing: " + expr);
				} else {
					parenthesis.pop();
				}
				break;
			case '~':
				sign = -1;
				break;
			case '!':
				sign = -1;
				break;
			default:
				if (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_' || ch == '-') {
					if (sign == 0) { // then we did not earlier find a negation
						sign = 1;
					}
					currentPartialSymbol.append(ch);
				}
	
				break;
			}
		}
	
		if (sign != 0) {
			String symbol = currentPartialSymbol.toString();
			symbols.add(symbol);
			currentClause.add(new Literal(sign == 1, symbol));
			clauses.add(currentClause);
		}
	
		return true;
	
	}

	private static Map<String, Integer> getSymbol2IndexMap(Set<String> symbols) {
		Map<String, Integer> list2Index = new HashMap<String, Integer>(symbols.size());
		int counter = 1;
		for (String symbol : symbols) {
			list2Index.put(symbol, Integer.valueOf(counter));
			counter++;
		}
		return list2Index;
	}

	private static int[] convertToArray(Set<Literal> clause, Map<String, Integer> indices) {
		int[] result = new int[clause.size()];
		int counter = 0;
		for (Literal literal : clause) {
			int sign = literal.isPositive() ? 1 : -1;
			String symbol = literal.getSymbol();
			int index = indices.get(symbol).intValue();
			result[counter] = sign * index;
			counter++;
		}
		return result;
	}

	private static class ModelIteratorInfo {
		public ModelIterator mi;
		public Map<Integer, String> reverseIndex;
		public Set<String> symbols;
		public Map<String, Integer> indices;
	}

	private static ModelIteratorInfo createIteratorFor(String expr) {
		ModelIteratorInfo mii = new ModelIteratorInfo();

		Set<Set<Literal>> clauses = new HashSet<>();
		mii.symbols = new HashSet<>();
		parseCnf(expr, clauses, mii.symbols);

		mii.indices = getSymbol2IndexMap(mii.symbols);

		int numberOfVariables = mii.symbols.size();
		int numberOfClauses = clauses.size();

		ISolver solver = SolverFactory.newDefault();
		mii.mi = new ModelIterator(solver);
		mii.mi.newVar(numberOfVariables);
		mii.mi.setExpectedNumberOfClauses(numberOfClauses);

		for (Set<Literal> clause : clauses) {
			int[] clauseArray = convertToArray(clause, mii.indices);
			try {
				mii.mi.addClause(new VecInt(clauseArray));
			} catch (ContradictionException e) {
				throw new RuntimeException("Contradiction in formula: "+expr);
			}
		}

		mii.reverseIndex = new HashMap<>();
		for (Entry<String, Integer> ind : mii.indices.entrySet()) {
			mii.reverseIndex.put(ind.getValue(), ind.getKey());
		}

		return mii;
	}

	public static void main(String[] args) {
		String expr1 = "a | b | c | d | e | f";
		System.out.println(SatSolverCnf.getAllSolutions(expr1));

		String expr2 = "(aa | dsb | cd | dff | ef | ff) & !aa";
		System.out.println(SatSolverCnf.getAllSolutions(expr2));

		String expr3 = "a & ~b";
		System.out.println(SatSolverCnf.getAllSolutions(expr3));
	}
}

class Literal {
	boolean positive;
	String symbol;

	public Literal(boolean positive, String symbol) {
		super();
		this.positive = positive;
		this.symbol = symbol;
	}

	public boolean isPositive() {
		return positive;
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public String toString() {
		return (positive ? "" : "!") + symbol;
	}
}
