package emf.utils;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.henshin.model.Annotation;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Module;
import org.eclipse.emf.henshin.model.Rule;

public class HenshinModuleGenerator {
	private HenshinFactory factory;
	private EPackage metamodel;
	private Module module;
	
	public HenshinModuleGenerator(String moduleName, EPackage metamodel) {
		this.factory = HenshinFactory.eINSTANCE;
		this.metamodel = metamodel;
		this.module = this.createEmptyModule(moduleName, metamodel);
	}
	
	public Module createEmptyModule(String name, EPackage metamodel) {
		Module module = this.factory.createModule();
		module.setName(name);
	
		// Set the metamodel for this module
		module.getImports().add(metamodel);
		
		return module;
	}
	
	public Rule createEmptyRule(String name, Module module) {
		Rule rule = this.factory.createRule();
		rule.setActivated(true);
		rule.setName(name);
		
		// Add the rule to the module
		module.getUnits().add(rule);
		
		return rule;
	}
	
	/**
	 * Create a nested rule (multirule).
	 */
	public Rule createNestedRule(Rule rule) {
		Rule nestedRule = this.factory.createRule();
		nestedRule.setActivated(true);
		rule.getMultiRules().add(nestedRule);
		
		return nestedRule;
	}
	
	public Module getModule() {
		return this.module;
	}
	
	public Annotation createAnnotation(String key, String value) {
		Annotation a = this.factory.createAnnotation();
		a.setKey(key);
		if (value != null) {
			a.setValue(value);
		}
		return a;
	}
}
