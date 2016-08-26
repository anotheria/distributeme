package org.distributeme.generator.ws;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.PrintWriter;


/**
 * <p>ConfigurationGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class ConfigurationGenerator extends WSStructureGenerator implements WebServiceMeGenerator {

	/**
	 * <p>Constructor for ConfigurationGenerator.</p>
	 *
	 * @param filer a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public ConfigurationGenerator(ProcessingEnvironment filer) {
		super(filer);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(TypeElement type) {
		// create WebContent folder structure
		// create META-INF directory
		PrintWriter writer = createTextFile(type.getSimpleName().toString(), getMetaInfDir(), "MANIFEST", "MF");
		writer.print("Manifest-Version: 1.0\n");
		writer.print("Class-Path: \n");
		closeWriter(writer);
		// create libraries directory
		writer = createTextFile(type.getSimpleName().toString(), getWebInfLibDir(), "readme", "txt");
		writer.print("Directory for project libraries");
		closeWriter(writer);

		// generate web.xml
		generateWebXml(type);

		// generate sun-jaxws.xml
		generateSunJaxWsXml(type);
	}

	private void generateWebXml(Element type) {
		PrintWriter writer = createXmlFile(type.getSimpleName().toString(), getWebInfDir(), "web");

		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.print("<web-app id=\"WebApp_ID\" version=\"2.4\" xmlns=\"http://java.sun.com/xml/ns/j2ee\"\n");
		writer.print("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		writer.print("\txsi:schemaLocation=\"http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd\">\n");
		writer.print("\t<display-name>" + type.getSimpleName().toString() + "</display-name>\n");
		writer.print("\t<listener>\n");
		writer.print("\t\t<listener-class>com.sun.xml.ws.transport.http.servlet.WSServletContextListener</listener-class>\n");
		writer.print("\t</listener>\n");
		writer.print("\t<servlet>\n");
		writer.print("\t\t<servlet-name>" + type.getSimpleName().toString() + "</servlet-name>\n");
		writer.print("\t\t<servlet-class>com.sun.xml.ws.transport.http.servlet.WSServlet</servlet-class>\n");
		writer.print("\t\t<load-on-startup>1</load-on-startup>\n");
		writer.print("\t</servlet>\n");
		writer.print("\t<servlet-mapping>\n");
		writer.print("\t\t<servlet-name>" + type.getSimpleName().toString() + "</servlet-name>\n");
		writer.print("\t\t<url-pattern>/" + type.getSimpleName().toString() + "</url-pattern>\n");
		writer.print("\t</servlet-mapping>\n");
		writer.print("</web-app>\n");

		closeWriter(writer);
	}

	private void generateSunJaxWsXml(Element type) {
		PrintWriter writer = createXmlFile(type.getSimpleName().toString(), getWebInfDir(), "sun-jaxws");

		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.print("<endpoints xmlns='http://java.sun.com/xml/ns/jax-ws/ri/runtime' version='2.0'>\n");
		writer.print("\t<endpoint\n");
		writer.print("\t\tname='" + type.getSimpleName().toString() + "'\n");
		writer.print("\t\timplementation='" + getWSProxyName(type) + "'\n");
		writer.print("\t\turl-pattern='/" + type.getSimpleName().toString() + "'/>\n");
		writer.print("</endpoints>");

		closeWriter(writer);
	}

}
