package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathUtils_gcdTest {

	@Test
	public void testGcd1() {
		int expected = 5;
		assertEquals(expected, MathUtils.gcd(-5, 0));
	}

	@Test
	public void testGcd2() {
		int expected = 2;
		assertEquals(expected, MathUtils.gcd((int) Math.pow(2, 30), 6));
	}

	@Test
	public void testGcd3() {
		int expected = 2;
		assertEquals(expected, MathUtils.gcd(6, (int) Math.pow(2, 30)));
	}

	@Test
	public void testGcd4() {
		int expected = 1;
		assertEquals(expected, MathUtils.gcd((int) Math.pow(2, 33), 6));
	}

	// edw
	@Test
	public void testGcd5() {
		int expected = -2147483646;
		assertEquals(expected, MathUtils.gcd((int) Math.pow(-2, 33), -2));
	}

	@Test
	public void testGcd6() {
		int expected = 2;
		assertEquals(expected, MathUtils.gcd(-2, -6));
	}

	@Test
	public void testGcd7() {
		int expected = 5;
		assertEquals(expected, MathUtils.gcd(-1091314270, -531525255));
	}
}
