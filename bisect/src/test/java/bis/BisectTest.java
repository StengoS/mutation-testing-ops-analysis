package bis;

import static org.junit.Assert.*;

import org.junit.Test;

public class BisectTest extends Thread {

	@Test
	public void testBisect1() {
		Bisect obtained = null;
		obtained = new Bisect();

		double arg1 = 0;
		obtained.setEpsilon(arg1);
		double arg2 = 0;
		double result = obtained.sqrt(arg2);

		assertEquals(String.valueOf(result), "0.0");
		assertTrue(obtained.mEpsilon == 0.0);
		assertTrue(obtained.mResult == 0.0);
	}

	@Test
	public void testBisect2() {
		Bisect obtained = null;
		obtained = new Bisect();

		double arg1 = 1;
		obtained.setEpsilon(arg1);
		double arg2 = 16;
		double result = obtained.sqrt(arg2);

		assertEquals(String.valueOf(result), "4.046875");
		assertTrue(obtained.mEpsilon == 1.0);
		assertTrue(obtained.mResult == 4.046875);
	}

	// @Test
	// public void testBisect3() {
	// Bisect obtained = null;
	// obtained = new Bisect();
	
	// double arg1 = 0.1;
	// obtained.setEpsilon(arg1);
	// double arg2 = 36;
	// int result = (int) obtained.sqrt(arg2);
	// int expect_result = 6;
	
	// assertTrue(result == expect_result);
	// }

	@Test
	public void testBisect4() {
		Bisect obtained = null;
		obtained = new Bisect();

		double arg1 = 0.1;
		obtained.setEpsilon(arg1);
		double arg2 = 36;
		double result = obtained.sqrt(arg2);

		assertEquals(String.valueOf(result), "6.00732421875");
		assertTrue(obtained.mEpsilon == 0.1);
		assertTrue(obtained.mResult == 6.00732421875);
	}

	// test 5
	Bisect obtained1 = new Bisect();

	@Test
	public void testBisect5() throws InterruptedException {

		double arg1 = 1;
		obtained1.setEpsilon(arg1);

		BisectTest thread1 = new BisectTest();
		thread1.start();

		long aa = System.currentTimeMillis();
		long time = 0;

		while (thread1.isAlive()) {
			time = System.currentTimeMillis() - aa;
			if (time > 1000) {
				thread1.stop();
				break;
			}
		}

		assertTrue(time > 1000);
	}

	public void run() {

		obtained1.sqrt(-1);
	}

}
