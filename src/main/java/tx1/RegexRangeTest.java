package tx1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexRangeTest {

	static class DemoMatcher{

		private List<Pattern> patterns=new ArrayList<>();

		public DemoMatcher(List<String> exprs) {
			for (String expr : exprs) {
				patterns.add(Pattern.compile(expr));
			}
		}

		public boolean matches(String string) {
			for (Pattern pattern : patterns) {
				if(pattern.matcher(string).matches()){
					return true;
				}
			}
			return false;
		}
	};
	
	
	@Test
	public void test() {
//		Pattern.compile("xal[n-j]");
		
		RegexRange rr = new RegexRange("alma", "alsovent");

		System.out.println(rr.getExprs());
		DemoMatcher dm = new DemoMatcher(rr.getExprs());
		
		assertFalse(dm.matches("alm"));
		assertTrue(dm.matches("almafa"));
		assertTrue(dm.matches("almafak"));
		assertTrue(dm.matches("alpa"));
		assertTrue(dm.matches("alsovd"));
		assertTrue(dm.matches("also."));
		assertFalse(dm.matches("alsoventa"));
	}

}
