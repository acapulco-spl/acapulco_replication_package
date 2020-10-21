package emf.utils;

import java.util.stream.Collectors;

import acapulco.featuremodel.FeatureModelHelper;
import acapulco.model.CrossTreeConstraint;
import acapulco.model.Feature;
import acapulco.model.FeatureModel;

import org.eclipse.emf.common.util.EList;

public class FMPropositionalFormula {
	private FeatureModelHelper fmHelper;
	
	public FMPropositionalFormula(FeatureModel fm) {
		this.fmHelper = new FeatureModelHelper(fm);
	}
	
	public String encodeFM() {
		StringBuffer result = new StringBuffer();
		for (Feature f : fmHelper.getFeatures()) {
			if (fmHelper.isORGroup(f)) {
				String s = encodeOrGroup(f);
				if (!s.isEmpty()) result.append(s).append(" & "); 
			} else if (fmHelper.isXORGroup(f)) {
				String s = encodeXorGroup(f);
				if (!s.isEmpty()) result.append(s).append(" & ");
			} else {
				String s = encodeMandatoryChildren(f);
				if (!s.isEmpty()) result.append(s).append(" & "); 
				
				s = encodeOptionalChildren(f);
				if (!s.isEmpty()) result.append(s).append(" & "); 
			}
			String s = encodeRequiresCTC(f);
			if (!s.isEmpty()) result.append(s).append(" & "); 
			
			s = encodeExcludesCTC(f);
			if (!s.isEmpty()) result.append(s).append(" & "); 
		}
		if (result.length() > 0) {
			result = result.delete(result.length()-3, result.length());
		}
		return result.toString();
	}
	
	
	
	public String encodeOrGroup(Feature orGroupFeature) {
		StringBuffer result = new StringBuffer();
		result.append(orGroupFeature.getName());
		result.append(" <=> ");
		result.append(orGroupFeature.getOwnedFeatures().stream().map(c -> c.getName()).collect(Collectors.joining(" | ", "(", ")")));
		return "(" + result.toString() + ")";
	}
	
	public String encodeXorGroup(Feature xorGroupFeature) {
		StringBuffer result = new StringBuffer();
		for (Feature c : xorGroupFeature.getOwnedFeatures()) {
			StringBuffer sc = new StringBuffer();
			sc.append(c.getName());
			sc.append(" <=> (");
			for (Feature c2 : xorGroupFeature.getOwnedFeatures()) {
				if (!c2.equals(c)) {
					sc.append("~").append(c2.getName()).append(" & ");
				}
			}
			sc.append(xorGroupFeature.getName());
			sc.append(")");
			result.append(sc);
			result.append(" & ");
		}
		if (result.length() > 0) {
			result = result.delete(result.length()-3, result.length());
		}
		return "(" + result.toString() + ")";
	}
	
	public String encodeMandatoryChildren(Feature parent) {
		StringBuffer result = new StringBuffer();
		for (Feature c : parent.getOwnedFeatures()) {
			if (!c.isOptional()) {
				result.append("(" + parent.getName() + " <=> " + c.getName() + ")");
				result.append(" & ");
			}
		}
		if (result.length() > 0) {
			result = result.delete(result.length()-3, result.length());
		}
		if (result.toString().contains("&")) {
			return "(" + result.toString() + ")";
		} else {
			return result.toString();
		}
	}
	
	public String encodeOptionalChildren(Feature parent) {
		StringBuffer result = new StringBuffer();
		for (Feature c : parent.getOwnedFeatures()) {
			if (c.isOptional()) {
				result.append("(" + c.getName() + " => " + parent.getName() + ")");
				result.append(" & ");
			}
		}
		if (result.length() > 0) {
			result = result.delete(result.length()-3, result.length());
		}
		if (result.toString().contains("&")) {
			return "(" + result.toString() + ")";
		} else {
			return result.toString();
		}
	}
	
	public String encodeRequiresCTC(Feature leftFeature) {
		StringBuffer result = new StringBuffer();
		for (CrossTreeConstraint ctc : fmHelper.getREQUIRESCrossTreeConstraints(leftFeature)) {
			result.append("(" + ctc.getLeftFeature().getName() + " => " + ctc.getRightFeature().getName() + ")");
			result.append(" & ");
		}
		if (result.length() > 0) {
			result = result.delete(result.length()-3, result.length());
		}
		if (result.toString().contains("&")) {
			return "(" + result.toString() + ")";
		} else {
			return result.toString();
		}
	}
	
	public String encodeExcludesCTC(Feature leftFeature) {
		StringBuffer result = new StringBuffer();
		for (CrossTreeConstraint ctc : fmHelper.getEXCLUDESCrossTreeConstraints(leftFeature)) {
			result.append("(~(" + ctc.getLeftFeature().getName() + " & " + ctc.getRightFeature().getName() + "))");
			result.append(" & ");
		}
		if (result.length() > 0) {
			result = result.delete(result.length()-3, result.length());
		}
		if (result.toString().contains(") &")) {
			return "(" + result.toString() + ")";
		} else {
			return result.toString();
		}
	}
}
