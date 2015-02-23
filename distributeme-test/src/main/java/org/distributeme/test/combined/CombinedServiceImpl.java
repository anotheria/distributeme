package org.distributeme.test.combined;

public enum CombinedServiceImpl implements AdminService, BusinessService{
	INSTANCE;
	
	@Override
	public void businessMethod() throws BusinessServiceException {
		System.out.println("Business method called");
	}

	@Override
	public void adminMethod() throws AdminServiceException {
		System.out.println("admin method called");
	}
	
}
