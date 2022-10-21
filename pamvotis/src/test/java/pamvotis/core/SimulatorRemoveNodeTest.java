package pamvotis.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pamvotis.exceptions.ElementDoesNotExistException;
import pamvotis.exceptions.ElementExistsException;
import pamvotis.exceptions.UnknownDistributionException;

public class SimulatorRemoveNodeTest {

	// test nmbrOfNodes
	@Test
	public void testRemoveNode1() throws ElementDoesNotExistException {
		int nodeId = 2;
		Simulator sim = new Simulator();
		SpecParams.SIFS_G = 14;
		sim.confParams();
		Float dd = sim.getSysUtil();
		sim.removeNode(nodeId);
		sim.getSysUtil();
		assertTrue(dd.isNaN());

	}

//	@Rule
//	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testRemoveNode2() {
		try {

			int nodeId = 14;
			Simulator sim = new Simulator();
			sim.addNode(nodeId, 444, 33, 1227, 117, 1);

			// additional assertions can be added
//			exception.expect(ElementDoesNotExistException.class);
			assertTrue(sim.removeNode(15));
			fail();
		} catch (Exception ex) {
			assertTrue(ex.getMessage().contains("Node 15 does not exist."));
		}
	}

	@Test
	public void testRemoveNode3() {
		try {

			int nodeId = 14;
			Simulator sim = new Simulator();
			sim.addNode(nodeId, 444, 33, 1227, 117, 1);

			// additional assertions can be added
			assertTrue(sim.removeNode(nodeId));
			// assertTrue(sim.getNmbrOfNodes()==0);

			sim.addNode(nodeId, 444, 33, 1227, 117, 1);

			assertTrue(sim.removeNode(nodeId));

		} catch (ElementExistsException | ElementDoesNotExistException ex) {
			fail();
		}
	}

//	// For the full mutation test
//	@Test//(expected = ElementDoesNotExistException.class)
//	public void exceptionTest_2() throws ElementDoesNotExistException {
//		try {
//			int nodeId = 1;
//			Simulator sim = new Simulator();
//			SpecParams.SIFS_G = 14;
//			sim.removeNode(nodeId);
//			fail();
//		} catch (ElementDoesNotExistException ex) {
//			assertEquals("Node 1 does not exist.", ex.getMessage());
////			throw ex;
//		}
//
//	}

}
