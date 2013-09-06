package org.distributeme.test.echo;

import org.distributeme.core.ServiceLocator;


public class MakeRemoteCallsForever {
	public static void main(String a[]) throws Exception{
		
		EchoService service = ServiceLocator.getRemote(EchoService.class);
		double totalDuration = 0.0;
		long totalCount = 0L;
		long errorCount = 0L;
		while(true){
			totalCount++;
			Echo echo = new Echo();
			System.out.println("sending echo: "+echo);
			long start = System.nanoTime();
			boolean success = false;
			try{
				echo = service.echo(echo);
				success = true;
			}catch(Exception e){
				errorCount++;
				System.out.println("ERROR "+e.getMessage());
			}
			long end = System.nanoTime();
			double duration = ((double)(end-start)) / 1000 / 1000;
			totalDuration += duration;
			System.out.println((success ? "received":"FAILED")+" echo: "+echo+" in "+duration +" ms, avg: "+(totalDuration/totalCount)+" ms, count: "+totalCount+", errors: "+errorCount);
		}
	}
}
