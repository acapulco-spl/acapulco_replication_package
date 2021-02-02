package acapulco.activationdiagrams

import acapulco.activationdiagrams.fasdPrincipleTesters.FADPrincipleTester
import acapulco.activationdiagrams.fdsetPrincipleTesters.FeatureDecisionSetPrincipleTester
import acapulco.engine.variability.FeatureExpression
import acapulco.engine.variability.SatSolver
import acapulco.engine.variability.XorEncoderUtil
import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator
import acapulco.model.Feature
import acapulco.model.FeatureModel
import acapulco.rulesgeneration.ActivationDiagToRuleConverter
import acapulco.rulesgeneration.activationdiagrams.FASDDotGenerator
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.OrImplicationDotGenerator
import aima.core.logic.propositional.parsing.ast.ComplexSentence
import aima.core.logic.propositional.parsing.ast.Sentence
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths
import java.text.DateFormat
import java.time.Instant
import java.util.BitSet
import java.util.Date
import java.util.HashMap
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Set
import java.util.stream.Collectors
import org.eclipse.emf.henshin.model.ModelElement
import org.eclipse.emf.henshin.model.Node
import org.eclipse.emf.henshin.model.Rule
import org.junit.jupiter.api.Test
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
		fmPath.coreTest
	}

	@Test
	def void testLinux() {
		"testdata/linux-2.6.33.3.sxfm.xml".coreTest
	}

	@Test
	def void exploreSpecificFeature() {
//		coreTest("testdata/WeaFQAs.sxfm.xml", "Feedback", false)
//		coreTest("testdata/WeaFQAs.sxfm.xml", "CachingOperations", null)
		coreTest("testdata/linux-2.6.33.3.sxfm.xml", "X86_32", false)
	}

	private def coreTest(String fmPath) {
		fmPath.coreTest(null, null)
	}

	/**
	 * Run the test suite for the specified feature model, possibly focusing in on only a specific feature or feature activation (if those parameters aren't null.
	 */
	private def coreTest(String fmPath, String featureName, Boolean featureActivation) {
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString)

		val redundancyOutputFolderPath = Paths.
			get('''testoutputs/«DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date.from(Instant.now)).replaceAll("[/,:]", ".")»/''').
			toString
		val redundancyOutputFolder = new File(redundancyOutputFolderPath)
		redundancyOutputFolder.mkdirs
		val redundancyOutputFilePath = Paths.get('''«redundancyOutputFolderPath»/«Paths.get(fmPath).fileName».log''').
			toString

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
		allRealOptionalFeatures.filter[(featureName === null) || (name == featureName)].forEach [ f |
			var FeatureActivationSubDiagram fasdActivate = null			
			if ((featureActivation === null) || featureActivation) {
				fasdActivate = fad.calculateSubdiagramFor(f, true)			
			}
			var FeatureActivationSubDiagram fasdDeActivate = null			
			if ((featureActivation === null) || !featureActivation) {
				fasdDeActivate = fad.calculateSubdiagramFor(f, false)			
			}
			
			if (featureActivation === null) {
				println('''Checking feature-activation diagram for feature «f.name».''')
				FADPrincipleTester.checkPrinciplesApply(f, diagramNodes, fh)				
			}

			// TODO: Test presence conditions -- need to provide data oracle for this, I think
			// TODO: Test or implications -- need to provide data oracle for this, I think
			
			
			if ((featureActivation === null) || !featureActivation) {
				println('''Checking deactivation of feature «f.name».''')
				fasdDeActivate.assertRootFeatureProperties(f, false)
				fasdDeActivate.checkExclusions
				fasdDeActivate.generateAndCheckRule(fh, metamodelGen, redundancyOutputFilePath)
			}

			if ((featureActivation === null) || featureActivation) {
				println('''Checking activation of feature «f.name».''')
				fasdActivate.assertRootFeatureProperties(f, true)
				fasdActivate.checkExclusions
				fasdActivate.generateAndCheckRule(fh, metamodelGen, redundancyOutputFilePath)
			}
			
			if (featureName !== null) {
				fasdActivate?.writeDotFiles(redundancyOutputFolderPath)
				fasdDeActivate?.writeDotFiles(redundancyOutputFolderPath)
			}
		]

		if (featureName === null) {
			assertEquals("There should be exactly 2 feature decisions for every real-optional feature.",
				allRealOptionalFeatures.size * 2, diagramNodes.filter(FeatureDecision).size)					
		}
	}

	/**
	 * Generate a rule for the given FASD and check it is sound
	 */
	private def generateAndCheckRule(FeatureActivationSubDiagram fasd, FeatureModelHelper fh,
		FMConfigurationMetamodelGenerator metamodelgen, String redundancyOutputFilePath) {
		// 1. Generate rule
		val rule = ActivationDiagToRuleConverter.convert(fasd, metamodelgen.geteClasses)
		assertNotNull("No rule generated", rule)
		
		println('''VB rule for «fasd.rootDecision» had «rule.rhs.nodes.size» nodes.''')

		// Check that only one LHS node checks the selected state and that that is the one corresponding to the root feature decision
		val checkingNodes = rule.lhs.nodes.reject[attributes.empty]
		assertTrue('''Only one feature should be checked for selection status and that should be «fasd.rootDecision.feature.name».''',
			(checkingNodes.size === 1) && (checkingNodes.head.type.name == fasd.rootDecision.feature.name))

		// 2. Extract VB feature model and SAT solve to identify all rule instantiations
		val featureConstraint = XorEncoderUtil.encodeXor(rule.annotations.head.value)
		val featuresAsString = rule.annotations.get(2).value.replace(" ", "")
		val features = featuresAsString.split(",").map[trim].toList

		println('''FASD for «fasd.rootDecision» had «features.size» VB rule features («fasd.vbRuleFeatures.children.size» or-features).''')
		println('''There are «fasd.featureExclusions.size» feature exclusion pairs.''')
		if (fasd.orImplications.size > 0) {
			println('''There are «fasd.orImplications.size» or-implications with an average «fasd.orImplications.values.map[size].fold(0,[acc, i | acc + i])/fasd.orImplications.size» implied or features.''')		
		} else {
			println('''There are 0 or-implications.''')
		}
		println('''FASD for «fasd.rootDecision» had «fasd.orFixings.values.map[size].fold(0)[acc, x | acc + x]» or-fixings.''')
		println('''The constraint expression string is «featureConstraint.length» characters long.''')

		val sentence = FeatureExpression.getExpr(featureConstraint).sentence
		sentence.assertIsCNF

		// Solution box to 10,000 solutions
		val solutions = SatSolver.getAllSolutions(featureConstraint, 10000).toSet

		println('''(«fasd.rootDecision») We generated «solutions.size» solutions.''')

		// 3. Check all rule instantiations for soundness (all principles satisfied, no conflicting decisions)
		// Extract unique rule instances
		// TODO: The below is a start at trying to make things a bit more efficient in rule instantiation to see if this makes a difference for X86_32-. **WiP**
		// Needs the version of the SAT solver that actually produces BitSets...
		val featureMap = new HashMap<String, Feature>
		featureMap.putAll(fh.featureModel.eAllContents.filter(Feature).groupBy[name].mapValues[head])
		val presenceConditions = new HashMap<Feature, BitSet>
		rule.rhs.nodes.forEach[n |
			val feature = featureMap.get(n.type.name)

			val pc = n.annotations.head.value
			// We know the PC is only a disjunction...
			// Param for split must be a regexp...
			val pcs = pc.split("\\|").map[trim]			
		]

		// Use parallel stream to speed up the process
		val uniqueRuleInstances = solutions.parallelStream.collect(Collectors.groupingByConcurrent [ solution |
			rule.activeFeatureDecisionsFor(solution, fh.featureModel)
		])
		println('''(«fasd.rootDecision») This produced «uniqueRuleInstances.keySet.size» unique rule instances.''')
		if (uniqueRuleInstances.keySet.size < solutions.size) {
			uniqueRuleInstances.recordRedundantRuleInstances(fasd, redundancyOutputFilePath)
			fasd.writeDotFiles(redundancyOutputFilePath)
		}

		assertTrue('''FASD for «fasd.rootDecision» produced no valid rule instances.''', uniqueRuleInstances.keySet.size > 0)

		// Use parallel checking of the rules so we can use all processor cores...
		uniqueRuleInstances.keySet.parallelStream.forEach [ ruleInstance |
//		ruleInstances.parallelStream.forEach[ ruleInstance |
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

	private def void recordRedundantRuleInstances(Map<Set<Pair<Feature, Boolean>>, List<SatSolver.SatSolution>> ruleInstances,
		FeatureActivationSubDiagram fasd, String path) {
		val fOutput = new File(path)
		try (val writer = new FileWriter(fOutput, true)) {
			writer.write(ruleInstances.generateRedundancyReport(fasd))
			writer.flush
		}
	}

	private def void writeDotFiles(FeatureActivationSubDiagram fasd, String path) {
		val fasdDotFile = new File(path +
			'''/«fasd.rootDecision.feature.name»«fasd.rootDecision.activate?'Act':'DeAct'».dot''')
		try (val writer = new FileWriter(fasdDotFile)) {
			writer.write(new FASDDotGenerator(fasd, true).render)
			writer.flush
		}

		val orImplicationsDotFile = new File(path +
			'''/«fasd.rootDecision.feature.name»«fasd.rootDecision.activate?'Act':'DeAct'»OrImplications.dot''')
		try (val writer = new FileWriter(orImplicationsDotFile)) {
			writer.write(new OrImplicationDotGenerator (fasd).render)
			writer.flush
		}
	}

	private def String generateRedundancyReport(Map<Set<Pair<Feature, Boolean>>, List<SatSolver.SatSolution>> ruleInstances,
		FeatureActivationSubDiagram fasd) '''
		From feature activation sub-diagram for «fasd.rootDecision», the following redundant rule instances were generated:
		
		«ruleInstances.values.filter[size > 1].map[generateRedundancyDescription].join('\n\n')»
		-------------
		
	'''

	private def generateRedundancyDescription(List<SatSolver.SatSolution> configurationVariants) {
		val featureNameIndices = configurationVariants.head.featureNameIndices
		val invertedIndex = featureNameIndices.keySet.groupBy[featureNameIndices.get(it)].mapValues[head]
		
		val sharedFeatures = configurationVariants.head.solution.clone as BitSet
		configurationVariants.tail.forEach[sharedFeatures.and(solution)]
		val sharedFeaturesList = sharedFeatures.stream.boxed.map[invertedIndex.get(it)].sorted.collect(Collectors.toList)
		
		val distinctFeatures = configurationVariants.map[
			val unsharedFeatures = solution.clone as BitSet
			unsharedFeatures.andNot(sharedFeatures)
			
			unsharedFeatures.stream.boxed.map[invertedIndex.get(it)].sorted.collect(Collectors.toList)
		]

		'''
			- Shared features: («sharedFeaturesList.join(', ')»)
			
			  Distinct feature sets:
			  
			    «distinctFeatures.map['''- («join(', ')»)'''].join('\n')»
			    
			  Resulting feature decisions:
			  
«««			  	(«configurationVariants.key.sortBy[key.name].map[toFDString].join(', ')»)
		'''
	}
	
	private def toFDString(Pair<Feature, Boolean> fd) '''«fd.key.name»«fd.value?'+':'-'»'''

	// Check that the given sentence is in CNF form
	private def void assertIsCNF(Sentence sentence) {
		val toProcess = new LinkedList<Sentence>
		toProcess += sentence

		while (!toProcess.empty) {
			val currentSentence = toProcess.remove(0)

			if (currentSentence instanceof ComplexSentence) {
				// Otherwise it's CNF by default
				if (currentSentence.isAndSentence) {
					for (var i = 0; i < currentSentence.numberSimplerSentences; i++) {
						toProcess += currentSentence.getSimplerSentence(i)
					}
				} else {
					if (currentSentence.isUnarySentence) {
						assertTrue("Condition not in CNF",
							currentSentence.isNotSentence && currentSentence.getSimplerSentence(0).isPropositionSymbol)
					} else {
						assertTrue(
							"Sentence not in CNF.",
							currentSentence.isOrSentence &&
								((currentSentence.getSimplerSentence(0).isNotSentence &&
									currentSentence.getSimplerSentence(0).getSimplerSentence(0).isPropositionSymbol) ||
									(currentSentence.getSimplerSentence(1).isNotSentence &&
										currentSentence.getSimplerSentence(1).getSimplerSentence(0).
											isPropositionSymbol))
						)
					}
				}
			}
		}
	}

	private def activeFeatureDecisionsFor(Rule rule, SatSolver.SatSolution selectedFeatures, FeatureModel fm) {
		val bitSetPCs = new HashMap<Node, BitSet>
		rule.rhs.nodes.forEach[n |
			bitSetPCs.put(n, n.calculateBitSetPC(selectedFeatures.featureNameIndices))
		]		
		
		rule.rhs.nodes.filter[pcFulfilled(selectedFeatures.solution, bitSetPCs)].map[createFeatureDecision(fm)].toSet
	}
	
	private def calculateBitSetPC(Node n, Map<String, Integer> featureNameIndices) {
		val pc = n.annotations.head.value
		
		new BitSet(featureNameIndices.size) => [
			if (pc === null || pc.isBlank) {
				set(0, featureNameIndices.size - 1, true)
			}
			
			pc.split("\\|").map[featureNameIndices.get(it)].forEach[idx | set(idx)]
		]
	}

	private def createFeatureDecision(Node n, FeatureModel fm) {
		fm.eAllContents.filter(Feature).findFirst[name == n.type.name] -> (n.attributes.head.value == "true")
	}

	private def pcFulfilled(ModelElement n, BitSet selectedFeatures, Map<Node, BitSet> pcs) {
		pcs.get(n).intersects(selectedFeatures)
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
//	def private void printFeatureActivationSubDiagram(FeatureActivationSubDiagram sd) {
//		var List<String> outputList = new ArrayList<String>()
//		for (Entry<FeatureDecision, Set<VBRuleFeature>> pc : sd.getPresenceConditions().entrySet()) {
//			var String output = '''«pc.getKey()» -> '''
//			if (pc.getValue().size() === 1) {
//				output += pc.getValue().iterator().next().getName()
//			} else {
//				output += "or( "
//				for (VBRuleFeature pcComponent : pc.getValue()) {
//					output += pcComponent.getName()
//					output += " "
//				}
//				output += " )"
//			}
//			outputList.add(output)
//		}
//		Collections.sort(outputList)
//		outputList.forEach([e|System.out.println(e)])
//	}
}
