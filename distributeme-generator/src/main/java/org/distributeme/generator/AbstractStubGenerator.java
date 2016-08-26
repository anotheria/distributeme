package org.distributeme.generator;


import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

/**
 * Base class for stub generators.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class AbstractStubGenerator extends AbstractGenerator{
	/**
	 * <p>Constructor for AbstractStubGenerator.</p>
	 *
	 * @param environment a {@link javax.annotation.processing.ProcessingEnvironment} object.
	 */
	public AbstractStubGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

	/**
	 * <p>convertReturnValue.</p>
	 *
	 * @param returnType a {@link javax.lang.model.type.TypeMirror} object.
	 * @param returnValue a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected String convertReturnValue(TypeMirror returnType, String returnValue){
		String rt = returnType.toString();
		if (rt.equals("long")){
			return "((Long)"+returnValue+").longValue()";
		}
		if (rt.equals("int")){
			return "((Integer)"+returnValue+").intValue()";
		}
		if (rt.equals("boolean")){
			return "((Boolean)"+returnValue+").booleanValue()";
		}
		if (rt.equals("double")){
			return "((Double)"+returnValue+").doubleValue()";
		}
		if (rt.equals("float")){
			return "((Float)"+returnValue+").floatValue()";
		}
		if (rt.equals("short")){
			return "((Short)"+returnValue+").shortValue()";
		}
		if (rt.equals("byte")){
			return "((Byte)"+returnValue+").byteValue()";
		}
		return "("+rt+") "+returnValue;
	}

}
