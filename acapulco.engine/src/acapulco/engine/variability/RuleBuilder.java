package acapulco.engine.variability;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.henshin.model.Annotation;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Mapping;
import org.eclipse.emf.henshin.model.ModelElement;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

public class RuleBuilder {
	private Rule inputRule;
	private Map<Node, BitSet> compiledPCs;
	private BitSet configuration;
	
	public RuleBuilder(Rule rule, Map<Node, BitSet> compiledPCs, BitSet configuration) {
		fixInconsistencies(rule);
		this.inputRule = rule;
		this.compiledPCs = compiledPCs;
		this.configuration = configuration;
	}

	public ConfigurationSearchOperator buildRule() {
		return this.buildRule(true);
	}
	
	public ConfigurationSearchOperator buildRule(boolean isVBRule) {
		Optional<Node> rootNode = inputRule.getLhs().getNodes().stream().filter(node -> { return !node.getAttributes().isEmpty(); }).findAny();
		if (rootNode.isEmpty()) {
			throw new IllegalArgumentException("VB rule without root decision.");
		}
		
		// Assuming there's only one attribute
		ConfigurationSearchOperator result = new ConfigurationSearchOperator(rootNode.get().getType(), Boolean.valueOf(rootNode.get().getAttributes().get(0).getValue()));
		for (Node decisionNode : inputRule.getRhs().getNodes()) {
			if ((!isVBRule) || pcFulfilled(decisionNode)) {
				result.addDecision(decisionNode.getType(), Boolean.valueOf(decisionNode.getAttributes().get(0).getValue()));
			}
		}
		
		return result;
	}

	private boolean pcFulfilled(ModelElement n) {
		if (n instanceof Node) {
			return compiledPCs.get(n).intersects(configuration);
		} else {
			return true;
		}
	}

	private void fixInconsistencies(Rule rule) {
		// Per definition, mapped nodes must have the same presence condition
		// in the LHS and the RHS.
		for (Mapping mapping : rule.getMappings()) {

			String pcOrig = getPresenceCondition(mapping.getOrigin());
			String pcImg = getPresenceCondition(mapping.getImage());

			if (pcOrig != null) {
				if (!pcOrig.equals(pcImg)) {
					setPresenceCondition(mapping.getImage(), pcOrig);
				}
			}
		}
	}

	String getPresenceCondition(ModelElement element) {
		if (!element.getAnnotations().isEmpty())
			return element.getAnnotations().get(0).getValue();
		else
			return null;
	}

	void setPresenceCondition(ModelElement element, String value) {
		Annotation ann = null;
		if (!element.getAnnotations().isEmpty())
			ann = element.getAnnotations().get(0);
		else {
			ann = HenshinFactory.eINSTANCE.createAnnotation();
			ann.setKey("presenceCondition");
			element.getAnnotations().add(ann);
		}
		ann.setValue(value);
	}
}
