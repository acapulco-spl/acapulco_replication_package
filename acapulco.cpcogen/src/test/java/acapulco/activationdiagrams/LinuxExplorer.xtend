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
import java.util.BitSet
import java.util.HashMap
import java.util.Map
import java.util.Random
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
		
		val bitSetStartTime = System.currentTimeMillis
		val bitSetPCs = new HashMap<Node, BitSet>
		rule.rhs.nodes.forEach[n |
			bitSetPCs.put(n, n.calculateBitSetPC(solutions.head.featureNameIndices))
		]
		val bitSetEndTime = System.currentTimeMillis
		val bitSetTimeTaken = bitSetEndTime - bitSetStartTime
		println('''Setting up PC bitsets took: «bitSetTimeTaken» milliseconds.''')
		
		val time = System.currentTimeMillis
		val instantiatedRule = rule.activeFeatureDecisionsFor(solutions.random, fh.featureModel, bitSetPCs)
		val time2 = System.currentTimeMillis
		val timeTaken = time2 - time
		println('''Instantiating one rule instance took «timeTaken» milliseconds.''')
		
		println('''Estimated time for instantiating all rules, thus, is: «((bitSetTimeTaken + (timeTaken * solutions.size))/1000d)/60» minutes.''')
	}
	
	private static def activeFeatureDecisionsFor(Rule rule, SatSolver.SatSolution selectedFeatures, FeatureModel fm, Map<Node, BitSet> bitSetPCs) {
		rule.rhs.nodes.filter[pcFulfilled(selectedFeatures.solution, bitSetPCs)].map[createFeatureDecision(fm)].toSet
	}
	
	private static def calculateBitSetPC(Node n, Map<String, Integer> featureNameIndices) {
		val pc = n.annotations.head.value
		
		new BitSet(featureNameIndices.size) => [
			if (pc === null || pc.isBlank) {
				set(0, featureNameIndices.size - 1, true)
			}
			
			pc.split("\\|").map[featureNameIndices.get(it)].forEach[idx | set(idx)]
		]
	}

	private static def createFeatureDecision(Node n, FeatureModel fm) {
		fm.eAllContents.filter(Feature).findFirst[name == n.type.name] -> (n.attributes.head.value == "true")
	}

	private static def pcFulfilled(ModelElement n, BitSet selectedFeatures, Map<Node, BitSet> pcs) {
		pcs.get(n).intersects(selectedFeatures)
	}

	static val Random rand = new Random

	private static def <T> getRandom(Iterable<T> collection) {
		collection.get(rand.nextInt(collection.size))
	}
}