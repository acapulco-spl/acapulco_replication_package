package acapulco.featuremodel;

import java.util.List;
import java.util.stream.Collectors;

import acapulco.model.FeatureModel;
import acapulco.model.QualityAttribute;
import acapulco.model.QualityAttributeAnnotation;
import acapulco.model.QualityAttributesModel;

/**
 * 
 *
 */
public class ExtendedFeatureModelHelper extends FeatureModelHelper {
	private QualityAttributesModel qaModel;
	
	public ExtendedFeatureModelHelper(FeatureModel fm) throws Exception {
		super(fm);
		this.qaModel = fm.getOwnedQualityAttributeModel();
		
		if (qaModel == null) {
			throw new Exception("Quality Attribute Model not initialized.");
		}
	}
	
	public List<QualityAttribute> getQualityAttributes() {
		return this.qaModel.getOwnedQualityAttributes();
	}
	
	public double getQAValue(String featureName, QualityAttribute qa) {
		List<QualityAttributeAnnotation> annotations = qa.getQualityAttributeAnnotations().stream().filter(a -> a.getFeatures()
															.stream().anyMatch(f -> f.getName().equals(featureName))).collect(Collectors.toList());
		return annotations.get(0).getValue();
	}
	
	public QualityAttribute getQAByName(String name) {
		return qaModel.getOwnedQualityAttributes().stream().filter(qa -> qa.getName().equals(name)).findAny().get();
	}
	
}
