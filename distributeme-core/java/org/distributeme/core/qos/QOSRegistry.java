package org.distributeme.core.qos;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 20.02.15 17:29
 */
public class QOSRegistry {

	private static final QOSRegistry instance = new QOSRegistry();

	public static final QOSRegistry getInstance(){
		return instance;
	}

}
