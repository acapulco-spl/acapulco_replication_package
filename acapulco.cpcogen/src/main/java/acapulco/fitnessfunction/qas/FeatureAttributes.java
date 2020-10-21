package acapulco.fitnessfunction.qas;

import java.util.HashMap;
import java.util.Map;

public class FeatureAttributes {
  private final String featureName;
  private Map<String, Double> attributes;	// qaName -> value
  
  public FeatureAttributes(final String featureName) {
    this.featureName = featureName.toLowerCase();
    this.attributes = new HashMap<String, Double>();
  }
  
  public void addAttribute(String attribute, Double value) {
	  this.attributes.put(attribute, value);
  }

  public String getFeatureName() {
    return this.featureName;
  }

  public Map<String, Double> getAttributes() {
    return this.attributes;
  }
  
  public double getValue(String qaName) {
	  return this.attributes.get(qaName);
  }
  
  public String toString() {
	  String res = featureName + " -> ";
	  for (String qa : attributes.keySet()) {
		  res += qa + ":" + attributes.get(qa) + ",";
	  }
	  return res.substring(0, res.length()-1);
  }
}
