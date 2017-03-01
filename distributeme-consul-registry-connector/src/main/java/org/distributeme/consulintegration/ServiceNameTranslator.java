package org.distributeme.consulintegration;

import net.anotheria.util.StringUtils;


/**
 * Created by rboehling on 3/1/17.
 */
class ServiceNameTranslator {

	private ServiceNameTranslator() {
	}

	static String toConsul(String serviceId) {
		return StringUtils.replace(serviceId, '_', '-');
	}
	static String fromConsul(String serviceId) {
		return StringUtils.replace(serviceId, '-', '_');
	}
}
