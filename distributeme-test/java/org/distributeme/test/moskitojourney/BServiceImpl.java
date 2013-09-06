package org.distributeme.test.moskitojourney;

import org.distributeme.core.ServiceLocator;

public class BServiceImpl implements BService{

	private CService cService = ServiceLocator.getRemote(CService.class);

	@Override
	public String bMethod(String param) throws BServiceException {
		try{
			return "b of ("+cService.cMethod(param)+")";
		}catch(CServiceException e){
			throw new BServiceException("CService failed:", e);
		}
	}

}
