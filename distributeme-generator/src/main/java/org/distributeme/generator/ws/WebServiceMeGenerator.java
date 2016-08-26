package org.distributeme.generator.ws;


import javax.lang.model.element.TypeElement;

/**
 * <p>WebServiceMeGenerator interface.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public interface WebServiceMeGenerator {

	/**
	 * <p>generate.</p>
	 *
	 * @param type a {@link javax.lang.model.element.TypeElement} object.
	 */
	void generate(TypeElement type);

}
