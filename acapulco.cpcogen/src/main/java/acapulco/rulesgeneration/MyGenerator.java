package acapulco.rulesgeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.henshin.model.Annotation;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.ModelElement;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.Rule;

import acapulco.featuremodel.FeatureModelHelper;
import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.rulesgeneration.activationdiagrams.ActivationDiagramNode;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision;
import acapulco.rulesgeneration.activationdiagrams.OrNode;
import emf.utils.HenshinFileWriter;

public class MyGenerator {
	public static String KEY_PRESENCE_CONDITION = "presenceCondition";
	public static String KEY_FEATURE_CONSTRAINT = "featureModel";
	public static String KEY_INJ_MATCHING = "injectiveMatchingPresenceCondition";
	public static String KEY_FEATURES = "features";
	
	public static Set<Set<FeatureDecision>> xorConstraints;
	public static Set<FeatureDecision> pcFeaturesList;
	
	public static void generatorCPCOsForFeature(FeatureModel fm, String fmName, String outpath, String featureName, boolean activate) {
		outpath += "/"+fmName;
		FeatureActivationDiagram ad = new FeatureActivationDiagram(fm); // FM-specific
		FMConfigurationMetamodelGenerator metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName,
				"http://"+fmName);
		
		metamodelGen.generateMetamodel();
		System.out.println(outpath);
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.ecore");
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+fmName+".ecore");
		
		FeatureModelHelper helper = new FeatureModelHelper(fm);
		List<Feature> trueOptional = new ArrayList<>(helper.getFeatures());
		trueOptional.removeAll(helper.getAlwaysActiveFeatures());

		Feature f = trueOptional.stream().filter(n -> n.getName().equals(featureName)).findAny().get();
		System.out.println("feature: " + f);
		
		xorConstraints = new HashSet<Set<FeatureDecision>>();
		pcFeaturesList = new HashSet<FeatureDecision>();
		FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, activate); // CPCO-specific
			
		Set<FMHenshinNode> nodes = new HashSet<FMHenshinNode>();
		Set<FeatureDecision> pc = new HashSet<FeatureDecision>();
		nodes = createFMHenshinNodes(nodes, sd.getRootDecision(), pc);
		
		System.out.println("============================");
		System.out.println("Root: " + sd.getRootDecision());
		for (FMHenshinNode n : nodes) {
			System.out.println("Henshin node: " + n);
		}
		System.out.println("FM FEATURES LIST: " + pcFeaturesList);
		
		// Clean FM constraints (by removing those XOR constraints that includes features not present in the features list of PCs
		xorConstraints = xorConstraints.stream().filter(s -> pcFeaturesList.containsAll(s)).collect(Collectors.toSet());
		System.out.println("FM XOR CONSTRAINTS: " + xorConstraints);
		
		System.out.println("<<We need to added an xor for each EXCLUDES constraint.>>");
	
		Rule rule = generateRule(sd.getRootDecision(), nodes, pcFeaturesList, xorConstraints, metamodelGen.geteClasses());
		HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
	}
	
	public static void generatorCPCOs(FeatureModel fm, String fmName, String outpath) {
		outpath += "/"+fmName;
		FeatureActivationDiagram ad = new FeatureActivationDiagram(fm); // FM-specific
		FMConfigurationMetamodelGenerator metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName,
				"http://"+fmName);
		
		metamodelGen.generateMetamodel();
		System.out.println(outpath);
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.ecore");
		metamodelGen.saveMetamodel(outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+fmName+".ecore");
		
		FeatureModelHelper helper = new FeatureModelHelper(fm);
		List<Feature> trueOptional = new ArrayList<>(helper.getFeatures());
		trueOptional.removeAll(helper.getAlwaysActiveFeatures());

		for (Feature f : trueOptional) {
			if (f.getName().equals("F1")) {
				// Activate feature
				xorConstraints = new HashSet<Set<FeatureDecision>>();
				pcFeaturesList = new HashSet<FeatureDecision>();
				FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, true); // CPCO-specific
				
				Set<FMHenshinNode> nodes = new HashSet<FMHenshinNode>();
				Set<FeatureDecision> pc = new HashSet<FeatureDecision>();
				//nodes = addMainFeatureDecisions(nodes, sd.getRootDecision());
				nodes = createFMHenshinNodes(nodes, sd.getRootDecision(), pc);
				
				System.out.println("============================");
				System.out.println("Root: " + sd.getRootDecision());
				for (FMHenshinNode n : nodes) {
					System.out.println("Henshin node: " + n);
				}
				System.out.println("FM FEATURES LIST: " + pcFeaturesList);
				
				// Clean FM constraints (by removing those XOR constraints that includes features not present in the features list of PCs
				xorConstraints = xorConstraints.stream().filter(s -> pcFeaturesList.containsAll(s)).collect(Collectors.toSet());
			
				System.out.println("FM XOR CONSTRAINTS: " + xorConstraints);
				
				System.out.println("<<We need to added an xor for each EXCLUDES constraint.>>");
				
				Rule rule = generateRule(sd.getRootDecision(), nodes, pcFeaturesList, xorConstraints, metamodelGen.geteClasses());
				HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
				
				// Deactivate feature
				/*
				xorConstraints = new HashSet<Set<FeatureDecision>>();
				pcFeaturesList = new HashSet<FeatureDecision>();
				sd = ad.calculateSubdiagramFor(f, false); // CPCO-specific
				
				nodes = new HashSet<FMHenshinNode>();
				pc = new HashSet<FeatureDecision>();
				nodes = createFMHenshinNodes(nodes, sd.getRootDecision(), pc);
				
				// Clean FM constraints (by removing those XOR constraints that includes features not present in the features list of PCs
				xorConstraints = xorConstraints.stream().filter(s -> pcFeaturesList.containsAll(s)).collect(Collectors.toSet());
			
				rule = generateRule(sd.getRootDecision(), nodes, pcFeaturesList, xorConstraints, metamodelGen.geteClasses());
				HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), outpath + "/acapulco/" + fmName+".dimacs.cpcos/"+rule.getName()+".hen");
				*/
				break;
			}
		}
		
	}
	
	private static Set<FMHenshinNode> addMainFeatureDecisions(Set<FMHenshinNode> nodes, FeatureDecision rootDecision) {
		Set<ActivationDiagramNode> terminalNodes = rootDecision.getConsequences().stream().filter(fd -> fd instanceof FeatureDecision).collect(Collectors.toSet());
		for (ActivationDiagramNode adn : terminalNodes) {
			FMHenshinNode hNode = createFMHenshinNode((FeatureDecision) adn, Set.of());
			nodes.add(hNode);
			nodes = addMainFeatureDecisions(nodes, (FeatureDecision) adn);
		}
		return nodes;
	}

	public static Set<FMHenshinNode> createFMHenshinNodes(Set<FMHenshinNode> nodes, ActivationDiagramNode adn, Set<FeatureDecision> pc) {
		System.out.println("nodes: " + nodes);
		System.out.println("new node: " + adn);
		System.out.println("pcs: " + pc);
		// Add the current node if it is a terminal decision
		if (adn instanceof FeatureDecision) {
			FeatureDecision fd = (FeatureDecision) adn;
			
			FMHenshinNode hNode = createFMHenshinNode(fd, pc);
			
			if (!nodes.contains(hNode)) {
				System.out.println("The node is not already.");
				if (alreadyExistWithoutPC(nodes, hNode)) {	// Inconsistency because there is a node without PCs
					System.out.println("alreadyExistWithoutPC");	
					return nodes;	// pruning
				} else {
					nodes.add(hNode);
					pcFeaturesList.addAll(pc);
					System.out.println("Node addded.");
				}
				
			} else {
				FMHenshinNode presentNode = nodes.stream().filter(n -> n.equals(hNode)).findAny().get();
				if (presentNode.getPCs().isEmpty()) {	// the existing node is on the main branch, so current node cannot be added.
					System.out.println("node is on the main branch.");
					return nodes;	// pruning
				} else {	// we add the PCs of the current node to the existing node (We do not add new nodes).
					presentNode.getPCs().add(pc);
					pcFeaturesList.addAll(pc);
					System.out.println("Concatenate PCs.");
				}
			}
			
			// Recursion for the consequences
			/*
			for (ActivationDiagramNode c : adn.getConsequences()) {
				System.out.println("Consequences.");
				nodes.addAll(createFMHenshinNodes(nodes, c, pc));
			}
			*/
			
			List<ActivationDiagramNode> mainConsequences = adn.getConsequences().stream().filter(c -> c instanceof FeatureDecision).collect(Collectors.toList());
			List<ActivationDiagramNode> otherConsequences = adn.getConsequences().stream().filter(c -> !(c instanceof FeatureDecision)).collect(Collectors.toList());
			
			for (ActivationDiagramNode c : mainConsequences) {
				System.out.println("Consequences.");
				nodes.addAll(createFMHenshinNodes(nodes, c, pc));
			}
			
			for (ActivationDiagramNode c : otherConsequences) {
				System.out.println("Consequences.");
				nodes.addAll(createFMHenshinNodes(nodes, c, pc));
			}
			
		} else if (adn instanceof OrNode) {
			Set<FeatureDecision> terminalNodes = adn.getConsequences().stream().filter(n -> n instanceof FeatureDecision).map(n -> (FeatureDecision)n).collect(Collectors.toSet());
			Set<OrNode> orNodes = adn.getConsequences().stream().filter(n -> n instanceof OrNode).map(n -> (OrNode)n).collect(Collectors.toSet());
			
			if (!terminalNodes.isEmpty()) {
				//String constraint = "xor(" + terminalNodes.stream().map(tn -> tn.toString()).collect(Collectors.joining(",")) + ")";
				xorConstraints.add(terminalNodes);
				for (FeatureDecision tn : terminalNodes) {
					System.out.println("terminal node in orNode.");
					Set<FeatureDecision> newPC = new HashSet<FeatureDecision>(pc);
					newPC.add(tn);
					nodes.addAll(createFMHenshinNodes(nodes, tn, newPC));
				}
				
			}
			
			for (OrNode on : orNodes) {
				System.out.println("orNode.");
				nodes.addAll(createFMHenshinNodes(nodes, on, pc));
			}
		}
		return nodes;
	}
	
	public static FMHenshinNode createFMHenshinNode(FeatureDecision fd, Set<FeatureDecision> pc) {
		FMHenshinNode node = new FMHenshinNode(fd);
		if (!pc.isEmpty()) {
			node.getPCs().add(pc);
		}
		return node;
	}

	public static boolean alreadyExistWithoutPC(Set<FMHenshinNode> nodes, FMHenshinNode hNode) {
		Optional<FMHenshinNode> searchNode = nodes.stream().filter(n -> n.getFeatureDecision().getFeature().equals(hNode.getFeatureDecision().getFeature())).findAny();
		if (searchNode.isPresent()) {
			FMHenshinNode n = searchNode.get();
			return n.getPCs().isEmpty();
		} else {
			return false;
		}
	}
	
	public static Rule generateRule(FeatureDecision root, Set<FMHenshinNode> nodes, Set<FeatureDecision> pcFeaturesList, Set<Set<FeatureDecision>> xorConstraints, Map<Feature, EClass> features2Classes) {
		String name = (root.isActivate() ? "Act" : "De") + root.getFeature().getName();
		Rule rule = HenshinFactory.eINSTANCE.createRule(name);
		
		for (FMHenshinNode n : nodes) {
			addPreserveNode(rule, features2Classes.get(n.getFeatureDecision().getFeature()), n.getFeatureDecision().isActivate(), n.getFeatureDecision().equals(root), n.getPCs());
		}
		
		Set<String> stringConstraints = new HashSet<String>();
		for (Set<FeatureDecision> xorC : xorConstraints) {
			stringConstraints.add("xor(" + String.join("&", xorC.stream().map(p -> p.getFeature().getName()).collect(Collectors.toSet())) + ")");
		}
		String constraints = String.join("&", stringConstraints);
		
		addAnnotation(rule, KEY_FEATURE_CONSTRAINT, constraints);
		addAnnotation(rule, KEY_INJ_MATCHING, "false");
		addAnnotation(rule, KEY_FEATURES, String.join(",", pcFeaturesList.stream().map(f -> f.getFeature().getName().toString()).collect(Collectors.toSet())));
		
		return rule;
		
	}
	
	private static void addPreserveNode(Rule rule, EClass type, boolean activate, boolean isRoot, Set<Set<FeatureDecision>> pcs) {
		if (type == null) {
			System.out.println("type was null");
			// TODO: Should this throw an exception?
		}

		Node lhsNode = HenshinFactory.eINSTANCE.createNode(rule.getLhs(), type, "");
		Node rhsNode = HenshinFactory.eINSTANCE.createNode(rule.getRhs(), type, "");
		rule.getMappings().add(lhsNode, rhsNode);

		EAttribute attributeType = type.getEAllAttributes().get(0);
		if (isRoot) {
			HenshinFactory.eINSTANCE.createAttribute(lhsNode, attributeType, Boolean.toString(!activate));
		}
		HenshinFactory.eINSTANCE.createAttribute(rhsNode, attributeType, Boolean.toString(activate));

		Set<String> stringPCs = new HashSet<String>();
		for (Set<FeatureDecision> OrPCs : pcs) {
			stringPCs.add(String.join("&", OrPCs.stream().map(p -> p.getFeature().getName()).collect(Collectors.toSet())));
		}
		String presenCondition = String.join("|", stringPCs);

		addAnnotation(lhsNode, KEY_PRESENCE_CONDITION, presenCondition);
		addAnnotation(rhsNode, KEY_PRESENCE_CONDITION, presenCondition);
	}

	private static void addAnnotation(ModelElement elem, String aKey, String aValue) {
		Annotation a = HenshinFactory.eINSTANCE.createAnnotation();
		a.setKey(aKey);
		a.setValue(aValue);
		elem.getAnnotations().add(a);
	}
}
