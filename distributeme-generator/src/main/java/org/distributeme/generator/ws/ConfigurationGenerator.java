package org.distributeme.generator.ws;

import java.io.PrintWriter;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.TypeDeclaration;

public class ConfigurationGenerator extends WSStructureGenerator implements WebServiceMeGenerator {

	public ConfigurationGenerator(Filer filer) {
		super(filer);
	}

	@Override
	public void generate(TypeDeclaration type) {
		// create WebContent folder structure
		// create META-INF directory
		PrintWriter writer = createTextFile(type.getSimpleName(), getMetaInfDir(), "MANIFEST", "MF");
		writer.print("Manifest-Version: 1.0\n");
		writer.print("Class-Path: \n");
		closeWriter(writer);
		// create libraries directory
		writer = createTextFile(type.getSimpleName(), getWebInfLibDir(), "readme", "txt");
		writer.print("Directory for project libraries");
		closeWriter(writer);

		// generate web.xml
		generateWebXml(type);

		// generate sun-jaxws.xml
		generateSunJaxWsXml(type);
	}

	private void generateWebXml(TypeDeclaration type) {
		PrintWriter writer = createXmlFile(type.getSimpleName(), getWebInfDir(), "web");

		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.print("<web-app id=\"WebApp_ID\" version=\"2.4\" xmlns=\"http://java.sun.com/xml/ns/j2ee\"\n");
		writer.print("\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		writer.print("\txsi:schemaLocation=\"http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd\">\n");
		writer.print("\t<display-name>" + type.getSimpleName() + "</display-name>\n");
		writer.print("\t<listener>\n");
		writer.print("\t\t<listener-class>com.sun.xml.ws.transport.http.servlet.WSServletContextListener</listener-class>\n");
		writer.print("\t</listener>\n");
		writer.print("\t<servlet>\n");
		writer.print("\t\t<servlet-name>" + type.getSimpleName() + "</servlet-name>\n");
		writer.print("\t\t<servlet-class>com.sun.xml.ws.transport.http.servlet.WSServlet</servlet-class>\n");
		writer.print("\t\t<load-on-startup>1</load-on-startup>\n");
		writer.print("\t</servlet>\n");
		writer.print("\t<servlet-mapping>\n");
		writer.print("\t\t<servlet-name>" + type.getSimpleName() + "</servlet-name>\n");
		writer.print("\t\t<url-pattern>/" + type.getSimpleName() + "</url-pattern>\n");
		writer.print("\t</servlet-mapping>\n");
		writer.print("</web-app>\n");

		closeWriter(writer);
	}

	private void generateSunJaxWsXml(TypeDeclaration type) {
		PrintWriter writer = createXmlFile(type.getSimpleName(), getWebInfDir(), "sun-jaxws");

		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.print("<endpoints xmlns='http://java.sun.com/xml/ns/jax-ws/ri/runtime' version='2.0'>\n");
		writer.print("\t<endpoint\n");
		writer.print("\t\tname='" + type.getSimpleName() + "'\n");
		writer.print("\t\timplementation='" + getWSProxyName(type) + "'\n");
		writer.print("\t\turl-pattern='/" + type.getSimpleName() + "'/>\n");
		writer.print("</endpoints>");

		closeWriter(writer);
	}

}
