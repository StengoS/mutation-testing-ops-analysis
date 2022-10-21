package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class gcdMutationAdeqTs_PITAll {

	@Test
	public void test1PITAll() {
		int expected = 5;
		assertEquals(expected, MathUtils.gcd(-5, 0));
	}

	@Test
	public void test4PITAll() {
		int expected = 2;
		assertEquals(expected, MathUtils.gcd((int) Math.pow(2, 30), 6));
	}

	@Test
	public void test5PITAll() {
		int expected = 2;
		assertEquals(expected, MathUtils.gcd(6, (int) Math.pow(2, 30)));
	}

	@Test
	public void test6PITAll() {
		int expected = 1;
		assertEquals(expected, MathUtils.gcd((int) Math.pow(2, 33), 6));
	}

	// edw
	@Test
	public void test8PITAll() {
		int expected = -2147483646;
		assertEquals(expected, MathUtils.gcd((int) Math.pow(-2, 33), -2));
	}

	@Test
	public void test9PITAll() {
		int expected = 2;
		assertEquals(expected, MathUtils.gcd(-2, -6));
	}

	@Test
	public void test11PITAll() {
		int expected = 5;
		assertEquals(expected, MathUtils.gcd(-1091314270, -531525255));
	}
}
