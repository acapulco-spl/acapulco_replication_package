package acapulco.activationdiagrams

import acapulco.activationdiagrams.fasdPrincipleTesters.FADPrincipleTester
import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
import acapulco.model.Feature
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature
import java.nio.file.Paths
import java.util.ArrayList
import java.util.Collections
import java.util.List
import java.util.Map.Entry
import java.util.Set
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
	@ValueSource(strings = #["testdata/ad-test-1.sxfm.xml", "testdata/ad-test-2.sxfm.xml"])
	def void testFeatureSubDiagramCreation(String fmPath) {
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString)
		extension val fh = new FeatureModelHelper(fm)
		val alwaysActiveFeatures = fh.alwaysActiveFeatures
		val allRealOptionalFeatures = fh.features.reject[alwaysActiveFeatures.contains(it)].toSet

		val fad = new FeatureActivationDiagram(fm)
		val diagramNodes = fad.diagramNodes
		allRealOptionalFeatures.forEach [ f |
			val fasdActivate = fad.calculateSubdiagramFor(f, true)
			val fasdDeActivate = fad.calculateSubdiagramFor(f, false)

			FADPrincipleTester.checkPrinciplesApply(f, diagramNodes, fh)

			fasdActivate.assertRootFeatureProperties(f, true)
			fasdDeActivate.assertRootFeatureProperties(f, false)

			fasdActivate.checkExclusions
			fasdDeActivate.checkExclusions
			
			// TODO: Test presence conditions -- need to provide data oracle for this, I think
			
			// TODO: Test or implications -- need to provide data oracle for this, I think
		]

		assertEquals("There should be exactly 2 feature decisions for every real-optional feature.",
			allRealOptionalFeatures.size * 2, diagramNodes.filter(FeatureDecision).size)
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
			'''Feature activation sub-diagram for «f.name»«activate?'+':'-'» should define that feature's presence condition to be 'root'.''',
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
