package org.distributeme.core.routing.blacklisting;

/**
 * Created by rboehling on 2/24/17.
 */
public enum BlacklistDecision {

	IS_BLACKLISTED {
		@Override
		boolean statusChanged() {
			return false;
		}

		@Override
		boolean isBlacklisted() {
			return true;
		}
	},
	GOT_BLACKLISTED {
		@Override
		boolean statusChanged() {
			return true;
		}

		@Override
		boolean isBlacklisted() {
			return true;
		}
	},
	NOT_BLACKLISTED {
		@Override
		boolean statusChanged() {
			return false;
		}

		@Override
		boolean isBlacklisted() {
			return false;
		}
	},
	UNBLACKLISTED {
		@Override
		boolean statusChanged() {
			return true;
		}

		@Override
		boolean isBlacklisted() {
			return false;
		}
	};

	abstract boolean statusChanged();

	abstract boolean isBlacklisted();
}
