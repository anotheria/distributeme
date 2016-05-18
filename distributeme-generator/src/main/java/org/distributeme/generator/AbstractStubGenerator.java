package org.distributeme.generator;


import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

/**
 * Base class for stub generators.
 * @author lrosenberg
 *
 */
public class AbstractStubGenerator extends AbstractGenerator{
	public AbstractStubGenerator(ProcessingEnvironment environment) {
		super(environment);
	}

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
