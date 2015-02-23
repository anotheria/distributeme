package org.distributeme.core.listener;
/**
 * The ServerLifecycleSysOutPrinterListener simply prints out START and END messages to the stdout.
 * @author lrosenberg
 */
public class ServerLifecycleSysOutPrinterListener implements ServerLifecycleListener{

	@Override
	public void afterStart() {
		System.out.println("%%% SERVER STARTED %%%");
	}

	@Override
	public void beforeShutdown() {
		System.out.println("%%% SERVER GOES DOWN %%%");
	}

}
