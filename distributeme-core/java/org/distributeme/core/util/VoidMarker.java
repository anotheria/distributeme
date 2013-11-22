package org.distributeme.core.util;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
/**
 * This class is used to place void values into the return lists.
 * @author lrosenberg
 *
 */
@JsonSerialize
public class VoidMarker implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Static instance.
	 */
	public static final VoidMarker VOID = new VoidMarker();
	
	@Override public String toString(){
		return "void";
	}

	/**
	 * Prevent from initializing multiple object instances.
	 */
	private VoidMarker(){
		//protected constructor
	}

}
