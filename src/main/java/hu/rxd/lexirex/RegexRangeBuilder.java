package hu.rxd.lexirex;

public class RegexRangeBuilder {
	static class From {

		private String st;
		private boolean stInclusive;

		private From(String st, boolean inclusive) {
			this.st = st;
			this.stInclusive = inclusive;
		}

		public RegexRange toInclusive(String to) {
			return new RegexRange(stInclusive, st, true, to);
		}

		public RegexRange toExclusive(String to) {
			return new RegexRange(stInclusive, st, false, to);
		}
	}

	public static From fromExclusive(String from) {
		return new From(from, false);
	}

	public static From fromInclusive(String from) {
		return new From(from, true);
	}

}
