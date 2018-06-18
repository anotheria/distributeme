package org.distributeme.core.routing.blacklisting;

/**
 * Created by rboehling on 2/24/17.
 */
public enum BlacklistDecision {

	IS_BLACKLISTED {
		@Override
		public boolean statusChanged() {
			return false;
		}

		@Override
		public boolean isBlacklisted() {
			return true;
		}
	},
	GOT_BLACKLISTED {
		@Override
		public boolean statusChanged() {
			return true;
		}

		@Override
		public boolean isBlacklisted() {
			return true;
		}
	},
	NOT_BLACKLISTED {
		@Override
		public boolean statusChanged() {
			return false;
		}

		@Override
		public boolean isBlacklisted() {
			return false;
		}
	},
	UNBLACKLISTED {
		@Override
		public boolean statusChanged() {
			return true;
		}

		@Override
		public boolean isBlacklisted() {
			return false;
		}
	};

	abstract public boolean statusChanged();

	abstract public boolean isBlacklisted();
}
