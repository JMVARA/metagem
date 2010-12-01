package kybele.metagem.ui.launchs.hybrid2rubytl;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import kybele.metagem.ui.api.Transformations;
import kybele.metagem.ui.api.ValidationExecution;
import kybele.metagem.ui.utils.Constants;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

public class HYBRID2RubyTLLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e1) {
			;
		}		
		
		if (monitor == null)
			monitor = new NullProgressMonitor();		
		
		monitor.beginTask("Launching transformation", 100);
		monitor.subTask("Getting configuration info");
		String IN = getINAtt(configuration);
		File fIN=new File(IN);
		String uriIN=fIN.toURI().toString();
		String OUT = getOUTAtt(configuration);
		File fOUT=new File(OUT);
		String uriOUT=fOUT.toURI().toString();
		monitor.worked(10);
		if (monitor.isCanceled()) 
			return;
		
		boolean isValid = false;
		isValid=true;
//		try {
//			isValid = ValidationExecution.isValid(new Path(uriIN).lastSegment(), uriIN, Constants.HYBRIDURI,Constants.RubyTLURI,Constants.SHOW_DIALOG);
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, "One or more models cannot be found.\nPlease, check your Launch Configuration", "Problems Launching...", JOptionPane.ERROR_MESSAGE);
//			isValid=false;
//		}
		if(isValid)
		{
			monitor.subTask("Executing transformation");
			try{
				//ValidationExecution.isValid(new Path(uriIN).lastSegment(), uriIN, Constants.HYBRIDURI,Constants.RubyTLURI, Constants.SHOW_WARNING);
				Transformations transf = Transformations.getInstance();
				transf.hybrid2rubytl(uriIN, uriOUT);
				monitor.worked(85);		
				if(monitor.isCanceled())
					return;
			}catch (Exception e){
				JOptionPane.showMessageDialog(null, "One or more models cannot be found.\nPlease, check your Launch Configuration", "Problems Launching...", JOptionPane.ERROR_MESSAGE);
			}
			monitor.subTask("Transformation finished");
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IFolder.DEPTH_INFINITE, null);
			monitor.done();
		}
	}
	
	private String getINAtt(ILaunchConfiguration configuration) throws CoreException
	{
		return getTransformationAtt(configuration, HYBRID2RUBYTLConstants.ATTR_TRANSFORMATION_IN);
	}
	
		
	private String getOUTAtt(ILaunchConfiguration configuration) throws CoreException
	{
		return getTransformationAtt(configuration, HYBRID2RUBYTLConstants.ATTR_TRANSFORMATION_OUT);
	}
	
	private String getTransformationAtt(ILaunchConfiguration configuration,String id) throws CoreException{
		
		String returned = null;

		returned = configuration.getAttribute(id, (String)null);

		return returned;
	}
}