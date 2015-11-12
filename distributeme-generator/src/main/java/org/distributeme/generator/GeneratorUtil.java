package org.distributeme.generator;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;
import net.anotheria.util.StringUtils;
import org.distributeme.annotation.CombinedService;
import org.distributeme.generator.jaxrs.ResourceGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Utilities for generators.
 * @author lrosenberg
 */
public class GeneratorUtil {
	
	private static Generator[] RMI_GENERATORS = {
		new RemoteInterfaceGenerator(),
		new StubGenerator(),
		new SkeletonGenerator(),
		new ServerGenerator(),
		new ConstantsGenerator(),
		new RemoteFactoryGenerator(),
		new ServerScriptGenerator(),
		new AsynchInterfaceGenerator(),
		new AsynchStubGenerator(),
		new AsynchFactoryGenerator(),
	};
	private static Generator[] COMBINED_SERVER_GENERATORS = {
		new ServerGenerator(),
		new ServerScriptGenerator()
	};

	private static Generator[] JAXRS_GENERATORS = {
			new ResourceGenerator(),
			new org.distributeme.generator.jaxrs.StubGenerator(),
			new ConstantsGenerator(),
	};

	public static void generateJAXRS(TypeDeclaration type, AnnotationProcessorEnvironment environment ){
		System.out.println("DistributeMe JAXRS generation started for type: "+type);
		Map<String,String> generatorOptions = getGeneratorOptions(environment);
		System.out.println("Found " + generatorOptions.size() + " Options:");
		for(String option: generatorOptions.keySet())
			System.out.println(option + " : " + generatorOptions.get(option));
		Filer filer = environment.getFiler();

		List<Generator> generators = Arrays.asList(JAXRS_GENERATORS);

		for (Generator g : generators){
			//System.out.println("%%% running generator "+g);
			try{
				g.generate(type, filer, generatorOptions);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		System.out.println("DistributeMe generation finished.");
	}

	public static void generateRMI(TypeDeclaration type, AnnotationProcessorEnvironment environment ){
		System.out.println("DistributeMe RMI generation started for type: "+type);
		Map<String,String> generatorOptions = getGeneratorOptions(environment);
		System.out.println("Found " + generatorOptions.size() + " Options:");
		for(Map.Entry<String,String> optionEntry: generatorOptions.entrySet())
			System.out.println(optionEntry.getKey() + " : " + optionEntry.getValue());
		Filer filer = environment.getFiler();
		
		List<Generator> generators = getGeneratorsForType(type);
		
		for (Generator g : generators){
			//System.out.println("%%% running generator "+g);
			try{
				g.generate(type, filer, generatorOptions);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		System.out.println("DistributeMe generation finished.");
		
	}
	
	private static List<Generator> getGeneratorsForType(TypeDeclaration type){
		//check for combined service annotation
		CombinedService combinedServiceAnn = type.getAnnotation(CombinedService.class);
		if (combinedServiceAnn!=null){
			return Arrays.asList(COMBINED_SERVER_GENERATORS);
		}
		return Arrays.asList(RMI_GENERATORS);
	
	}
	
	private static Map<String,String> getGeneratorOptions(AnnotationProcessorEnvironment environment){
		Map<String,String> ret = new HashMap<String, String>();
		for(String option: environment.getOptions().keySet()){
			if(!option.startsWith("-A"))
				continue;
			String[] optionTokens = StringUtils.tokenize(option, '=');
			if(optionTokens.length > 2)
				throw new IllegalArgumentException("Wrong format of generator option: " + option + ": expected -Aname=value");
			String name = optionTokens[0].substring("-A".length());
			String value = optionTokens[1];
			ret.put(name,value);
		}
		return ret;
	}
}
