package org.apache.commons.math.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class Vector3D_orthogonalTest {

	@Test
	public void testOrthogonal1() {
		try {
			Vector3D v3 = new Vector3D(-1.7, 1.4, 0.2);
			Vector3D resultVector = v3.orthogonal();
			assertEquals(resultVector.getX(), 0.6357072528610996, 0.0);
			assertEquals(resultVector.getY(), 0.7719302356170497, 0.0);
			assertEquals(resultVector.getZ(), 0.0, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void testOrthogonal2() {
		try {
			Vector3D v7 = new Vector3D(0.6, 0.8, 0);
			Vector3D resultVector = v7.orthogonal();
			assertEquals(resultVector.getX(), 0.0, 0.0);
			assertEquals(resultVector.getY(), 0.0, 0.0);
			assertEquals(resultVector.getZ(), -1.0, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void testOrthogonal3() {
		try {
			Vector3D v8 = new Vector3D(0.8, 0.6, 0);
			Vector3D resultVector = v8.orthogonal();
			assertEquals(resultVector.getX(), -0.0, 0.0);
			assertEquals(resultVector.getY(), 0.0, 0.0);
			assertEquals(resultVector.getZ(), 1.0, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void testOrthogonal4() {
		try {
			Vector3D v10 = new Vector3D(1.2, 0.8, 0.8);
			Vector3D resultVector = v10.orthogonal();
			assertEquals(resultVector.getX(), -0.5547001962252291, 0.0);
			assertEquals(resultVector.getY(), 0.0, 0.0);
			assertEquals(resultVector.getZ(), 0.8320502943378436, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void testOrthogonal5() {
		try {
			new Vector3D(0, 0, 0).orthogonal();
			fail("an exception should have been thrown");
		} catch (ArithmeticException ae) {
			assertEquals(ae.getMessage(), "null norm");
			// expected behavior
		} catch (Exception e) {
			fail("wrong exception caught: " + e.getMessage());
		}

	}

	@Test
	public void testOrthogonal6() {
		try {
			Vector3D vector1 = new Vector3D(0.3, -0.3, 0.15);
			Vector3D resultVector = vector1.orthogonal();
			assertEquals(resultVector.getX(), -0.7071067811865476, 0.0);
			assertEquals(resultVector.getY(), -0.7071067811865476, 0.0);
			assertEquals(resultVector.getZ(), 0.0, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void testOrthogonal7() {
		try {
			Vector3D v5 = new Vector3D(-1.2, 0, 1.6);
			Vector3D resultVector = v5.orthogonal();
			assertEquals(resultVector.getX(), 0.0, 0.0);
			assertEquals(resultVector.getY(), 1.0, 0.0);
			assertEquals(resultVector.getZ(), -0.0, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

	@Test
	public void testOrthogonal8() {
		try {
			Vector3D v8 = new Vector3D(0.8, -0.6, 0);
			Vector3D resultVector = v8.orthogonal();
			assertEquals(resultVector.getX(), -0.0, 0.0);
			assertEquals(resultVector.getY(), 0.0, 0.0);
			assertEquals(resultVector.getZ(), 1.0, 0.0);
		} catch (Exception e) {
			fail("exception thrown");
		}
	}

}
