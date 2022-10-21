package xcom.thoughtworks.xstream.io.xml;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DecodeNameTest {
	
	@Test
	public void testDecodeName1() {

		String dollarReplacement = "_-";
		String escapeCharReplacement = "^-";

		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
				dollarReplacement, escapeCharReplacement);

		String decoded = ncoder.decodeNode("A^-Node");

		String expected = "A_Node";

		assertTrue(decoded.equals(expected));

		// for if(s != null) coverage
		decoded = ncoder.decodeNode("A^-Node");
		assertTrue(decoded.equals(expected));
	}

//	@Test
//	public void testDecodeNode_noReplacements() {
//
//		String dollarReplacement = "_-";
//		String escapeCharReplacement = "^_";
//
//		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
//				dollarReplacement, escapeCharReplacement);
//
//		String decoded = ncoder.decodeNode("Yo");
//		String expected = "Yo";
//
//		assertTrue(decoded.equals(expected));
//	}

	@Test
	public void testDecodeName2() {

		String dollarReplacement = "_-";
		String escapeCharReplacement = "^_";

		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
				dollarReplacement, escapeCharReplacement);

		String decoded = ncoder.decodeNode("_a");

		String expected = "_a";

		assertTrue(decoded.equals(expected));
	}
	
	@Test
	public void testDecodeName3() {

		String dollarReplacement = "_-";
		String escapeCharReplacement = "^_";

		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
				dollarReplacement, escapeCharReplacement);

		String decoded = ncoder.decodeNode("^a");
		String expected = "^a";

		assertTrue(decoded.equals(expected));
	}
	
	@Test
	public void testDecodeName4() {

		String dollarReplacement = "_-";
		String escapeCharReplacement = "^_";

		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
				dollarReplacement, escapeCharReplacement);

		String decoded = ncoder.decodeNode("");
		String expected = "";

		assertTrue(decoded.equals(expected));
	}

	@Test
	public void testDecodeName5() {

		String dollarReplacement = "_-";
		String escapeCharReplacement = "^_";

		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
				dollarReplacement, escapeCharReplacement);

		String decoded = ncoder.decodeNode("_-Hello^_There!");
		String expected = "$Hello_There!";

		assertTrue(decoded.equals(expected));
	}

	@Test
	public void testDecodeName6() {

		String dollarReplacement = "_-";
		String escapeCharReplacement = "^_";

		XmlFriendlyNameCoder ncoder = new XmlFriendlyNameCoder(
				dollarReplacement, escapeCharReplacement);

		String decoded = ncoder.decodeNode("^_HowAreYou");
		String expected = "_HowAreYou";

		assertTrue(decoded.equals(expected));
	}
	
}
