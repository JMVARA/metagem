package kybele.metagem.ui.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import kybele.metagem.ui.Activator;
import kybele.metagem.ui.utils.Constants;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;
import org.osgi.framework.Bundle;

public class Transformations {
	static private Transformations transformations = null;
	private Properties METAGEM2HYBRID_properties;
	private Properties HYBRID2ATL_properties;
	private Properties HYBRID2RUBYTL_properties;
	private Properties HYBRID2ETL_properties;
	
	final static int _METAGEM2HYBRID=1;
	final static int _HYBRID2ATL=2;
	final static int _HYBRID2RUBYTL=3;
	final static int _HYBRID2ETL=4; 
		
	private Transformations() throws IOException {
		
		METAGEM2HYBRID_properties = new Properties();
		METAGEM2HYBRID_properties.load(Utils.getFileURL("MeTAGeM2Hybrid.properties").openStream());
		
		HYBRID2ATL_properties = new Properties();
		HYBRID2ATL_properties.load(Utils.getFileURL("Hybrid2ATL.properties").openStream());
		
		HYBRID2RUBYTL_properties = new Properties();
		HYBRID2RUBYTL_properties.load(Utils.getFileURL("Hybrid2RubyTL.properties").openStream());
		
		HYBRID2ETL_properties = new Properties();
		HYBRID2ETL_properties.load(Utils.getFileURL("Hybrid2ETL.properties").openStream());
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	}
		
//	public void metagem2hybrid(String inFilePath, String leftFilePath, String rightFilePath,String outFilePath) throws Exception {
//		try{
//		Map<String, Object> models=loadModelsMeTAGeM2Hybrid(inFilePath, leftFilePath, rightFilePath);
//		doMeTAGeM2Hybrid(models,new NullProgressMonitor());
//		saveModels(((IModel)models.get("OUT")),outFilePath);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	public void metagem2hybrid(String inFilePath, String outFilePath) throws Exception {
		try{
		Map<String, Object> models=loadModelsMeTAGeM2Hybrid(inFilePath);
		doMeTAGeM2Hybrid(models,new NullProgressMonitor());
		saveModels(((IModel)models.get("OUT")),outFilePath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void doMeTAGeM2Hybrid(Map<String, Object> models, NullProgressMonitor nullProgressMonitor) throws Exception {
		ILauncher launcher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions(_METAGEM2HYBRID);
		launcher.initialize(launcherOptions);
		launcher.addInModel(((IModel)models.get("IN")), "IN", "MeTAGeM");
		//launcher.addInModel(((IModel)models.get("left")), "left", "MOF");
		//launcher.addInModel(((IModel)models.get("right")), "right", "MOF");
		launcher.addOutModel(((IModel)models.get("OUT")), "OUT", "Hybrid");
		launcher.launch("run", nullProgressMonitor, launcherOptions, (Object[]) getModulesList(_METAGEM2HYBRID));
	}

//	private Map<String, Object> loadModelsMeTAGeM2Hybrid(String inFilePath, String leftFilePath,String rightFilePath) throws Exception {
//		Map<String, Object> models = new HashMap<String, Object>();
//		ModelFactory factory = new EMFModelFactory();
//		IInjector injector = new EMFInjector();
//	 	IReferenceModel hybridMetamodel = factory.newReferenceModel();
//		injector.inject(hybridMetamodel, Utils.getFileURL("resources/Hybrid.ecore").toString());
//		IReferenceModel metagemMetamodel = factory.newReferenceModel();
//		injector.inject(metagemMetamodel, Utils.getFileURL("resources/MeTAGeM.ecore").toString());
//		IReferenceModel mofMetamodel = factory.getMetametamodel();
//		IModel metagemInputModel = factory.newModel(metagemMetamodel);
//		injector.inject(metagemInputModel, inFilePath);
//		models.put("IN", metagemInputModel);
//		
//		IModel leftInputModel = factory.newModel(mofMetamodel);
//		injector.inject(leftInputModel, leftFilePath);
//		models.put("left", leftInputModel);
//		
//		IModel rightInputModel = factory.newModel(mofMetamodel);
//		injector.inject(rightInputModel, rightFilePath);
//		models.put("right", rightInputModel);
//		
//		IModel hybridOutputModel = factory.newModel(hybridMetamodel);
//		models.put("OUT", hybridOutputModel);
//		return models;
//	}
	
	private Map<String, Object> loadModelsMeTAGeM2Hybrid(String inFilePath) throws Exception {
		Map<String, Object> models = new HashMap<String, Object>();
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel hybridMetamodel = factory.newReferenceModel();
		injector.inject(hybridMetamodel, Utils.getFileURL("resources/Hybrid.ecore").toString());
		IReferenceModel metagemMetamodel = factory.newReferenceModel();
		injector.inject(metagemMetamodel, Utils.getFileURL("resources/MeTAGeM.ecore").toString());
		IModel metagemInputModel = factory.newModel(metagemMetamodel);
		injector.inject(metagemInputModel, inFilePath);
		models.put("IN", metagemInputModel);
		
		IModel hybridOutputModel = factory.newModel(hybridMetamodel);
		models.put("OUT", hybridOutputModel);
		return models;
	}
	
	
	
	public void hybrid2atl(String inFilePath, String outFilePath) throws Exception {
		try{
		Map<String, Object> models=loadModelsHybrid2ATL(inFilePath);
		doHybrid2ATL(models,new NullProgressMonitor());
		saveModels(((IModel)models.get("OUT")),outFilePath);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// Register ATL metamodel
		Bundle b = Activator.getDefault().getBundle();
		InputStream input = FileLocator.openStream(b, new Path("/src/kybele/metagem/ui/api/resources/ATL.ecore"), false);
		Utils.registerMetamodel(Constants.ATLURI, input);
		input.close();	
		
	}
	
	private void doHybrid2ATL(Map<String, Object> models, NullProgressMonitor nullProgressMonitor) throws Exception {
		ILauncher launcher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions(_HYBRID2ATL);
		launcher.initialize(launcherOptions);
		launcher.addInModel(((IModel)models.get("IN")), "IN", "Hybrid");
		launcher.addOutModel(((IModel)models.get("OUT")), "OUT", "ATL");
		launcher.launch("run", nullProgressMonitor, launcherOptions, (Object[]) getModulesList(_HYBRID2ATL));
	}
	
	private Map<String, Object> loadModelsHybrid2ATL(String inFilePath) throws Exception {
		Map<String, Object> models = new HashMap<String, Object>();
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel hybridMetamodel = factory.newReferenceModel();
		injector.inject(hybridMetamodel, HYBRID2ATL_properties.getProperty("Hybrid2ATL.metamodels.Hybrid"));
		IReferenceModel ATLMetamodel = factory.newReferenceModel();
		injector.inject(ATLMetamodel, Utils.getFileURL("resources/ATL.ecore").toString());

		
		IModel hybridInputModel = factory.newModel(hybridMetamodel);
		injector.inject(hybridInputModel, inFilePath);
		models.put("IN", hybridInputModel);
		
		
		IModel ATLOutputModel = factory.newModel(ATLMetamodel);
		models.put("OUT", ATLOutputModel);
		return models;
	}
	
	
	public void hybrid2rubytl(String inFilePath, String outFilePath) throws Exception {		
		try{
		Map<String, Object> models=loadModelsHybrid2RubyTL(inFilePath);
		doHybrid2RubyTL(models,new NullProgressMonitor());
		saveModels(((IModel)models.get("OUT")),outFilePath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void doHybrid2RubyTL(Map<String, Object> models, NullProgressMonitor nullProgressMonitor) throws Exception {
		ILauncher launcher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions(_HYBRID2RUBYTL);
		launcher.initialize(launcherOptions);
		launcher.addInModel(((IModel)models.get("IN")), "IN", "Hybrid");
		launcher.addOutModel(((IModel)models.get("OUT")), "OUT", "RubyTL");
		launcher.launch("run", nullProgressMonitor, launcherOptions, (Object[]) getModulesList(_HYBRID2RUBYTL));
	}
	
	private Map<String, Object> loadModelsHybrid2RubyTL(String inFilePath) throws Exception {
		Map<String, Object> models = new HashMap<String, Object>();
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel hybridMetamodel = factory.newReferenceModel();
		injector.inject(hybridMetamodel, HYBRID2RUBYTL_properties.getProperty("Hybrid2RubyTL.metamodels.Hybrid"));
		IReferenceModel RubyTLMetamodel = factory.newReferenceModel();
		injector.inject(RubyTLMetamodel, HYBRID2RUBYTL_properties.getProperty("Hybrid2RubyTL.metamodels.RubyTL"));

		
		IModel hybridInputModel = factory.newModel(hybridMetamodel);
		injector.inject(hybridInputModel, inFilePath);
		models.put("IN", hybridInputModel);
		
		
		IModel RubyTLOutputModel = factory.newModel(RubyTLMetamodel);
		models.put("OUT", RubyTLOutputModel);
		return models;
	}

	
	public void hybrid2etl(String inFilePath, String outFilePath) throws Exception {		
		try{
		Map<String, Object> models=loadModelsHybrid2ETL(inFilePath);
		doHybrid2ETL(models,new NullProgressMonitor());
		saveModels(((IModel)models.get("OUT")),outFilePath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void doHybrid2ETL(Map<String, Object> models, NullProgressMonitor nullProgressMonitor) throws Exception {
		ILauncher launcher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions(_HYBRID2RUBYTL);
		launcher.initialize(launcherOptions);
		launcher.addInModel(((IModel)models.get("IN")), "IN", "Hybrid");
		launcher.addOutModel(((IModel)models.get("OUT")), "OUT", "ETL");
		launcher.launch("run", nullProgressMonitor, launcherOptions, (Object[]) getModulesList(_HYBRID2ETL));
	}
	
	private Map<String, Object> loadModelsHybrid2ETL(String inFilePath) throws Exception {
		Map<String, Object> models = new HashMap<String, Object>();
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel hybridMetamodel = factory.newReferenceModel();
		injector.inject(hybridMetamodel, HYBRID2ETL_properties.getProperty("Hybrid2ETL.metamodels.Hybrid"));
		IReferenceModel ETLMetamodel = factory.newReferenceModel();
		injector.inject(ETLMetamodel, HYBRID2ETL_properties.getProperty("Hybrid2ETL.metamodels.ETL"));

		
		IModel hybridInputModel = factory.newModel(hybridMetamodel);
		injector.inject(hybridInputModel, inFilePath);
		models.put("IN", hybridInputModel);
		
		
		IModel ETLOutputModel = factory.newModel(ETLMetamodel);
		models.put("OUT", ETLOutputModel);
		return models;
	}
	
	
	
	/**
	 * Returns an Array of the module input streams, parameterized by the
	 * property file.
	 * 
	 * @return an Array of the module input streams
	 * @throws IOException
	 *             if a module cannot be read
	 *
	 * @generated
	 */
	protected InputStream[] getModulesList(int process) throws IOException {
		InputStream[] modules = null;
		String modulesList="";
		switch (process){
		case 1: modulesList = METAGEM2HYBRID_properties.getProperty("MeTAGeM2Hybrid.modules");
					break;
		case 2:modulesList = HYBRID2ATL_properties.getProperty("Hybrid2ATL.modules");
				break;
		case 3:modulesList = HYBRID2RUBYTL_properties.getProperty("Hybrid2RubyTL.modules");
				break;
		case 4:modulesList = HYBRID2ETL_properties.getProperty("Hybrid2ETL.modules");
			break;
		}
		if (modulesList != null) {
			String[] moduleNames = modulesList.split(",");
			modules = new InputStream[moduleNames.length];
			for (int i = 0; i < moduleNames.length; i++) {
				String asmModulePath = new Path(moduleNames[i].trim()).removeFileExtension().addFileExtension("asm").toString();
				modules[i] = Utils.getFileURL(asmModulePath).openStream();
			}
		}
		return modules;
	}
	
	
	
	/**
	 * Returns the options map, parameterized from the property file.
	 * 
	 * @return the options map
	 *
	 * @generated
	 */
	protected Map<String, Object> getOptions(int process) {
		Properties property=null;
		String text="";
		
		switch(process){
			case 1: property=METAGEM2HYBRID_properties;
					  text="MeTAGEM2HYBRID.options.";
					  break;
			case 2: property=HYBRID2ATL_properties;
			  		text="Hybrid2ATL.options.";
			  		break;
			case 3: property=HYBRID2RUBYTL_properties;
	  				text="Hybrid2RubyTL.options.";
	  				break;
			case 4: property=HYBRID2ATL_properties;
	  				text="Hybrid2ETL.options.";
	  				break;
				
		}
		
		Map<String, Object> options = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : property.entrySet()) {
			if (entry.getKey().toString().startsWith(text)) {
				options.put(entry.getKey().toString().replaceFirst(text, ""), 
				entry.getValue().toString());
			}
		}
		return options;
	}
	
	
	/**
	 * Save the output and input/output models.
	 * 
	 * @param outModelPath
	 *            the OUT model path
	 * @throws ATLCoreException
	 *             if a problem occurs while saving models
	 *
	 * @generated
	 */
	public void saveModels(IModel outModel, String outModelPath) throws ATLCoreException {
		IExtractor extractor = new EMFExtractor();
		extractor.extract(outModel, outModelPath);
	}
	
	
	static synchronized public Transformations getInstance() throws Exception {

		if (transformations == null) {

			transformations = new Transformations();

		}

		return transformations;

	}

	

	
}