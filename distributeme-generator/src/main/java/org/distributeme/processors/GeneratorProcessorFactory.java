package org.distributeme.processors;

import org.distributeme.annotation.DistributeMe;
import org.distributeme.core.ServiceDescriptor;
import org.distributeme.generator.GeneratorUtil;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Factory for the annotation processor.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class GeneratorProcessorFactory implements Processor {

    /**
     * Constant list for supported annotations.
     */
    private static final Set<String> supportedAnnotations = new HashSet<String>(Arrays.asList(
            "org.distributeme.annotation.DistributeMe"
    ));
    /**
     * Constant list for supported options.
     */
    private static final Set<String> supportedOptions = new HashSet<String>();

    private ProcessingEnvironment environment;

    /** {@inheritDoc} */
    @Override
    public void init(ProcessingEnvironment processingEnv) {
        this.environment = processingEnv;
    }

    /** {@inheritDoc} */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            DistributeMe annotation = element.getAnnotation(DistributeMe.class);


            if (element instanceof TypeElement) {
                TypeElement type = (TypeElement) element;

                if (annotation != null) {

                    if (Arrays.asList(annotation.protocols()).contains(ServiceDescriptor.Protocol.RMI)) {
                        try {
                            //System.err.println("Generation for RMI disabled! "+type.getSimpleName().toString());
                            GeneratorUtil.generateRMI(type, environment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotations;
    }

    /** {@inheritDoc} */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> getSupportedOptions() {
        return supportedOptions;
    }

}
