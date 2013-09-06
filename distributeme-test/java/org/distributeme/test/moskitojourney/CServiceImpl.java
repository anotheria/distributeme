package org.distributeme.test.moskitojourney;

public class CServiceImpl implements CService{

	@Override
	public String cMethod(String string) throws CServiceException {
		return "c of "+string;
	}

}
