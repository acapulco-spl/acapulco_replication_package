package emf.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EMFUtils {

	public static EObject loadMetamodel(String filepath) {
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the default resource factory -- only needed for stand-alone!
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		
		// Register the package -- only needed for stand-alone!
		//EcorePackage ecorePackage = EcorePackage.eINSTANCE;
		//resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		// Load the resource using the URI
		Resource resource = resourceSet.getResource(URI.createFileURI(filepath), true);

		try {
			resource.load(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}

		EObject resourceContents = resource.getContents().get(0);
		return resourceContents;
	}

	public static void saveMetamodel(EObject contents, String filepath) {
		ResourceSet metaResourceSet = new ResourceSetImpl();

		// Register XML Factory implementation to handle .ecore files
		metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		//metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(org.eclipse.emf.ecore.xmi.XMLResource.OPTION_ENCODING, "UTF-8");
		
		//Create empty resource with the given URI
		XMIResource metaResource = (XMIResource) metaResourceSet.createResource(URI.createFileURI(filepath));
		metaResource.getDefaultSaveOptions().put(XMIResource.OPTION_ENCODING, "UTF-8");
		
		// Add the Package to contents list of the resource 
		metaResource.getContents().add(contents);

		try {
			// Save the resource
			metaResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public static void saveModel(EObject contents, String filepath) {
		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();

		// Register the P21 resource factory for the .xmi extension
		resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		//resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(org.eclipse.emf.ecore.xmi.XMLResource.OPTION_ENCODING, "UTF-8");

		// Get the resource
		XMIResource resource = (XMIResource) resSet.createResource(URI.createFileURI(filepath), null);
		resource.getDefaultSaveOptions().put(XMIResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
		resource.getDefaultSaveOptions().put(XMIResource.OPTION_ENCODING, "UTF-8");
		resource.getContents().add(contents);

		try {
			resource.save(Collections.emptyMap());
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public static EObject loadModel(String filepath, EPackage ePackage) {
		ResourceSet resourceSet = new ResourceSetImpl();
		
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		 
		resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		 
		Resource resource = resourceSet.getResource(URI.createFileURI(filepath),true);
	
		EObject contents = resource.getContents().get(0);
		
		return contents;
	}
	
	/**
	 * Copy the metamodel to a destination folder.
	 * 
	 * @param baseDir				Base dir.
	 * @param targetFolder			Destination folder.
	 * @param metamodelFilename		Filename of the metamodel.
	 */
	public static void copyMetamodel(String baseDir, String targetFolder, String metamodelFilename) {
		Path resourceSetRoot = Paths.get(baseDir);
		Path metamodel = Paths.get(resourceSetRoot.toString(), metamodelFilename);
		Path outputDirectory = Paths.get(resourceSetRoot.toString(), targetFolder, metamodelFilename);
		
		try {
			Files.deleteIfExists(outputDirectory);
			Files.copy(metamodel, outputDirectory);
		} catch (IOException e) {
			//LOGGER.log(Level.SEVERE, String.format("Error copying metamodel file to serialisation directory %s. The metamodel already exists.", outputDirectory));
			e.printStackTrace();
		}
	}
	
	/**
	 * Move the metamodel to a destination folder.
	 * 
	 * @param baseDir				Base dir.
	 * @param targetFolder			Destination folder.
	 * @param metamodelFilename		Filename of the metamodel.
	 */
	public static void moveMetamodel(String baseDir, String targetFolder, String metamodelFilename) {
		Path resourceSetRoot = Paths.get(baseDir);
		Path metamodel = Paths.get(resourceSetRoot.toString(), metamodelFilename);
		Path outputDirectory = Paths.get(resourceSetRoot.toString(), targetFolder, metamodelFilename);
		
		try {
			Files.deleteIfExists(outputDirectory);
			Files.copy(metamodel, outputDirectory);
			Files.delete(metamodel);
		} catch (IOException e) {
			//LOGGER.log(Level.SEVERE, String.format("Error copying metamodel file to serialisation directory %s. The metamodel already exists.", outputDirectory));
			e.printStackTrace();
		}
	}
	
	 /**
     * Clean up the rule serialisation directory.
     * 
     * @param baseDir	Base dir.
     * @param folder	Folder name to be cleaned.
     */
	public static void cleanUp(String folder) {
		try {
			Path resourceSetRoot = Paths.get(folder);
			File file = resourceSetRoot.toFile();
		
			if(file.exists()) {
				//LOGGER.log(Level.INFO, String.format("Found existing directory %s. Deleting it.", file.getAbsolutePath()));
				
				Files.walk(file.toPath())
				    .sorted(Comparator.reverseOrder())
				    .map(Path::toFile)
				    .forEach(File::delete);
			}
		} catch (IOException e) {
			//LOGGER.log(Level.SEVERE, "Error cleaning up directory for generated rules.");
			e.printStackTrace();
		}
	}
	

}
