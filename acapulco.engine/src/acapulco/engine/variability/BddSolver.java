package acapulco.engine.variability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.juliasoft.beedeedee.bdd.Assignment;
import com.juliasoft.beedeedee.bdd.BDD;
import com.juliasoft.beedeedee.factories.Factory;

import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;

public class BddSolver {

	static Map<String, List<List<String>>> cachedSolutions = new HashMap<>();

	public static List<List<String>> getAllSolutions(String expr) {
		List<List<String>> result = cachedSolutions.get(expr);
		if (result == null) {
			ExtendedSentence sentence = FeatureExpression.getExpr(expr);
			result = calculateAllSolutions(sentence);
			cachedSolutions.put(expr, result);
		}
		return result;
	}

	private static List<List<String>> calculateAllSolutions(ExtendedSentence expr) {
		Sentence cnf = expr.sentence; // Assume sentence is already in CNF to avoid complexity of generating CNF.

		List<PropositionSymbol> symbols = new ArrayList<>(expr.getSymbols().values()); // SymbolCollector.getSymbolsFrom(cnf);
		List<Clause> clauses = collectClausesFrom(cnf); // ClauseCollector.getClausesFrom(cnf);

		int numberOfVariables = symbols.size();

		Factory factory = Factory.mk(numberOfVariables, numberOfVariables);
		Map<PropositionSymbol, Integer> symbol2index = getSymbol2IndexMap(symbols);
		Map<PropositionSymbol, BDD> symbol2bdd = new HashMap<>();
		symbol2index.entrySet().forEach(s -> symbol2bdd.put(s.getKey(), factory.makeVar(s.getValue())));

		BDD bdd = clause2bdd(clauses.get(0), symbol2bdd);
		for (int i = 1; i < clauses.size(); i++) {
			BDD nextClause = clause2bdd(clauses.get(i), symbol2bdd);
			bdd = bdd.andWith(nextClause);
		}

		List<List<String>> result = new ArrayList<>();
		for (Assignment ass : bdd.allSat()) {
			List<String> partialResult = new ArrayList<>();
			List<Integer> unbound = new ArrayList<>();

			for (int i = 0; i < symbols.size(); i++) {
				try {
					if (ass.holds(i + 1))
						partialResult.add(symbols.get(i).getSymbol());
				} catch (IndexOutOfBoundsException e) {
					unbound.add(i + 1);
				}
			}

			if (unbound.isEmpty()) {
				result.add(partialResult);
			} else {
				result.addAll(deriveCompleteResults(partialResult, unbound, symbols));
			}
		}
		return result;
	}

	private static List<List<String>> deriveCompleteResults(List<String> partialResult, List<Integer> unbound,
			List<PropositionSymbol> symbols) {
		List<List<String>> result = new ArrayList<>();
		deriveCompleteResults(result, partialResult, unbound, symbols);
		return result;
	}

	private static void deriveCompleteResults(List<List<String>> aggregateResult, List<String> currentPartialResult, List<Integer> unbound,
			List<PropositionSymbol> symbols) {
		if (unbound.isEmpty()) {
			aggregateResult.add(currentPartialResult);
		} else {
			List<Integer> newUnbound = new ArrayList<>(unbound);
			List<String> newPartialResultPos = new ArrayList<>(currentPartialResult);
			Integer unbound0 = newUnbound.get(0);
			newUnbound.remove(0); // removes at position 0, I checked it
			newPartialResultPos.add(symbols.get(unbound0-1).toString());
			deriveCompleteResults(aggregateResult, currentPartialResult, newUnbound, symbols);
			deriveCompleteResults(aggregateResult, newPartialResultPos, newUnbound, symbols);
		}
	}

	private static BDD clause2bdd(Clause clause, Map<PropositionSymbol, BDD> symbol2bdd) {
		Set<Literal> literals = clause.getLiterals();
		BDD result = null;
		for (Literal literal : literals) {
			PropositionSymbol symbol = literal.getAtomicSentence();
			BDD bdd = symbol2bdd.get(symbol);
			if (!literal.isPositiveLiteral()) {
				bdd = bdd.not();
			}
			
			if (result == null) {
				result = bdd;
			} else {
				result = result.or(bdd);
			}
		}
		return result;
	}

	private static List<Clause> collectClausesFrom(Sentence cnf) {
		List<Clause> result = new ArrayList<>();

		List<Sentence> toProcess = new LinkedList<>();
		toProcess.add(cnf);

		while (!toProcess.isEmpty()) {
			Sentence currentSentence = toProcess.remove(0);

			if (currentSentence.isAndSentence()) {
				toProcess.add(currentSentence.getSimplerSentence(0));
				toProcess.add(currentSentence.getSimplerSentence(1));
			} else if (currentSentence.isOrSentence()) {
				// Collect literals and wrap in clause
				result.add(new Clause(collectOrLiterals(currentSentence)));
			} else if (currentSentence.isUnarySentence()) {
				if (!currentSentence.getSimplerSentence(0).isPropositionSymbol()) {
					throw new IllegalStateException("Sentence is not in CNF: " + currentSentence);
				}

				Literal negativeLiteral = new Literal((PropositionSymbol) currentSentence.getSimplerSentence(0), false);
				result.add(new Clause(negativeLiteral));
			} else if (currentSentence.isPropositionSymbol()) {
				Literal positiveLiteral = new Literal((PropositionSymbol) currentSentence);
				result.add(new Clause(positiveLiteral));
			} else {
				throw new IllegalStateException("Sentence is not in CNF: " + currentSentence);
			}
		}

		return result;
	}

	private static List<Literal> collectOrLiterals(Sentence orSentence) {
		List<Literal> result = new ArrayList<>();
		List<Sentence> toProcess = new LinkedList<>();
		toProcess.add(orSentence);

		while (!toProcess.isEmpty()) {
			Sentence currentSentence = toProcess.remove(0);
			if (currentSentence.isOrSentence()) {
				toProcess.add(currentSentence.getSimplerSentence(0));
				toProcess.add(currentSentence.getSimplerSentence(1));
			} else if (currentSentence.isUnarySentence()) {
				if (!currentSentence.getSimplerSentence(0).isPropositionSymbol()) {
					throw new IllegalStateException("Sentence is not in CNF: " + currentSentence);
				}

				Literal negativeLiteral = new Literal((PropositionSymbol) currentSentence.getSimplerSentence(0), false);
				result.add(negativeLiteral);
			} else if (currentSentence.isPropositionSymbol()) {
				Literal positiveLiteral = new Literal((PropositionSymbol) currentSentence);
				result.add(positiveLiteral);
			} else {
				throw new IllegalStateException("Sentence is not in CNF: " + currentSentence);
			}
		}

		return result;
	}

	private static Map<PropositionSymbol, Integer> getSymbol2IndexMap(List<PropositionSymbol> symbols) {
		Map<PropositionSymbol, Integer> list2Index = new HashMap<PropositionSymbol, Integer>(symbols.size());
		int counter = 1;
		for (PropositionSymbol symbol : symbols) {
			list2Index.put(symbol, Integer.valueOf(counter));
			counter++;
		}
		return list2Index;
	}
	
	public static void main(String[] args) {
		String expr1 = "a | b | c | d | e | f";
		System.out.println(BddSolver.getAllSolutions(expr1));
		

		expr1 = "a & b & c & d & e & f";
		System.out.println(BddSolver.getAllSolutions(expr1));
	}

}
