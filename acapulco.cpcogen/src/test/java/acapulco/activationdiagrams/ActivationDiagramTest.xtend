package acapulco.activationdiagrams

import acapulco.activationdiagrams.fasdPrincipleTesters.FADPrincipleTester
import acapulco.activationdiagrams.fdsetPrincipleTesters.FeatureDecisionSetPrincipleTester
import acapulco.engine.variability.ExtendedSentence
import acapulco.engine.variability.FeatureExpression
import acapulco.engine.variability.SatSolver
import acapulco.engine.variability.XorEncoderUtil
import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator
import acapulco.model.Feature
import acapulco.model.FeatureModel
import acapulco.rulesgeneration.ActivationDiagToRuleConverter
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import aima.core.logic.fol.parsing.ast.Sentence
import aima.core.logic.propositional.parsing.ast.ComplexSentence
import aima.core.logic.propositional.parsing.ast.Connective
import aima.core.logic.propositional.parsing.ast.PropositionSymbol
import java.nio.file.Paths
import java.util.ArrayList
import java.util.Collections
import java.util.List
import java.util.Map.Entry
import java.util.Set
import org.eclipse.emf.henshin.model.ModelElement
import org.eclipse.emf.henshin.model.Node
import org.eclipse.emf.henshin.model.Rule
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

import static org.junit.Assert.*

class ActivationDiagramTest {

