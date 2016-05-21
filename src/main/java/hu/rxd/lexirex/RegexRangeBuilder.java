package hu.rxd.lexirex;

import hu.rxd.lexirex.RegexRangeBuilder.From;

public class RegexRangeBuilder {
	public static class From {

		private String st;
		private boolean stInclusive;

		private From(String st, boolean inclusive) {
			this.st = st;
			this.stInclusive = inclusive;
		}

		public RegexRange toInclusive(String string) {
			return new RegexRange(stInclusive, st, true, string);
		}
		public RegexRange toExclusive(String string) {
			return new RegexRange(stInclusive, st, false, string);
		}
	}

	public static From fromExclusive(String string) {
		return new From(string, false);
	}

	public static From fromInclusive(String string) {
		return new From(string,true);
	}

}
