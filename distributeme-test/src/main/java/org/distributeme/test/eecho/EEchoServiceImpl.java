package org.distributeme.test.eecho;

import org.distributeme.test.echo.EchoServiceImpl;

public class EEchoServiceImpl extends EchoServiceImpl implements EEchoService{
	public long eecho(long param){
		return echo(param);
	}
}
