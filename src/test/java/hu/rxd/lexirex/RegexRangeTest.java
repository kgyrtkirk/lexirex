package hu.rxd.lexirex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class RegexRangeTest {

	@Test
	public void testUsage0() {
		RegexRange rr = new RegexRange("alma", "alsovent");
		Pattern p = Pattern.compile(rr.getRegex());

		assertFalse(p.matcher("alm").matches());
		assertTrue(p.matcher("almafa").matches());
		assertTrue(p.matcher("almafak").matches());
		assertTrue(p.matcher("alpa").matches());
		assertTrue(p.matcher("alsovd").matches());
		assertTrue(p.matcher("also.").matches());
		assertFalse(p.matcher("alsoventa").matches());

	}
	

}
