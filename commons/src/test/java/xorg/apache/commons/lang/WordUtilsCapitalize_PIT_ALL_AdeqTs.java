package xorg.apache.commons.lang;

import xorg.apache.commons.lang.WordUtils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import junit.framework.TestCase;

//*****Minimum Mutation Coverage with All mutation Operators.*****//

public class WordUtilsCapitalize_PIT_ALL_AdeqTs {
	// *********Necessary Mutations for 58 coverage. (Same as before)*********//

	@Test
	public void testPIT_ALLAssertEqualsNull() {
		// check if null entry returns null
		assertEquals(null, WordUtils.capitalize(null, null));
	}

	@Test
	public void testPIT_ALLAssertEquals() {
		// check if null array checks name for whitespaces
		assertEquals("Hello Th.ere", WordUtils.capitalize("hello th.ere", null));
	}

	@Test
	public void testPIT_ALLAssertEqualsNoWhitespace() {
		// check if nothing or whitespace returns the same
		char[] delim = new char[0];
		assertEquals("", WordUtils.capitalize("", delim));
	}

	@Test
	public void testPIT_ALLAssertEqualsWithWhitespace() {
		char[] delim = new char[0];
		assertEquals("  ", WordUtils.capitalize("  ", delim));
	}

	@Test
	public void testPIT_ALLAssertEqualsSameAfterSymbol() {
		char[] delim = new char[] { '1', '_', ' ', '@', '#' };
		assertEquals("Hello There_How@Are#You", WordUtils.capitalize("Hello there_how@are#you", delim));
	}

	@Test
	public void testPIT_ALLAssertEqualsEmptyDelim() {
		char[] delim = new char[] {};
		assertEquals("teststringone", WordUtils.capitalize("teststringone", delim));
	}

	// public void testAssertEqualsSameAfterSymbol2() {
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("Hello There_How@Are#You", WordUtils.capitalize("Hello
	// There_How@Are#You", delim) );
	// }
	//
	// //when added it makes #mutations=56 from 55
	// public void testAssertEqualsSameCap(){
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("He_He_Ha_Ha@Ho 2341Bla",
	// WordUtils.capitalize("he_he_ha_ha@ho 2341bla", delim) );
	// }

	// **********Unnecessary assertions.***********//

	// if added still #mutations=56
	// public void testAssertEqualsSameCap2(){
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("He_He_Ha_Ha@Ho 234bla",
	// WordUtils.capitalize("he_he_ha_ha@ho 234bla", delim) );
	//
	// }

	// if added: still 57
	// public void testAssertEqualsFirstCapitalized(){
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("ME", WordUtils.capitalize("ME", delim) );
	// }
	// if added: still 57
	// public void testAssertEqualsAllCapitalized(){
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("ME", WordUtils.capitalize("mE", delim) );
	// }

	// public void testAssertEquals1end(){
	// //check if catches the 1 at the end
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("He_He_Ha_Ha@Ho 2341Bla",
	// WordUtils.capitalize("he_he_ha_ha@ho 2341bla", delim) );
	// }
	//
	// public void testAssertEqualsSameCapA(){
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// assertEquals("BLUBI_A 23", WordUtils.capitalize("BLUBI_a 23", delim) );
	// }

	// public void testAssertEqualsDoubleDelimiter(){
	// char[] delim = new char[] { '1','_',' ','@','#'};
	// //check if catches the double delimeter
	// assertEquals("BLUBI_@A 1X23", WordUtils.capitalize("BLUBI_@a 1x23",
	// delim) );
	// }

	// public void testAssertEqualsFullstop(){
	// //check if ignores whitespace,does the job only for fullstop
	// char[] delim = new char[] {'.'};
	// assertEquals("Ba by.Chris", WordUtils.capitalize("ba by.Chris", delim) );
	// }
	//

}
