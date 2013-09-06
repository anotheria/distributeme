package org.distributeme.test.combined.client;

import org.distributeme.core.ServiceLocator;
import org.distributeme.test.combined.AdminService;

public class AdminClient {
	public static void main(String[] args) throws Exception{
		AdminService service = ServiceLocator.getRemote(AdminService.class);
		System.out.println("CALLING REMOTE "+service);
		service.adminMethod();
		System.out.println("READY");
	}
}
