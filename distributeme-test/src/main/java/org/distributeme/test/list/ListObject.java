package org.distributeme.test.list;

import java.io.Serializable;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 00:57
 */
public class ListObject implements Serializable {

	private String key;

	public ListObject(int i) {
		key = "Object "+i;
	}

	public String getKey(){
		return key;
	}

	@Override public String toString(){
		return "Object with key "+key;
	}
}