	/**
	 * Generate all activation sub-diagrams for the feature model, then do some basic checks
	 */
	@ParameterizedTest
	// Add more paths to more feature models to test below...
	// TODO: This should really be in src/test/resources
	@ValueSource(strings=#["testdata/ad-test-1.sxfm.xml", "testdata/ad-test-2.sxfm.xml",
		"testdata/mobile_media2.sxfm.xml", "testdata/TankWar.sxfm.xml", "testdata/WeaFQAs.sxfm.xml"])
	def void testFeatureSubDiagramCreation(String fmPath) {
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString)
		extension val fh = new FeatureModelHelper(fm)
		val alwaysActiveFeatures = fh.alwaysActiveFeatures
		println('''Always active features are: «alwaysActiveFeatures.map[name]».''')
		val allRealOptionalFeatures = fh.features.reject[alwaysActiveFeatures.contains(it)].toSet
		println('''Real optional features are: «allRealOptionalFeatures.map[name]».''')

		val fmName = "testmm"
		val metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName, "http://" + fmName)
		metamodelGen.generateMetamodel

		val fad = new FeatureActivationDiagram(fm)
		val diagramNodes = fad.diagramNodes
		allRealOptionalFeatures.forEach [ f |
			println('''Checking activation of feature «f.name».''')
			val fasdActivate = fad.calculateSubdiagramFor(f, true)
			fasdActivate.assertRootFeatureProperties(f, true)
			fasdActivate.checkExclusions
			fasdActivate.generateAndCheckRule(fh, metamodelGen)

			println('''Checking deactivation of feature «f.name».''')
			val fasdDeActivate = fad.calculateSubdiagramFor(f, false)
			fasdDeActivate.assertRootFeatureProperties(f, false)
			fasdDeActivate.checkExclusions
			fasdDeActivate.generateAndCheckRule(fh, metamodelGen)

			FADPrincipleTester.checkPrinciplesApply(f, diagramNodes, fh)

		// TODO: Test presence conditions -- need to provide data oracle for this, I think
		// TODO: Test or implications -- need to provide data oracle for this, I think
		]

		assertEquals("There should be exactly 2 feature decisions for every real-optional feature.",
			allRealOptionalFeatures.size * 2, diagramNodes.filter(FeatureDecision).size)
	}

	/**
	 * Generate a rule for the given FASD and check it is sound
	 */
	private def generateAndCheckRule(FeatureActivationSubDiagram fasd, FeatureModelHelper fh,
		FMConfigurationMetamodelGenerator metamodelgen) {
		// 1. Generate rule
		val rule = ActivationDiagToRuleConverter.convert(fasd, metamodelgen.geteClasses)
		assertNotNull("No rule generated", rule)

		// Check that only one LHS node checks the selected state and that that is the one corresponding to the root feature decision
		val checkingNodes = rule.lhs.nodes.reject[attributes.empty]
		assertTrue('''Only one feature should be checked for selection status and that should be «fasd.rootDecision.feature.name».''',
			(checkingNodes.size === 1) && (checkingNodes.head.type.name == fasd.rootDecision.feature.name))

		// 2. Extract VB feature model and SAT solve to identify all rule instantiations
		val featureConstraint = XorEncoderUtil.encodeXor(rule.annotations.head.value)
		val featuresAsString = rule.annotations.get(2).value.replace(" ", "")
		val features = featuresAsString.split(",").map[trim].toList

		// Just temporarily, let's parse this to get a sense of the complexity of the conditions
		val sentence = FeatureExpression.getExpr(featureConstraint)
		println('''FASD for «fasd.rootDecision» had «features.size» VB rule features.''')

		val solutions = SatSolver.getAllSolutions(featureConstraint).toSet

		println('''(«fasd.rootDecision») We generated «solutions.size» solutions.''')

		// 3. Check all rule instantiations for soundness (all principles satisfied, no conflicting decisions)
		// Extract unique rule instances
		val uniqueRuleInstances = solutions.map[solution|rule.activeFeatureDecisionsFor(solution, fh.featureModel)].
			toSet

		println('''(«fasd.rootDecision») This produced «uniqueRuleInstances.size» unique rule instances.''')

		uniqueRuleInstances.forEach [ ruleInstance |
			// 3.1 no conflicting decisions
			assertTrue(
				"No rule instance should contain conflicting feature decisions.",
				ruleInstance.forall [ fd1 |
					ruleInstance.forall [ fd2 |
						(fd1 === fd2) || (fd1.key != fd2.key) || (fd1.value == fd2.value)
					]
				]
			)

			// 3.2 all principles satisfied
			FeatureDecisionSetPrincipleTester.checkPrinciplesApply(ruleInstance, fh)

			// 3.3 root decision is included
			assertTrue('''FASD for «fasd.rootDecision» produced rule that didn't contain root decision.''', ruleInstance.
				exists[fd|(fd.key === fasd.rootDecision.feature) && (fd.value === fasd.rootDecision.activate)])
		]

	// TODO: Now run the same tests over the actual rules generated			
//		solutions.forEach [ solution |			
//			val ruleInstance = RuleProvider.provideRule(rule, features.toInvertedMap[solution.contains(it)])
//		]
	}

	private def activeFeatureDecisionsFor(Rule rule, List<String> selectedFeatures, FeatureModel fm) {
		rule.rhs.nodes.filter[pcFulfilled(selectedFeatures)].map[createFeatureDecision(fm)].toSet
	}

	private def createFeatureDecision(Node n, FeatureModel fm) {
		fm.eAllContents.filter(Feature).findFirst[name == n.type.name] -> (n.attributes.head.value == "true")
	}

	private def pcFulfilled(ModelElement n, List<String> selectedFeatures) {
		val pc = n.annotations.head.value
		if (pc === null || pc.isBlank) {
			return true
		}

		// We know the PC is only a disjunction...
		// Param for split must be a regexp...
		pc.split("\\|").exists[selectedFeatures.contains(it.trim)]
	}

	private def checkExclusions(FeatureActivationSubDiagram fasd) {
		val presenceConditions = fasd.presenceConditions
		val exclusions = fasd.featureExclusions
		fasd.featureDecisions.filter[activate].forEach [ fdPlus |
			val fdMinus = fasd.featureDecisions.reject[activate].findFirst[feature === fdPlus.feature]

			if (fdMinus !== null) {
				// For every pair of conflicting feature decisions, check that they can never occur together
				val pcPlus = presenceConditions.get(fdPlus)
				val pcMinus = presenceConditions.get(fdMinus)

				assertTrue(
					'''Feature Activation Sub-Diagram for «fasd.rootDecision» contains conflicting decisions for feature «fdPlus.feature.name» and these are not mutually excluded.''',
					pcPlus.forall [ ppc |
						pcMinus.forall[mpc|exclusions.contains(ppc -> mpc) || exclusions.contains(mpc -> ppc)]
					]
				)
			}
		]
	}

	private def assertRootFeatureProperties(FeatureActivationSubDiagram fasd, Feature f, boolean activate) {
		assertSame('''Was expecting root feature of sub-diagram for «f.name»«activate?'+':'-'» to be «f.name»''', f,
			fasd.rootDecision.feature)
		assertSame(
			'''Was expecting root feature of sub-diagram for «f.name»«activate?'+':'-'» to be «!activate?'de'»activated.''',
			activate,
			fasd.rootDecision.activate
		)

		// Require root fd's presence condition to be fully simplified
		val fasdRootPC = fasd.presenceConditions.get(fasd.rootDecision)
		assertTrue(
			'''Feature activation sub-diagram for «fasd.rootDecision» should define that feature's presence condition to be 'root'.''',
			(fasdRootPC.size === 1) && (fasdRootPC.head === fasd.vbRuleFeatures)
		)
	}

	/*
	 * 	@Test -- for now, let's not run this test: it generates files (which also happen to be checked in) and then doesn't actually check anything
	 * 	def package void testCreateActivationDiagram() {
	 * 		var String fmPath = "testdata/ad-test-1.sxfm.xml"
	 * 		var FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString())
	 * 		var Feature f1 = fm.getOwnedRoot().getOwnedFeatures().get(0)
	 * 		assertEquals(f1.getName(), "F1")
	 * 		var FeatureActivationDiagram ad = new FeatureActivationDiagram(fm)
	 * 		// FM-specific
	 * 		var String fmName = "ad-test-1"
	 * 		var FMConfigurationMetamodelGenerator metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName,
	 * 			"http://+fmName")
	 * 		metamodelGen.generateMetamodel()
	 * 		for (Feature f : metamodelGen.geteClasses().keySet()) {
	 * 			if (!f.getName().startsWith("R")) {
	 * 				var FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, true)
	 * 				// CPCO-specific
	 * 				// printFeatureActivationSubDiagram(sd);
	 * 				var Rule rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses())
	 * 				HenshinFileWriter.writeModuleToPath(
	 * 					Collections.singletonList(rule), '''generated\�rule.getName()�.hen''')
	 * 				sd = ad.calculateSubdiagramFor(f, false)
	 * 				// CPCO-specific
	 * 				rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses())
	 * 				HenshinFileWriter.writeModuleToPath(
	 * 					Collections.singletonList(rule), '''generated\�rule.getName()�.hen''')
	 * 			}
	 * 		}
	 * 	}
	 */
	def private void printFeatureActivationSubDiagram(FeatureActivationSubDiagram sd) {
		var List<String> outputList = new ArrayList<String>()
		for (Entry<FeatureDecision, Set<VBRuleFeature>> pc : sd.getPresenceConditions().entrySet()) {
			var String output = '''«pc.getKey()» -> '''
			if (pc.getValue().size() === 1) {
				output += pc.getValue().iterator().next().getName()
			} else {
				output += "or( "
				for (VBRuleFeature pcComponent : pc.getValue()) {
					output += pcComponent.getName()
					output += " "
				}
				output += " )"
			}
			outputList.add(output)
		}
		Collections.sort(outputList)
		outputList.forEach([e|System.out.println(e)])
	}
}
