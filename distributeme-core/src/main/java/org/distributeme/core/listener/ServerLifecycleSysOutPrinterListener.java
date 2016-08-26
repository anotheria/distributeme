package org.distributeme.core.listener;
/**
 * The ServerLifecycleSysOutPrinterListener simply prints out START and END messages to the stdout.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ServerLifecycleSysOutPrinterListener implements ServerLifecycleListener{

	/** {@inheritDoc} */
	@Override
	public void afterStart() {
		System.out.println("%%% SERVER STARTED %%%");
	}

	/** {@inheritDoc} */
	@Override
	public void beforeShutdown() {
		System.out.println("%%% SERVER GOES DOWN %%%");
	}

}
