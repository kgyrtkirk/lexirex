package hu.rxd.lexirex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class RegexRangeTest {

	@Test
	public void testUsage1() {
		Pattern p = RegexRangeBuilder
				.fromExclusive("alma")
				.toInclusive("alsovent")
				.toPattern();

		assertFalse(p.matcher("alm").matches());
		assertTrue(p.matcher("almafa").matches());
		assertTrue(p.matcher("almafak").matches());
		assertTrue(p.matcher("alpa").matches());
		assertTrue(p.matcher("alsovd").matches());
		assertTrue(p.matcher("also.").matches());
		assertFalse(p.matcher("alsoventa").matches());
	}

	@Test
	public void testII() {
		Pattern p = RegexRangeBuilder
				.fromInclusive("alma")
				.toInclusive("alsovent")
				.toPattern();
		assertTrue(p.matcher("alma").matches());
		assertTrue(p.matcher("alsovent").matches());
	}

	@Test
	public void testEE() {
		Pattern p = RegexRangeBuilder
				.fromExclusive("alma")
				.toExclusive("alsovent")
				.toPattern();
		assertFalse(p.matcher("alma").matches());
		assertFalse(p.matcher("alsovent").matches());
	}

	@Test
	public void testC1() {
		RegexRange regexRange = RegexRangeBuilder
				.fromExclusive("")
				.toExclusive("aa");
		System.out.println(regexRange.toRegex());
		Pattern p = regexRange
				.toPattern();
		assertTrue(p.matcher("a").matches());
	}

	@Test(expected=Exception.class)
	public void testInvalidRange() {
		RegexRange regexRange = RegexRangeBuilder
				.fromExclusive("aa")
				.toExclusive("aa");
	}

	@Test(expected=Exception.class)
	public void testInvalidRange2() {
		RegexRange regexRange = RegexRangeBuilder
				.fromInclusive("xx")
				.toInclusive("aa");
	}
}
