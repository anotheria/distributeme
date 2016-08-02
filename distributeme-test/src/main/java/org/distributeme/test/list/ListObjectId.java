package org.distributeme.test.list;

import java.io.Serializable;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 22.09.15 23:41
 */
public class ListObjectId implements Serializable {

	private String primary;

	public ListObjectId(int i) {
		primary = String.valueOf(i);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ListObjectId)) return false;

		ListObjectId that = (ListObjectId) o;

		return !(primary != null ? !primary.equals(that.primary) : that.primary != null);

	}

	@Override
	public int hashCode() {
		return primary != null ? primary.hashCode() : 0;
	}

	@Override
	public String toString(){
		return primary;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}
}
