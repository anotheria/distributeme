package org.distributeme.generator;

public class TranslatedAnnotation {
	/**
	 * Class name of the strategy class.
	 */
	private String strategyClass;
	/**
	 * Parameter of the strategy class (from source annotation).
	 */
	private String parameter;
	/**
	 * Order at which this annotation was found.
	 */
	private int order ;

	/**
	 * Name of the configuration if supported by source annotation.
	 */
	private String configurationName;
	
	public TranslatedAnnotation(String aStrategyClass, String aParameter, int anOrder){
		strategyClass = aStrategyClass;
		parameter = aParameter;
		order = anOrder;
	}
	
	public String getStrategyClass() {
		return strategyClass;
	}
	public void setStrategyClass(String strategyClass) {
		this.strategyClass = strategyClass;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public int getOrder() {
		return order;
	}

	public String getConfigurationName() {
		return configurationName;
	}

	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
}
