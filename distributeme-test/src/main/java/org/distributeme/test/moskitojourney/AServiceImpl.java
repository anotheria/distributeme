package org.distributeme.test.moskitojourney;

import org.distributeme.core.ServiceLocator;

public class AServiceImpl implements AService{
	
	private BService bService = ServiceLocator.getRemote(BService.class);

	@Override
	public String aMethod(String param) throws AServiceException {
		try{
			return "a of ("+bService.bMethod(param)+ ')';
		}catch(BServiceException e){
			throw new AServiceException("BService failed:", e);
		}
	}

}
