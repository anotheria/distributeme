package org.distributeme.registry.ui.action;

import org.distributeme.core.ServiceDescriptor;

/**
 * Removes a supplier from the channel.
 * @author lrosenberg
 *
 */
public class RemoveESSupplierAction extends RemoveESParticipantAction{

	@Override
	protected void dothejob(String id) {
		getRegistry().notifySupplierUnavailable(ServiceDescriptor.fromRegistrationString(id));
	}


}
