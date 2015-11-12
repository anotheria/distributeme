package org.distributeme.processors;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.WebServiceMe;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.generator.GeneratorUtil;
import org.distributeme.generator.ws.ConfigurationGenerator;
import org.distributeme.generator.ws.ServiceProxyGenerator;
import org.distributeme.generator.ws.WebServiceMeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Factory for the annotation processor.
 *
 * @author lrosenberg
 */
public class GeneratorProcessorFactory implements AnnotationProcessorFactory {

    /**
     * Constant list for supported annotations.
     */
    private static final Collection<String> supportedAnnotations = Arrays.asList(
            "org.distributeme.annotation.DistributeMe",
            "org.distributeme.annotation.WebServiceMe"
    );
    /**
     * Constant list for supported options.
     */
    private static final Collection<String> supportedOptions = new HashSet<String>();

    @Override
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> set, AnnotationProcessorEnvironment environment) {
        return new GeneratorProcessor(environment);
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    @Override
    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    private static class GeneratorProcessor implements AnnotationProcessor {
        private AnnotationProcessorEnvironment environment;
        private List<WebServiceMeGenerator> wsGenerators;

        public GeneratorProcessor(AnnotationProcessorEnvironment aEnvironment) {
            this.environment = aEnvironment;

            Filer file = environment.getFiler();
            wsGenerators = new ArrayList<WebServiceMeGenerator>();
            wsGenerators.add(new ServiceProxyGenerator(file));
            wsGenerators.add(new ConfigurationGenerator(file));


        }

        @Override
        public void process() {
            for (TypeDeclaration type : environment.getTypeDeclarations()) {
                DistributeMe annotation = type.getAnnotation(DistributeMe.class);
                if (annotation != null) {
                    if (Arrays.asList(annotation.protocols()).contains(ServiceDescriptor.Protocol.RMI)) {
                        try {
							//System.err.println("Generation for RMI disabled! "+type.getSimpleName());
                            GeneratorUtil.generateRMI(type, environment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

					if (Arrays.asList(annotation.protocols()).contains(ServiceDescriptor.Protocol.JAXRS)) {
						try {
							GeneratorUtil.generateJAXRS(type, environment);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
                }
                WebServiceMe wsAnnotation = type.getAnnotation(WebServiceMe.class);
                if (wsAnnotation != null) {
                    for (WebServiceMeGenerator generator : wsGenerators) {
                        generator.generate(type);
                    }
                }
            }

        }

    }

}
