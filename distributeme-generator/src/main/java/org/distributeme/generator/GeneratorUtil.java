package org.distributeme.generator;

import net.anotheria.util.StringUtils;
import org.distributeme.annotation.CombinedService;
import org.distributeme.generator.jaxrs.ResourceGenerator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for generators.
 *
 * @author lrosenberg
 */
public class GeneratorUtil {

    private static Class<? extends Generator>[] RMI_GENERATORS = new Class[]{
            RemoteInterfaceGenerator.class,
            StubGenerator.class,
            SkeletonGenerator.class,
            ServerGenerator.class,
            ConstantsGenerator.class,
            RemoteFactoryGenerator.class,
            ServerScriptGenerator.class,
            AsynchInterfaceGenerator.class,
            AsynchStubGenerator.class,
            AsynchFactoryGenerator.class,
    };
    private static Class<? extends Generator>[] COMBINED_SERVER_GENERATORS = new Class[]{
            ServerGenerator.class,
            ServerScriptGenerator.class
    };

    private static Class<? extends Generator>[] JAXRS_GENERATORS = new Class[]{
            ResourceGenerator.class,
            StubGenerator.class,
            ConstantsGenerator.class
    };

    public static void generateJAXRS(TypeElement type, ProcessingEnvironment environment) {
        System.out.println("DistributeMe JAXRS generation started for type: " + type);
        Map<String, String> generatorOptions = getGeneratorOptions(environment);
        System.out.println("Found " + generatorOptions.size() + " Options:");
        for (String option : generatorOptions.keySet())
            System.out.println(option + " : " + generatorOptions.get(option));
        Filer filer = environment.getFiler();

        generate(type, environment, generatorOptions, filer, JAXRS_GENERATORS);

        System.out.println("DistributeMe generation finished.");
    }

    private static void generate(TypeElement type, ProcessingEnvironment environment, Map<String, String> generatorOptions, Filer filer, Class<? extends Generator>[] generatorClasses) {
        for (Class<? extends Generator> generatorClass : generatorClasses) {
            try {
                Generator g  = generatorClass.getDeclaredConstructor(ProcessingEnvironment.class).newInstance(environment);
                g.generate(type, filer, generatorOptions);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void generateRMI(TypeElement type, ProcessingEnvironment environment) {
        System.out.println("DistributeMe RMI generation started for type: " + type);
        Map<String, String> generatorOptions = getGeneratorOptions(environment);
        System.out.println("Found " + generatorOptions.size() + " Options:");
        for (Map.Entry<String, String> optionEntry : generatorOptions.entrySet())
            System.out.println(optionEntry.getKey() + " : " + optionEntry.getValue());
        Filer filer = environment.getFiler();

        generate(type, environment, generatorOptions, filer, getGeneratorsForType(type));

        System.out.println("DistributeMe generation finished.");

    }

    private static Class<? extends Generator>[] getGeneratorsForType(Element type) {
        //check for combined service annotation
        CombinedService combinedServiceAnn = type.getAnnotation(CombinedService.class);
        if (combinedServiceAnn != null) {
            return COMBINED_SERVER_GENERATORS;
        }
        return RMI_GENERATORS;

    }

    private static Map<String, String> getGeneratorOptions(ProcessingEnvironment environment) {
        Map<String, String> ret = new HashMap<String, String>();
        for (String option : environment.getOptions().keySet()) {
            if (!option.startsWith("-A"))
                continue;
            String[] optionTokens = StringUtils.tokenize(option, '=');
            if (optionTokens.length > 2)
                throw new IllegalArgumentException("Wrong format of generator option: " + option + ": expected -Aname=value");
            String name = optionTokens[0].substring("-A".length());
            String value = optionTokens[1];
            ret.put(name, value);
        }
        return ret;
    }
}
