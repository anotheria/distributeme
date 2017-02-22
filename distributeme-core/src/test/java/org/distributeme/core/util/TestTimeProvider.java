package org.distributeme.core.util;

/**
 * Created by rboehling on 2/22/17.
 */
public class TestTimeProvider implements TimeProvider {

	private long currentMillis;

	@Override
	public long getCurrentTimeMillis() {
		return currentMillis;
	}

	public void setCurrentMillis(long currentMillis) {
		this.currentMillis = currentMillis;
	}
}
