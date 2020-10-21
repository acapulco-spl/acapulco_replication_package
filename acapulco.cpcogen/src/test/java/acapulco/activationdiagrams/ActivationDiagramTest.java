package acapulco.activationdiagrams;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.henshin.model.Rule;
import org.junit.jupiter.api.Test;

import emf.utils.HenshinFileWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import acapulco.featuremodel.configuration.FMConfigurationMetamodelGenerator;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.rulesgeneration.ActivationDiagToRuleConverter;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureActivationSubDiagram;
import acapulco.rulesgeneration.activationdiagrams.FeatureDecision;
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleFeature;
import acapulco.rulesgeneration.activationdiagrams.vbrulefeatures.VBRuleOrAlternative;

public class ActivationDiagramTest {
	@Test
	void testCreateActivationDiagram() {
		String fmPath = "testdata/ad-test-1.sxfm.xml";
		FeatureModel fm = FeatureIDEUtils.loadFeatureModel(Paths.get(fmPath).toString());
		Feature f1 = fm.getOwnedRoot().getOwnedFeatures().get(0);
		assertEquals(f1.getName(), "F1");

		FeatureActivationDiagram ad = new FeatureActivationDiagram(fm); // FM-specific
		String fmName = "ad-test-1";
		FMConfigurationMetamodelGenerator metamodelGen = new FMConfigurationMetamodelGenerator(fm, fmName, fmName,
				"http://+fmName");
		metamodelGen.generateMetamodel();

		for (Feature f : metamodelGen.geteClasses().keySet()) {
			if(f.getName().startsWith("R"))
				continue;
			FeatureActivationSubDiagram sd = ad.calculateSubdiagramFor(f, true); // CPCO-specific
//			printFeatureActivationSubDiagram(sd);
			Rule rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
			HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), "generated\\"+rule.getName()+".hen");
			
			sd = ad.calculateSubdiagramFor(f, false); // CPCO-specific
			rule = ActivationDiagToRuleConverter.convert(sd, metamodelGen.geteClasses());
			HenshinFileWriter.writeModuleToPath(Collections.singletonList(rule), "generated\\"+rule.getName()+".hen");
			
		}
	}

	private void printFeatureActivationSubDiagram(FeatureActivationSubDiagram sd) {
		List<String> outputList = new ArrayList<String>();
		for (Entry<FeatureDecision, Set<VBRuleFeature>> pc : sd.getPresenceConditions().entrySet()) {
			String output = pc.getKey() + " -> ";
			if (pc.getValue().size() == 1) {
				output += pc.getValue().iterator().next().getName();
			} else {
				output += "or( ";
				for (VBRuleFeature pcComponent : pc.getValue()) {
					output += pcComponent.getName();
					output += " ";
				}
				output += " )";
			}
			outputList.add(output);
		}
		java.util.Collections.sort(outputList);

		outputList.forEach(e -> System.out.println(e));
	}
}
