package org.distributeme.registry.metaregistry;

import org.distributeme.core.ServiceDescriptor;
/**
 * A command which should be sent to all members of the cluster.
 * @author lrosenberg
 *
 */
public class ClusterSyncCommand {
	/**
	 * Operation of the command.
	 * @author lrosenberg
	 *
	 */
	public static enum Operation{
		/**
		 * A service has been binded.
		 */
		BIND, 
		/**
		 * A service has been unbinded.
		 */
		UNBIND;
	};
	/**
	 * The service descriptor of the target service.
	 */
	private ServiceDescriptor serviceDescriptor;
	/**
	 * The operation of the command.
	 */
	private Operation operation;
	
	/**
	 * Creates a new ClusterSyncCommand.
	 * @param anOperation
	 * @param aDescriptor
	 */
	public ClusterSyncCommand(Operation anOperation, ServiceDescriptor aDescriptor){
		operation = anOperation;
		serviceDescriptor = aDescriptor;
	}
	
	@Override public String toString(){
		return operation.name()+ ' ' +serviceDescriptor;
	}
	
	public Operation getOperation(){
		return operation;
	}
	
	public ServiceDescriptor getDescriptor(){
		return serviceDescriptor;
	}
	/**
	 * Factory method for bind command.
	 * @param descriptor
	 * @return
	 */
	public static final ClusterSyncCommand bind(ServiceDescriptor descriptor){
		return new ClusterSyncCommand(Operation.BIND, descriptor);
	}

	/**
	 * Factory method for unbind command.
	 * @param descriptor
	 * @return
	 */
	public static final ClusterSyncCommand unbind(ServiceDescriptor descriptor){
		return new ClusterSyncCommand(Operation.UNBIND, descriptor);
	}
}
