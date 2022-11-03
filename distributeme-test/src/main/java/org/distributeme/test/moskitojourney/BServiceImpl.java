package org.distributeme.test.moskitojourney;

import org.distributeme.core.ServiceLocator;
import org.distributeme.core.exception.DistributemeRuntimeException;

public class BServiceImpl implements BService{

	private CService cService = ServiceLocator.getRemote(CService.class);

	@Override
	public String bMethod(String param) throws BServiceException {
		try{
			return "b of ("+cService.cMethod(param)+")";
		}catch(CServiceException e){
			return "b of (c failed)";
		}catch(Exception e){
			return "b of (c failed)";
		}
	}

}
