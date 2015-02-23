package org.distributeme.test.combined.client;

import org.distributeme.core.ServiceLocator;
import org.distributeme.test.combined.BusinessService;

public class BusinessClient {
	public static void main(String[] args) throws Exception{
		BusinessService service = ServiceLocator.getRemote(BusinessService.class);
		System.out.println("CALLING REMOTE "+service);
		service.businessMethod();
		System.out.println("READY");
	}

}
