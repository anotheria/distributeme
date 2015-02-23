package org.distributeme.core.asynch;

/**
 * General interface all asynch stubs implement.
 * @author lrosenberg
 *
 */
public interface AsynchStub {
	/**
	 * Shutdowns the internal thread pool.
	 */
	void shutdown();
}
