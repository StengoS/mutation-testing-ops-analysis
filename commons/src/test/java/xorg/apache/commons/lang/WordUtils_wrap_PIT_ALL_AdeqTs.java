package xorg.apache.commons.lang;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import xorg.apache.commons.lang.WordUtils;

public class WordUtils_wrap_PIT_ALL_AdeqTs {

	@Test
	public void testPIT_ALL1() {
		String expectedNull = WordUtils.wrap(null, 0, "", true);
		assertTrue(expectedNull == null);
	}

	@Test
	public void testPIT_ALL2() {
		String expectedNotNull = WordUtils.wrap("definitelyNotNull", 0);
		assertTrue(expectedNotNull != null);
	}

	@Test
	public void testPIT_ALL3() {
		String test = WordUtils.wrap("", 0, null, true);
		assertTrue(test.equals(""));
		test = WordUtils.wrap(" test ", 0, "d", true);
		assertTrue(test.equals("tdedsdtd"));
	}

	// @Test
	// public void test4() {
	// String test = WordUtils.wrap("test,test", 7, ",", true);
	// assertTrue(test.equals("test,te,st"));
	// }
	//
	// @Test
	// public void test5() {
	// String test = WordUtils.wrap("tes t,tes t", 1, ",", false);
	// assertTrue(test.equals("tes,t,tes,t"));
	// }

	@Test
	public void testPIT_ALL6() {
		String test = WordUtils.wrap("This cv svnasvnav vsdvsdavsdv vosdakvkdsa]v ovsdavo", 10, null, false);
		// 9/22/22 - Added \r before all \n to do carriage return on Windows
		assertTrue(test.equals("This cv\r\nsvnasvnav\r\nvsdvsdavsdv\r\nvosdakvkdsa]v\r\novsdavo"));
	}

	@Test
	public void testPIT_ALL7() {
		String test = WordUtils.wrap("asd areallyhugewordthatneedstobebiggerthanthenumberontheright", 5);
		// 9/22/22 - Added \r before \n to do carriage return on Windows
		assertTrue(test.equals("asd\r\nareallyhugewordthatneedstobebiggerthanthenumberontheright"));
	}

	@Test
	public void testPIT_ALL8() {

		String test = WordUtils.wrap(" as a", 5, null, false);
		assertTrue(test.equals(" as a"));
	}

	// @Test
	// public void test9() {
	// String test = WordUtils.wrap("ab c", 2, null, true);
	// assertTrue(test.equals("ab\nc"));
	// }
}
