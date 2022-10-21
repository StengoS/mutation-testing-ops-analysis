package xorg.apache.commons.lang;

import static org.junit.Assert.*;

// package xorg.apache.commons.lang;

import java.util.Map;

import org.junit.Test;

import xorg.apache.commons.lang.ArrayUtils;
import junit.framework.TestCase;

public class ArrayUtils_toMapTest  {

	// check if catches null
	@Test
	public void testToMap1() {
		assertEquals(null, ArrayUtils.toMap(null));
	}

	// check if the pairs work (extra method for all mutation coverage)
	@Test
	public void testToMap2() {
		Map map = ArrayUtils
				.toMap(new String[][] { { "RED", "#FF0000" }, { "GREEN", "#00FF00" }, { "BLUE", "#0000FF" } });
		assertEquals("#FF0000", map.get("RED"));
	}

	// check for mapEntry
	@Test
	public void testToMap3() {
		Map map = ArrayUtils
				.toMap(new String[][] { { "RED", "#FF0000" }, { "GREEN", "#00FF00" }, { "BLUE", "#0000FF" } });

		map = ArrayUtils.toMap(new Object[] { new Map.Entry() {
			public Object getKey() {
				return "RED";
			}

			public Object getValue() {
				return "#FF0000";
			}

			@Override
			public Object setValue(Object value) {
				throw new UnsupportedOperationException();

			}

		} });
		assertEquals("#FF0000", map.get("RED"));

	}

	// not all pairs testing <exception pair.length<2>
	@Test
	public void testToMap4() {
		try {
			ArrayUtils.toMap(new String[][] { { "dog", "1" }, { "cat" } });
			fail("exception expected");
		} catch (IllegalArgumentException ex) {
			assertTrue(ex.toString().contains("java.lang.IllegalArgumentException: Array element 1,"));
			assertTrue(ex.toString().contains("java.lang.String"));
			assertTrue(ex.toString().contains(", has a length less than 2"));
		}

	}

	// check if catches not mapEntry or Array Exception
	@Test
	public void testToMap5() {

		try {
			ArrayUtils.toMap(new String[] { "epalitheusi", "meeting" });
			fail("exception expected");
		} catch (IllegalArgumentException ex) {
			assertEquals("Array element 0, 'epalitheusi', is neither of type Map.Entry nor an Array", ex.getMessage());
			// ********* EXTRA assertion for ALL MUTATION COVERAGE
			// **************** //
		}

	}

}
