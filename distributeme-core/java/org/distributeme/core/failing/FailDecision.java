package org.distributeme.core.failing;

/**
 * Possible decisions that a failing strategy can make.
 * @author lrosenberg
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
	 * @param aReaction
	 */
	public FailDecision(Reaction aReaction){
		reaction = aReaction;
	}
	
	/**
	 * Creates a new fail decision with given reaction and target service.
	 * @param aReaction
	 * @param aTargetService
	 */
	public FailDecision(Reaction aReaction, String aTargetService){
		reaction = aReaction;
		targetService = aTargetService;
	}

	public Reaction getReaction(){
		return reaction;
	}
	
	public String getTargetService() {
		return targetService;
	}

	public void setTargetService(String targetService) {
		this.targetService = targetService;
	}

	/**
	 * Factory method for fail reaction.
	 * @return
	 */
	public static final FailDecision fail(){
		return new FailDecision(Reaction.FAIL);
	}
	/**
	 * Factory method for retry reaction.
	 * @return
	 */
	public static final FailDecision retry(){
		return new FailDecision(Reaction.RETRY);
	}
	/**
	 * Factory method for retryOnce reaction.
	 * @return
	 */
	public static final FailDecision retryOnce(){
		return new FailDecision(Reaction.RETRYONCE);
	}
}
