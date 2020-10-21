package acapulco.featureide.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import acapulco.featureide.FeatureIDE2FM;
import acapulco.featureide.utils.FeatureIDEUtils;
import acapulco.model.FeatureModel;

public class CreateFMAction implements IObjectActionDelegate {

	@Override
	public void run(IAction action) {
		Object sel = ((IStructuredSelection) selection).getFirstElement();
		IResource res = (IResource) sel;
		File file = getFileFromIResource(res);
		IFeatureModel fm = FeatureIDEUtils.load(file);
		FeatureModel myfm = FeatureIDE2FM.create(fm);
		File myfmFile = new File(file.getParent(), "fm.mdeoptimiser4efm");
		try {
			FeatureIDEUtils.saveEObject(myfmFile.toURI(), myfm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ISelection selection = null;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	/**
	 * get File from IResource
	 * 
	 * @param iresource
	 *            (including IFile)
	 * @return File
	 */
	public static File getFileFromIResource(IResource resource) {
		if (resource instanceof IProject) {
			// for some reason rawlocation in projects return null
			IProject project = (IProject) resource;
			if (!project.exists()) {
				return null;
			}
			return project.getLocation().makeAbsolute().toFile();
		}
		if (resource.getRawLocation() != null) {
			return resource.getRawLocation().makeAbsolute().toFile();
		}
		return null;
	}

}
