package acapulco.activationdiagrams

import acapulco.activationdiagrams.principleTesters.ActExc
import acapulco.activationdiagrams.principleTesters.ActGroup
import acapulco.activationdiagrams.principleTesters.ActMand
import acapulco.activationdiagrams.principleTesters.ActPar
import acapulco.activationdiagrams.principleTesters.ActReq
import acapulco.activationdiagrams.principleTesters.ActXor
import acapulco.activationdiagrams.principleTesters.DeChild
import acapulco.activationdiagrams.principleTesters.DeOr
import acapulco.activationdiagrams.principleTesters.DeParent
import acapulco.activationdiagrams.principleTesters.DeReq
import acapulco.activationdiagrams.principleTesters.DeXor
import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
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
import org.junit.jupiter.api.Test

import static org.junit.Assert.assertEquals

class ActivationDiagramTest {

	static val principleChecks = #[
		new ActMand,
		new ActPar,
		new ActReq,
		new ActGroup,
		new ActXor,
		new ActExc,
		new DeChild,
		new DeXor,
		new DeOr,
		new DeParent,
		new DeReq
	]

	/**
	 * Generate all activation sub-diagrams for the feature model, then do some basic checks
	 */
	@Test
	def void testFeatureSubDiagramCreation() {
		// TODO: This should really be in src/test/resources
		val fmPath = "testdata/ad-test-1.sxfm.xml"
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString)
		extension val fh = new FeatureModelHelper(fm)
		val alwaysActiveFeatures = fh.alwaysActiveFeatures
		val allRealOptionalFeatures = fh.features.reject[alwaysActiveFeatures.contains(it)].toSet

		val fad = new FeatureActivationDiagram(fm)
		val diagramNodes = fad.diagramNodes
		allRealOptionalFeatures.forEach [ f |
			val fasdActivate = fad.calculateSubdiagramFor(f, true)
			val fasdDeActivate = fad.calculateSubdiagramFor(f, false)
			
			principleChecks.forEach[checkPrincipleApplies(f, diagramNodes, fh)]
		]

		assertEquals("There should be exactly 2 feature decisions for every real-optional feature.",
			allRealOptionalFeatures.size * 2, diagramNodes.filter(FeatureDecision).size)
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
			var String output = '''�pc.getKey()� -> '''
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
