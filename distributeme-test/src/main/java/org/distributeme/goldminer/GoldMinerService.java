package org.distributeme.goldminer;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

@DistributeMe(asynchSupport=true, asynchCallTimeout=2500)
public interface GoldMinerService extends Service{
	/**
	 * Searches a random location for gold. Can last up to 10 seconds. 
	 * Returns if anything was found.
	 * @return
	 */
	boolean searchForGold();
	/**
	 * Washes gold for a given duration. Returns the amount of washed clumps.
	 * @param duration
	 * @return
	 */
	int washGold(long duration);
}
