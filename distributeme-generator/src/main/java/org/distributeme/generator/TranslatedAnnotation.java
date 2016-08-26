package org.distributeme.generator;

/**
 * <p>TranslatedAnnotation class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
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
	
	/**
	 * <p>Constructor for TranslatedAnnotation.</p>
	 *
	 * @param aStrategyClass a {@link java.lang.String} object.
	 * @param aParameter a {@link java.lang.String} object.
	 * @param anOrder a int.
	 */
	public TranslatedAnnotation(String aStrategyClass, String aParameter, int anOrder){
		strategyClass = aStrategyClass;
		parameter = aParameter;
		order = anOrder;
	}
	
	/**
	 * <p>Getter for the field <code>strategyClass</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getStrategyClass() {
		return strategyClass;
	}
	/**
	 * <p>Setter for the field <code>strategyClass</code>.</p>
	 *
	 * @param strategyClass a {@link java.lang.String} object.
	 */
	public void setStrategyClass(String strategyClass) {
		this.strategyClass = strategyClass;
	}
	/**
	 * <p>Getter for the field <code>parameter</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * <p>Setter for the field <code>parameter</code>.</p>
	 *
	 * @param parameter a {@link java.lang.String} object.
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * <p>Getter for the field <code>order</code>.</p>
	 *
	 * @return a int.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * <p>Getter for the field <code>configurationName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getConfigurationName() {
		return configurationName;
	}

	/**
	 * <p>Setter for the field <code>configurationName</code>.</p>
	 *
	 * @param configurationName a {@link java.lang.String} object.
	 */
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}
}
