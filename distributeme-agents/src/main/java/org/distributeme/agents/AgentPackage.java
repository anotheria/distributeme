package org.distributeme.agents;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains mobile version of an agent.
 * @author lrosenberg
 *
 */
public class AgentPackage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 42L;
	/**
	 * Contains the serialized version of the object.
	 */
	private byte[] serializedData;
	/**
	 * Class definitions.
	 */
	private Map<String, byte[]> clazzDefinitions;
	/**
	 * The name of the root class of the agent.
	 */
	private String rootClazzName;
	
	public AgentPackage(){
		clazzDefinitions = new HashMap<String, byte[]>();
	}
	
	public String toString(){
		return "Package of class " +rootClazzName +" agent data: "+byte2string(serializedData)+", classes: "+clazzDefinitions.keySet();
	}
	
	private String byte2string(byte[] arr){
		return arr == null ? "null" : arr.length+" bytes";
	}

	public byte[] getSerializedData() {
		return serializedData;
	}

	public void setSerializedData(byte[] serializedData) {
		this.serializedData = serializedData;
	}

	public void addClazzDefinition(String className, byte[] data){
		clazzDefinitions.put(className, data);
	}
	
	public byte[] getClazzDefinition(String className){
		return clazzDefinitions.get(className);
	}

	public String getRootClazzName() {
		return rootClazzName;
	}

	public void setRootClazzName(String rootClazzName) {
		this.rootClazzName = rootClazzName;
	}

	public Map<String, byte[]> getClazzDefinitions(){
		return clazzDefinitions;
	}
}
