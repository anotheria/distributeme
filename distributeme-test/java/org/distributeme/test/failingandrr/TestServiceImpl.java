package org.distributeme.test.failingandrr;

public class TestServiceImpl implements TestService{

	@Override
	public long echo(long test) {
		System.out.println("-- ECHO "+test+" --");
		return test;
	}

	public void dontroute(){ ; }
	public void route(){ ; }
	
	public long routeEcho(long echo){ return echo(echo); }
}
