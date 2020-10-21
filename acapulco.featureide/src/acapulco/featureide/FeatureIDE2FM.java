package acapulco.featureide;

import java.util.Collection;
import java.util.Iterator;

import acapulco.model.CrossTreeConstraint;
import acapulco.model.CrossTreeConstraintType;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;
import acapulco.model.GroupFeature;
import acapulco.model.MDEOptimiser4EFMFactory;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelStructure;
import de.ovgu.featureide.fm.core.base.IFeatureStructure;
import acapulco.model.helper.FeatureModelHelper;

public class FeatureIDE2FM {

	public static FeatureModel create(IFeatureModel fm) {
		// fm and root
		FeatureModel myfm = MDEOptimiser4EFMFactory.eINSTANCE.createFeatureModel();
		IFeatureModelStructure structure = fm.getStructure();
		IFeatureStructure fs = structure.getRoot();
		myfm.setName(fs.getFeature().getName());

		Feature root = createStructure(fs);
		myfm.setOwnedRoot(root);
		
		// constraints
		// TODO currently only support for implies and mutex
		for(IConstraint c : fm.getConstraints()) {
			Collection<IFeature> f = c.getContainedFeatures();
			if(f.size() == 2) {
				Iterator<IFeature> i = f.iterator();
				IFeature left = i.next();
				IFeature right = i.next();
				// requires and excludes
				// TODO basic string comparison, there are other possible representations for excludes
				//System.out.println(c);
				if (c.getDisplayName().contains(" | ") || c.getDisplayName().contains(" => ")) {
					CrossTreeConstraint ctc = MDEOptimiser4EFMFactory.eINSTANCE.createCrossTreeConstraint();
					ctc.setLeftFeature(FeatureModelHelper.getFeatureByName(myfm, left.getName()));
					ctc.setRightFeature(FeatureModelHelper.getFeatureByName(myfm, right.getName()));
					//System.out.println(c.getDisplayName());
					if (c.getDisplayName().startsWith("-") && c.getDisplayName().contains(" | -")) {
						ctc.setType(CrossTreeConstraintType.EXCLUDES);
					} else if (c.getDisplayName().startsWith("-") && c.getDisplayName().contains(" => -")) {
						ctc.setType(CrossTreeConstraintType.EXCLUDES);
					} else {
						ctc.setType(CrossTreeConstraintType.REQUIRES);	
						if(c.getDisplayName().contains(" | -") || c.getDisplayName().contains(" => -")) {
							ctc.setLeftFeature(FeatureModelHelper.getFeatureByName(myfm, right.getName()));
							ctc.setRightFeature(FeatureModelHelper.getFeatureByName(myfm, left.getName()));	
						}
					}
					myfm.getCrossTreeConstraints().add(ctc);
				}
			}
		}

		return myfm;
	}

	/**
	 * To be used recursively
	 * 
	 * @param current
	 *            featureIde feature structure
	 * @return feature
	 */
	public static Feature createStructure(IFeatureStructure current) {
		IFeature f = current.getFeature();
		Feature myf = null;

		// group feature or not
		if (current.isAlternative() || current.isOr()) {
			myf = MDEOptimiser4EFMFactory.eINSTANCE.createGroupFeature();
			if (current.isAlternative()) {
				((GroupFeature) myf).setChildMinCardinality(1);
				((GroupFeature) myf).setChildMaxCardinality(1);
			} else if (current.isOr()) {
				((GroupFeature) myf).setChildMinCardinality(1);
				((GroupFeature) myf).setChildMaxCardinality(current.getChildrenCount());
			}
		} else {
			myf = MDEOptimiser4EFMFactory.eINSTANCE.createFeature();
		}

		// mandatory or not
		if (current.getParent() != null && (current.getParent().isAlternative() || current.getParent().isOr())) {
			// in featureide the children of group feature are marked as
			// mandatory
			myf.setOptional(true);
		} else if (current.isMandatory()) {
			myf.setOptional(false);
		} else {
			myf.setOptional(true);
		}

		myf.setName(f.getName());

		// children
		for (IFeatureStructure cfs : current.getChildren()) {
			Feature c = createStructure(cfs);
			myf.getOwnedFeatures().add(c);
		}

		return myf;
	}

}
