package org.distributeme.core.util;

/**
 * Created by rboehling on 2/22/17.
 */
public class SystemTimeProvider implements TimeProvider{

	@Override
	public long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
}
