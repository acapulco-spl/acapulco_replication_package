package acapulco.model.helper;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

import acapulco.model.Feature;
import acapulco.model.FeatureModel;

public class FeatureModelHelper {

	public static Feature getFeatureByName(FeatureModel fm, String featureName) {
		Iterator<EObject> i = fm.eAllContents();
		while(i.hasNext()) {
			EObject eo = i.next();
			if(eo instanceof Feature) {
				if(featureName.equals(((Feature)eo).getName())){
					return ((Feature)eo);
				}
			}
		}
		return null;
	}
}
