package org.distributeme.registry.ui.action;

/**
 * Base action for all clustering relevant actions.
 * @author lrosenberg
 *
 */
public abstract class BaseClusterAction extends BaseAction{
	
	public BaseClusterAction(){
    }
	
	@Override
	protected String getMenuSection() {
		return "cluster";
	}

	@Override
	protected String getTitle() {
		return "Cluster";
	}

}
