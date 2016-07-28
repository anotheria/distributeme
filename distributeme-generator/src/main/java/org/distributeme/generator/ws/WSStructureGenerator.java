package org.distributeme.generator.ws;

import org.distributeme.generator.AbstractGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class WSStructureGenerator extends AbstractGenerator {

	private static final Marker FATAL = MarkerFactory.getMarker("FATAL");

	private static final Logger LOGGER = LoggerFactory.getLogger(WSStructureGenerator.class.getName());

	private final Filer filer;

    protected WSStructureGenerator(ProcessingEnvironment environment) {
        super(environment);
		this.filer = environment.getFiler();
	}

	protected final PrintWriter createXmlFile(String serviceName, String relativePath, String fileName) {
		return createTextFile(serviceName, relativePath, fileName, "xml");
	}

	protected final PrintWriter createTextFile(String serviceName, String relativePath, String pkg, String fileName, String extension) {
		return createTextFile(serviceName, relativePath + File.separator + packageToFolderPath(pkg), fileName, extension);
	}

	protected final PrintWriter createTextFile(String serviceName, String relativePath, String fileName, String extension) {
		try {
			String url = ".." + File.separator + "ws" + File.separator + serviceName + File.separator;
			url += relativePath + File.separator;
			url += fileName + '.' + extension;
			FileObject fileObject = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", url);
			return new PrintWriter(fileObject.openWriter());
		} catch (IOException e) {
			String exceptionMessage = "Generation error. Create text file failure: ";
			LOGGER.error(FATAL, exceptionMessage + e.getMessage(), e);
			throw new RuntimeException(exceptionMessage, e);
		}
	}

	protected final PrintWriter createSourceFile(String serviceName, String pkg, String fileName) {
		try {
			String url = pkg.replace(".", File.separator) + File.separator;
			url += fileName + ".java";
            FileObject fileObject = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", url);
            return new PrintWriter(fileObject.openWriter());
		} catch (IOException e) {
			String exceptionMessage = "Generation error. Create text file failure: ";
			LOGGER.error(FATAL, exceptionMessage + e.getMessage(), e);
			throw new RuntimeException(exceptionMessage, e);
		}
	}

	protected static final void closeWriter(PrintWriter writer) {
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}

	protected static final void closeOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				String exceptionMessage = "Generation error: ";
				LOGGER.error(FATAL, exceptionMessage + e.getMessage(), e);
				throw new RuntimeException(exceptionMessage, e);
			}
		}
	}

	protected final String getWSProxyPackage(Element type) {
        PackageElement packageElement = getPackageOf(type);
        return packageElement + ".generated.ws";
	}

    protected static final String getWSProxySimpleName(Element type) {
		return type.getSimpleName() + "WebSkeleton";
	}

	protected final String getWSProxyName(Element type) {
		return getWSProxyPackage(type) + '.' + getWSProxySimpleName(type);
	}

	protected static final String packageToFolderPath(String pkg) {
		return pkg.replace(".", File.separator);
	}

	protected static final String getMetaInfDir() {
		return "META-INF";
	}

	protected static final String getWebInfDir() {
		return "WEB-INF";
	}

	protected static final String getWebInfLibDir() {
		return getWebInfDir() + File.separator + "lib";
	}

	protected static final String getWebInfClassesDir() {
		return getWebInfDir() + File.separator + "classes";
	}
}
