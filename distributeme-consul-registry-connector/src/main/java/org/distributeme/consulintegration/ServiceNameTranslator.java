package org.distributeme.consulintegration;

import net.anotheria.util.StringUtils;


/**
 * Distributeme use underscore as separators inside service names. Consul discourage
 * underscores because they do not confirm to dns naming conventions. This util translates
 * serviceId between both worlds: underscores are replaced with dashes.
 *
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
