package org.distributeme.test.lifecycle;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.distributeme.core.ServiceDescriptor;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class AbstractLifecycleTest {
	protected static List<ServiceDescriptor> parse(String content) throws Exception{
		SAXBuilder reader = new SAXBuilder();
		reader.setValidation(false);

		Document doc = reader.build(new StringReader(content));
		ArrayList<ServiceDescriptor> ret = new ArrayList<ServiceDescriptor>();
		Element root = doc.getRootElement();
	
		@SuppressWarnings("unchecked")List<Element> services = root.getChildren();
		for (Element service : services){
			String regString = service.getAttributeValue("registrationString");
			ServiceDescriptor sd = ServiceDescriptor.fromRegistrationString(regString);
			ret.add(sd);
		}
	
		return ret;
	}

}
