package acapulco.engine.variability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ClauseCollector;
import aima.core.logic.propositional.visitors.ConvertToCNF;
import aima.core.logic.propositional.visitors.SymbolCollector;

/**
 * 
 * @author Daniel Strüber
 * @author Sven Peldszus
 *
 */
public class SatSolver {
	static Map<String,List<List<String>>> cachedSolutions = new HashMap<>();
	
	static Map<String,Boolean> cachedSatisfiable = new HashMap<>();
	
	private static List<String> solution;

	public static ISolver createModelIterator(String expr, Map<Integer, String> symbolsToIndices) {
		Sentence cnf = ConvertToCNF.convert(FeatureExpression.getExpr(expr));

		Set<PropositionSymbol> symbols = SymbolCollector.getSymbolsFrom(cnf);
		Set<Clause> clauses = ClauseCollector.getClausesFrom(cnf);
		Map<PropositionSymbol, Integer> indices = getSymbol2IndexMap(symbols);
		for (PropositionSymbol symbol : symbols) {
			symbolsToIndices.put(indices.get(symbol), symbol.getSymbol());
		}

		int numberOfVariables = symbols.size();
		int numberOfClauses = clauses.size();

		ISolver solver = new ModelIterator(SolverFactory.newDefault());
		solver.setDBSimplificationAllowed(false);
		solver.newVar(numberOfVariables);
		solver.setExpectedNumberOfClauses(numberOfClauses);

		for (Clause clause : clauses) {
			if (clause.isFalse()) {
				return null;
			}
			if (!clause.isTautology()) {
				int[] clauseArray = convertToArray(clause, indices);
				try {
					solver.addClause(new VecInt(clauseArray));
				} catch (ContradictionException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return solver;
	}


	private static boolean checkSatisfiable(String expr) {
		Boolean result = cachedSatisfiable.get(expr);
		if (result == null) {
			Sentence sentence = FeatureExpression.getExpr(expr);
			result = isSatisfiable(sentence);
			cachedSatisfiable.put(expr, result);
		}
		return result;
	}
	
	public static Boolean isSatisfiable(String expr) {
		Sentence cnf = FeatureExpression.getExpr(expr);
		return isSatisfiable(cnf);
	}

	
	public static  Boolean isSatisfiable(Sentence expr) {
		Sentence cnf = ConvertToCNF.convert(expr);
		Set<PropositionSymbol> symbols = SymbolCollector.getSymbolsFrom(cnf);
		Set<Clause> clauses = ClauseCollector.getClausesFrom(cnf);
		Map<PropositionSymbol, Integer> indices = getSymbol2IndexMap(symbols);

		int numberOfVariables = symbols.size();
		int numberOfClauses = clauses.size();

		ISolver solver = SolverFactory.newGreedySolver();

		solver.newVar(numberOfVariables);
		solver.setExpectedNumberOfClauses(numberOfClauses);

		for (Clause clause : clauses) {
			if (clause.isFalse()) {
				return Boolean.FALSE;
			}
			if (!clause.isTautology()) {
				int[] clauseArray = convertToArray(clause, indices);
				try {
					solver.addClause(new VecInt(clauseArray));
				} catch (ContradictionException e) {
//					e.printStackTrace();
					return Boolean.FALSE;
				}
			}
		}

		try {

			boolean satisfiable = solver.isSatisfiable();
			if (satisfiable) {
				solution = new LinkedList<>();
				int[] model = solver.findModel();
				for (PropositionSymbol key : indices.keySet()) {
					int index = indices.get(key);
					for (int i : model) {
						if (i > 0 && i == index) {
							solution.add(key.getSymbol());
						}
					}
				}
			}
			return Boolean.valueOf(satisfiable);
		} catch (TimeoutException e) {
			throw new RuntimeException("Timeout during evaluation of satisfiability.");
		}
	}

	public Boolean isContradiction(Sentence expr) {
		return isSatisfiable(expr) ? Boolean.FALSE : Boolean.TRUE;
	}

	public Boolean isContradiction(String expr) {
		return isSatisfiable(expr).booleanValue() ? Boolean.FALSE : Boolean.TRUE;
	}

	private static Map<PropositionSymbol, Integer> getSymbol2IndexMap(Set<PropositionSymbol> symbols) {
		Map<PropositionSymbol, Integer> list2Index = new HashMap<PropositionSymbol, Integer>(symbols.size());
		int counter = 1;
		for (PropositionSymbol symbol : symbols) {
			list2Index.put(symbol, Integer.valueOf(counter));
			counter++;
		}
		return list2Index;
	}

	private static int[] convertToArray(Clause clause, Map<PropositionSymbol, Integer> indices) {
		Set<Literal> literals = clause.getLiterals();
		int[] result = new int[literals.size()];
		int counter = 0;
		for (Literal literal : literals) {
			int sign = literal.isPositiveLiteral() ? 1 : -1;
			PropositionSymbol symbol = literal.getAtomicSentence();
			int index = indices.get(symbol).intValue();
			result[counter] = sign * index;
			counter++;
		}
		return result;
	}

	public List<String> getSolution() {
		return solution;
	}

	public static List<List<String>> getAllSolutions(String expr) {
		List<List<String>> result = cachedSolutions.get(expr);
		if (result == null) {
			Sentence sentence = FeatureExpression.getExpr(expr);
			result = calculateAllSolutions(sentence);
			cachedSolutions.put(expr, result);
		}
		return result;
	}

	private static List<List<String>> calculateAllSolutions(Sentence expr) {
//		Sentence cnf = ConvertToCNF.convert(expr);
		Sentence cnf = expr; // Assume sentence is already in CNF to avoid complexity of generating CNF.
		
		Set<PropositionSymbol> symbols = SymbolCollector.getSymbolsFrom(cnf);
		Set<Clause> clauses = ClauseCollector.getClausesFrom(cnf);
		Map<PropositionSymbol, Integer> indices = getSymbol2IndexMap(symbols);

		int numberOfVariables = symbols.size();
		int numberOfClauses = clauses.size();

		ISolver solver = SolverFactory.newDefault();
        ModelIterator mi = new ModelIterator(solver);
		mi.newVar(numberOfVariables);
		mi.setExpectedNumberOfClauses(numberOfClauses);

		List<List<String>> result = new ArrayList<>();
		for (Clause clause : clauses) {
			if (clause.isFalse()) {
				return result;
			}
			if (!clause.isTautology()) {
				int[] clauseArray = convertToArray(clause, indices);
				try {
					mi.addClause(new VecInt(clauseArray));
				} catch (ContradictionException e) {
//					e.printStackTrace();
					return result;
				}
			}
		}

		Map<Integer, PropositionSymbol> reverseIndex = new HashMap<>();
		for (Entry<PropositionSymbol, Integer> ind : indices.entrySet()) {
			reverseIndex.put(ind.getValue(), ind.getKey());
		}

		try {
			while (mi.isSatisfiable()) {
				int[] model = mi.model();
				List<String> sol = new LinkedList<>();
				
//				for (int i : model) {
//					if (i > 0) {
//						PropositionSymbol ps = reverseIndex.get(i);
//						if (ps != null) {
//							sol.add(ps.getSymbol());
//						}
//					}
//				}
//				/*
//				 * Steffen: Swapped this around to reduce the number of loops within loops. That gave us a little bit of extra performance, though not a huge amount.
				for (PropositionSymbol key : indices.keySet()) {
					int index = indices.get(key);
					for (int i : model) {
						if (i > 0 && i == index) {
							sol.add(key.getSymbol());
						}
					}
				}
//				*/
				result.add(sol);
			}
			return result;
		} catch (TimeoutException e) {
			throw new RuntimeException("Timeout during evaluation of satisfiability.");
		}
	}

	public static void main(String[] args) {
		String expr1 = "a | b | c | d | e | f";
		System.out.println(SatSolver.checkSatisfiable(expr1));
		System.out.println(SatSolver.getAllSolutions(expr1));
			
		String expr2 = "a & ~a";
		System.out.println(SatSolver.checkSatisfiable(expr2));
		System.out.println(SatSolver.getAllSolutions(expr2));	
	}

}