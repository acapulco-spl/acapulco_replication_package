package acapulco.activationdiagrams

import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
import acapulco.rulesgeneration.activationdiagrams.FASDDotGenerator
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths
import java.text.DateFormat
import java.time.Instant
import java.util.Date

class FeedbackDeactExplorer {
	def static void main(String[] args) {
		val fmPath = "testdata/WeaFQAs.sxfm.xml"
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString)

		val outputFolderPath = Paths.
			get('''testoutputs/«DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date.from(Instant.now)).replaceAll("[/,:]", ".")»/''').
			toString
		val outputFolder = new File(outputFolderPath)
		outputFolder.mkdirs

		val fad = new FeatureActivationDiagram(fm)
		val fh = new FeatureModelHelper(fm)
		val fasd = fad.calculateSubdiagramFor(fh.features.findFirst[name == "Feedback"], false)

		val fasdDotFile = new File(outputFolderPath +
			'''/«fasd.rootDecision.feature.name»«fasd.rootDecision.activate?'Act':'DeAct'».dot''')
		try (val writer = new FileWriter(fasdDotFile)) {
			val generator = new FASDDotGenerator(fasd, true, fh.features.filter [
				(name == "Internationalization") || (name == "ContextAwareness")
			].map[new FeatureDecision(it, it.name == "Internationalization")].toSet)
			writer.write(generator.render)
			writer.flush
		}
	}
}
