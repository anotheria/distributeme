package org.distributeme.test.moskitojourney;

import net.anotheria.moskito.webui.embedded.StartMoSKitoInspectBackendForRemote;
import org.distributeme.core.ServiceLocator;

public class AServiceImpl implements AService{

	static{
		try {
			StartMoSKitoInspectBackendForRemote.startMoSKitoInspectBackend();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private BService bService = ServiceLocator.getRemote(BService.class);

	@Override
	public String aMethod(String param) throws AServiceException {
		try{
			return "a of ("+bService.bMethod(param)+")";
		}catch(BServiceException e){
			throw new AServiceException("BService failed:", e);
		}
	}

}
