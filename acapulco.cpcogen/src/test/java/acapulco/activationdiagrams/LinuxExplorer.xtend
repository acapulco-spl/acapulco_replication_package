package acapulco.activationdiagrams

import acapulco.engine.variability.SatSolver
import acapulco.engine.variability.XorEncoderUtil
import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator
import acapulco.model.Feature
import acapulco.model.FeatureModel
import acapulco.rulesgeneration.ActivationDiagToRuleConverter
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram
import java.nio.file.Paths
import java.util.HashSet
import java.util.List
import java.util.Random
import java.util.Set
import org.eclipse.emf.henshin.model.ModelElement
import org.eclipse.emf.henshin.model.Node
import org.eclipse.emf.henshin.model.Rule

import static java.util.stream.Collectors.*

class LinuxExplorer {
	def static void main(String[] args) {
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get("testdata/linux-2.6.33.3.sxfm.xml").toString)		
		val fad = new FeatureActivationDiagram(fm)
		val fh = new FeatureModelHelper(fm)
		
		val fmName = "testmm"
		val metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName, "http://" + fmName)
		metamodelGen.generateMetamodel
		
		val fasd = fad.calculateSubdiagramFor(fh.features.findFirst[name == "X86_32"], false)
		
		val rule = ActivationDiagToRuleConverter.convert(fasd, metamodelGen.geteClasses)
		
		val featuresAsString = rule.annotations.get(2).value.replace(" ", "")
		val features = featuresAsString.split(",").map[trim].toList
		
		val nodeCount = rule.lhs.nodes.size
		val averagePCSize = fasd.presenceConditions.values.stream.collect(averagingInt[size])		
		println('''VB rule for «fasd.rootDecision» has «nodeCount» nodes.''')
		println('''Average size of presence-condition for each node is «averagePCSize».''')
		println('''Rule has «features.size» VB features.''')
		
		val featureConstraint = XorEncoderUtil.encodeXor(rule.annotations.head.value)
		val solutions = SatSolver.getAllSolutions(featureConstraint, 60000l).toSet
		
		println('''Generated «solutions.size» solutions.''')
		
		val time = System.currentTimeMillis
		val instantiatedRule = rule.activeFeatureDecisionsFor(solutions.random, fh.featureModel)
		val time2 = System.currentTimeMillis
		val timeTaken = time2 - time
		println('''Instantiating one rule instance took «timeTaken» milliseconds.''')
		
		println('''Estimated time for instantiating all rules, thus, is: «((timeTaken * solutions.size)/1000d)/60» minutes.''')
	}
	
	private static def activeFeatureDecisionsFor(Rule rule, List<String> selectedFeatures, FeatureModel fm) {
		val selectedFeatureSet = new HashSet<String>
		selectedFeatureSet += selectedFeatures
		rule.rhs.nodes.filter[pcFulfilled(selectedFeatureSet)].map[createFeatureDecision(fm)].toSet
	}

	private static def createFeatureDecision(Node n, FeatureModel fm) {
		fm.eAllContents.filter(Feature).findFirst[name == n.type.name] -> (n.attributes.head.value == "true")
	}

	private static def pcFulfilled(ModelElement n, Set<String> selectedFeatures) {
		val pc = n.annotations.head.value
		if (pc === null || pc.isBlank) {
			return true
		}

		// We know the PC is only a disjunction...
		// Param for split must be a regexp...
		pc.split("\\|").exists[selectedFeatures.contains(it.trim)]
	}

	static val Random rand = new Random

	private static def <T> getRandom(Iterable<T> collection) {
		collection.get(rand.nextInt(collection.size))
	}
}