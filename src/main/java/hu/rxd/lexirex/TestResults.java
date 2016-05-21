package hu.rxd.lexirex;

import java.util.List;

public class TestResults {

	public static class Suite {
		public static class Case {
			public String className;
			public String name;
			public double duration;
		}

		public List<Case> cases;
	};

	public List<Suite> suites;

}
