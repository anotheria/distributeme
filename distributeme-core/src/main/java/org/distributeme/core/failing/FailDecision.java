package org.distributeme.core.failing;

/**
 * Possible decisions that a failing strategy can make.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class FailDecision {
	/**
	 * Reaction on failure.
	 */
	public static enum Reaction{
	/**
	 * Simply fail.
	 */
	FAIL,
	/**
	 * Retry.
	 */
	RETRY,
	/**
	 * Retry but max once.
	 */
	RETRYONCE,
	}
	
	/**
	 * Reaction for this decision, what should the stub do as next.
	 */
	private Reaction reaction;
	/**
	 * The service to apply reaction to, this may be a backup instance for retry or retryonce.
	 */
	private String targetService;
	/**
	 * Creates a new fail decision with given reaction.
	 *
	 * @param aReaction a {@link org.distributeme.core.failing.FailDecision.Reaction} object.
	 */
	public FailDecision(Reaction aReaction){
		reaction = aReaction;
	}
	
	/**
	 * Creates a new fail decision with given reaction and target service.
	 *
	 * @param aReaction a {@link org.distributeme.core.failing.FailDecision.Reaction} object.
	 * @param aTargetService a {@link java.lang.String} object.
	 */
	public FailDecision(Reaction aReaction, String aTargetService){
		reaction = aReaction;
		targetService = aTargetService;
	}

	/**
	 * <p>Getter for the field <code>reaction</code>.</p>
	 *
	 * @return a {@link org.distributeme.core.failing.FailDecision.Reaction} object.
	 */
	public Reaction getReaction(){
		return reaction;
	}
	
	/**
	 * <p>Getter for the field <code>targetService</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetService() {
		return targetService;
	}

	/**
	 * <p>Setter for the field <code>targetService</code>.</p>
	 *
	 * @param targetService a {@link java.lang.String} object.
	 */
	public void setTargetService(String targetService) {
		this.targetService = targetService;
	}

	/**
	 * Factory method for fail reaction.
	 *
	 * @return a {@link org.distributeme.core.failing.FailDecision} object.
	 */
	public static final FailDecision fail(){
		return new FailDecision(Reaction.FAIL);
	}
	/**
	 * Factory method for retry reaction.
	 *
	 * @return a {@link org.distributeme.core.failing.FailDecision} object.
	 */
	public static final FailDecision retry(){
		return new FailDecision(Reaction.RETRY);
	}
	/**
	 * Factory method for retryOnce reaction.
	 *
	 * @return a {@link org.distributeme.core.failing.FailDecision} object.
	 */
	public static final FailDecision retryOnce(){
		return new FailDecision(Reaction.RETRYONCE);
	}
}
