package acapulco.engine.variability;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;

public class ExtendedSentence extends Sentence {
	public Sentence sentence;
	private Map<String, PropositionSymbol> symbols;
	
	public Map<String, PropositionSymbol> getSymbols() {
		return symbols;
	}
	public ExtendedSentence(Sentence sentence) {
		this.sentence = sentence;
		this.symbols = retrieveSymbols(sentence);
	}
	public Connective getConnective() {
		return sentence.getConnective();
	}
	public int getNumberSimplerSentences() {
		return sentence.getNumberSimplerSentences();
	}
	public Sentence getSimplerSentence(int offset) {
		return sentence.getSimplerSentence(offset);
	}
	public boolean isNotSentence() {
		return sentence.isNotSentence();
	}
	public int hashCode() {
		return sentence.hashCode();
	}
	public boolean isAndSentence() {
		return sentence.isAndSentence();
	}
	public boolean isOrSentence() {
		return sentence.isOrSentence();
	}
	public boolean isImplicationSentence() {
		return sentence.isImplicationSentence();
	}
	public boolean isBiconditionalSentence() {
		return sentence.isBiconditionalSentence();
	}
	public boolean isPropositionSymbol() {
		return sentence.isPropositionSymbol();
	}
	public boolean isUnarySentence() {
		return sentence.isUnarySentence();
	}
	public boolean isBinarySentence() {
		return sentence.isBinarySentence();
	}
	public <A, R> R accept(PLVisitor<A, R> plv, A arg) {
		return sentence.accept(plv, arg);
	}
	public boolean equals(Object obj) {
		return sentence.equals(obj);
	}
	public String bracketSentenceIfNecessary(Connective parentConnective, Sentence childSentence) {
		return sentence.bracketSentenceIfNecessary(parentConnective, childSentence);
	}
	public String toString() {
		return sentence.toString();
	}

	private static HashMap<String, PropositionSymbol> retrieveSymbols(Sentence sentence, HashMap<String, PropositionSymbol> result) {
		// Rewritten to remove recursion
		List<Sentence> toProcess = new LinkedList<>();
		toProcess.add(sentence);
		
		while (!toProcess.isEmpty()) {
			Sentence s = toProcess.remove(0);
			if (s instanceof PropositionSymbol) {
				result.put(s.toString(), (PropositionSymbol) s);
			} else {
				for (int i=0; i < s.getNumberSimplerSentences(); i++) {
					toProcess.add(s.getSimplerSentence(i));
				}
			}
		}
				
//		for (int i=0; i < sentence.getNumberSimplerSentences(); i++) {
//			Sentence s = sentence.getSimplerSentence(i);
//			if (s instanceof PropositionSymbol) {
//				result.put(s.toString(), (PropositionSymbol) s);
//			} else {
//				retrieveSymbols(s, result);
//			}
//		}
		
		return result;
	}

	public static Map<String, PropositionSymbol> retrieveSymbols(Sentence sentence) {
		HashMap<String, PropositionSymbol> result = new HashMap<String, PropositionSymbol>();
		return retrieveSymbols(sentence, result);
	}
}
