package emf.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Annotation;
import org.eclipse.emf.henshin.model.Attribute;
import org.eclipse.emf.henshin.model.Edge;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Mapping;
import org.eclipse.emf.henshin.model.ModelElement;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

import acapulco.engine.variability.FeatureExpression;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

public class RuleBuilder {
	private Rule inputRule;
	private Model m;

	public RuleBuilder(Rule rule, Map<String, Boolean> configuration) {
		fixInconsistencies(rule);
		this.inputRule = rule;
		Map<PropositionSymbol, Boolean> configurationMap = new HashMap<>();
		configuration.entrySet()
				.forEach(e -> configurationMap.put(FeatureExpression.getSymbolExpr(e.getKey()), e.getValue()));
		this.m = new Model(configurationMap);
	}

	public Rule buildRule() {
		Map<EObject, EObject> orig2copy = new HashMap<>();

		Rule input = inputRule;
		Rule output = HenshinFactory.eINSTANCE.createRule(input.getName());
		buildRuleRecursively(input, output, orig2copy);

//		System.out.println(output);
		return output;
	}

	private void buildRuleRecursively(Rule input, Rule output, Map<EObject, EObject> orig2copy) {
		copyElements(input, output, orig2copy, true);
		copyElements(input, output, orig2copy, false);
		copyMappings(input, output, orig2copy, false);
		copyMultiMappings(input, output, orig2copy, false);

		for (Rule multiInput : input.getMultiRules()) {
			Rule multiOutput = HenshinFactory.eINSTANCE.createRule(multiInput.getName());
			buildRuleRecursively(multiInput, multiOutput, orig2copy);
			output.getMultiRules().add(multiOutput);
		}
	}

	private void copyMappings(Rule input, Rule output, Map<EObject, EObject> orig2copy, boolean b) {
		for (Mapping m : input.getMappings()) {
			EObject origin = orig2copy.get(m.getOrigin());
			EObject image = orig2copy.get(m.getImage());

			if (origin != null && image != null) {
				Mapping mapping = HenshinFactory.eINSTANCE.createMapping((Node) origin, (Node) image);
				output.getMappings().add(mapping);
			}
		}
	}

	private void copyMultiMappings(Rule input, Rule output, Map<EObject, EObject> orig2copy, boolean b) {
		for (Mapping m : input.getMultiMappings()) {
			EObject origin = orig2copy.get(m.getOrigin());
			EObject image = orig2copy.get(m.getImage());

			if (origin != null && image != null) {
				Mapping mapping = HenshinFactory.eINSTANCE.createMapping((Node) origin, (Node) image);
				output.getMultiMappings().add(mapping);
			}
		}
	}

	private void copyElements(Rule input, Rule output, Map<EObject, EObject> orig2copy, boolean lhs) {
		Graph source = lhs ? input.getLhs() : input.getRhs();
		Graph target = lhs ? output.getLhs() : output.getRhs();
		for (Node n : source.getNodes()) {
			if (n.getAnnotations().isEmpty() || pcFulfilled(n)) {
				Node n2 = HenshinFactory.eINSTANCE.createNode(target, n.getType(), n.getName());
				orig2copy.put(n, n2);

				if (!n.getAttributes().isEmpty()) {
					Attribute a = n.getAttributes().get(0);
					Attribute a2 = HenshinFactory.eINSTANCE.createAttribute(n2, a.getType(), a.getValue());
					orig2copy.put(a, a2);
				}
			}
		}

		for (Edge e : source.getEdges()) {
			Node src = (Node) orig2copy.get(e.getSource());
			Node trg = (Node) orig2copy.get(e.getTarget());
			if (src != null && trg != null) {
				Edge e2 = HenshinFactory.eINSTANCE.createEdge(src, trg, e.getType());
				orig2copy.put(e, e2);
				// We assume that the PC is determined by the nodes' PCs, hence no check
			}
		}
	}

	private boolean pcFulfilled(ModelElement n) {
		String pc = n.getAnnotations().get(0).getValue();
		if (pc == null || pc.isBlank())
			return true;
		
		boolean isTrue = m.isTrue(FeatureExpression.getExpr(pc));
		boolean isFalse = m.isFalse(FeatureExpression.getExpr(pc));

		if (isTrue == isFalse)
			throw new RuntimeException("Error during PC evaluation (Rule: "+inputRule.getName()+ ", PC: "+pc+"). Probably one feature unknown?");
		return isTrue;
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
