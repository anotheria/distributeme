package org.distributeme.registry.ui.action;

import org.distributeme.core.ServiceDescriptor;

/**
 * Removes event channel consumer from all channels.
 * @author lrosenberg
 *
 */
public class RemoveESConsumerAction extends RemoveESParticipantAction{

	@Override
	protected void dothejob(String id) {
		getRegistry().notifyConsumerUnavailable(ServiceDescriptor.fromRegistrationString(id));
	}


}
