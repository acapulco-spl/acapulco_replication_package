package acapulco.activationdiagrams

import acapulco.featureide.utils.FeatureIDEUtils
import acapulco.featuremodel.FeatureModelHelper
import acapulco.rulesgeneration.activationdiagrams.FADDotGenerator
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths
import java.text.DateFormat
import java.time.Instant
import java.util.Date

class PaperIllustrationsGenerator {
	def static void main(String[] args) {
		val fmPath = "testdata/ad-test-1.sxfm.xml"
		val fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString)

		val outputFolderPath = Paths.
			get('''testoutputs/«DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date.from(Instant.now)).replaceAll("[/,:]", ".")»/''').
			toString
		val outputFolder = new File(outputFolderPath)
		outputFolder.mkdirs

		val fad = new FeatureActivationDiagram(fm)
		extension val fh = new FeatureModelHelper(fm)
		val alwaysActiveFeatures = fh.alwaysActiveFeatures
		val allRealOptionalFeatures = fh.features.reject[alwaysActiveFeatures.contains(it)].toSet
		allRealOptionalFeatures.forEach[f |
			fad.calculateSubdiagramFor(f, false)
			fad.calculateSubdiagramFor(f, true)
		]

		val fadDotFile = new File(outputFolderPath +
			'''/«fm.name».dot''')
		try (val writer = new FileWriter(fadDotFile)) {
			val generator = new FADDotGenerator(fad)
			writer.write(generator.render)
			writer.flush
		}
	}
}
