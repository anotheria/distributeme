package org.distributeme.test.regression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.anotheria.anoprise.metafactory.Service;

import org.distributeme.annotation.DistributeMe;

/**
 * This service contains method declarations that have previously led to problems.
 * @author another
 *
 */

@DistributeMe
public interface ParentService extends Service{
	long operation(int param1, int param2);
	
	long operation(long param1, long param2);
	
	List<Integer> genericOperation(List<String> x, Set<Object> y, Map<Long,Boolean> z);

	List<Integer> genericOperation(List<String> x, List<Set<String>> y, Map<Long,Boolean> z);

	Integer[] arrayOperation(String[] x, Object[] y, Long[][][] z);
	
	Map<Object, List<String>> getVisits(List<Object> userIds);
	
	byte[] highWeightEcho(byte[] data);


}
