package xorg.apache.commons.lang;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;

public class ArrayUtils_lastIndexOfTest  {

	@Test
	public void testLastIndexOf1() {

		assertEquals(-1, ArrayUtils.lastIndexOf(null, null, 2));

    }

	@Test
	public void testLastIndexOf2(){

		Object[] array = new Object[] { "a", "b", "c" };
		assertEquals(0, ArrayUtils.lastIndexOf(array, "a", 0));

	}

	@Test
	public void testLastIndexOf3(){

		Object[] array = new Object[] { "a", "b", "c" };
		assertEquals(1, ArrayUtils.lastIndexOf(array, "b", 3));

	}

	@Test
	public void testLastIndexOf4(){

		Object[] array = new Object[] {  null,"a", "b","c" };
		assertEquals(0, ArrayUtils.lastIndexOf(array, null,5));

	}

	@Test
	public void testLastIndexOf5(){

		Object[] array = new Object[] { "a", "b", "c" };
		assertEquals(-1, ArrayUtils.lastIndexOf(array, "c", 1));

	}

	@Test
	public void testLastIndexOf6(){

		Object[] array = new Object[] { "a", "b", "c" };
		assertEquals(-1, ArrayUtils.lastIndexOf(array, null, 2));

	}

	@Test
	public void testLastIndexOf7(){

		Object[] array = new Object[] { "a", "b", "c" };
		assertEquals(-1, ArrayUtils.lastIndexOf(array, null, -2));

	}

	@Test
	public void testLastIndexOf8(){

		Object[] array = new Object[] {null, null};
		assertEquals(1, ArrayUtils.lastIndexOf(array, null, 1));

	}

}
