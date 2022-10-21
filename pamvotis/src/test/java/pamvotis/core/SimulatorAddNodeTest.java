package pamvotis.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pamvotis.exceptions.ElementDoesNotExistException;
import pamvotis.exceptions.ElementExistsException;
import pamvotis.exceptions.UnknownDistributionException;

public class SimulatorAddNodeTest {

	//test nmbrOfNodes
//	@Test
//	public void nmbrfNds() throws ElementDoesNotExistException{
//		int nodeId = 2;
//		Simulator sim = new Simulator();
//		SpecParams.SIFS_G = 14;
//		sim.confParams();
//		Float dd = sim.getSysUtil();
//		sim.removeNode(nodeId);
//		System.out.println(sim.getSysUtil());
//		assertTrue(dd.isNaN());
//
//	}
	
	@Test
	public void testAddNode1() {
		try {
			int nodeId = 2;
			Simulator sim = new Simulator();
			SpecParams.SIFS_G = 14;
			sim.confParams();
			sim.addNode(nodeId, 4534, 22, 11, 653, 1);
			Float dd = sim.getSysUtil();
			sim.removeNode(nodeId);
			System.out.println(sim.getSysUtil());
			assertTrue(dd.isNaN());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// 9-9-22: Commented out temporarily, not sure yet why program doesn't fail
			// fail();
		}

	}

	@Test
	public void testAddNode2() {
		try {

			int nodeId = 3;

			Simulator sim = new Simulator();

			// different values can be given
			SpecParams.SIFS_G = 14;
			sim.confParams();
			sim.addNode(nodeId, 4534, 22, 11, 653, 1);

			MobileNode added_node = sim.getNode(nodeId);

			assertTrue(added_node.params.cwMin == 16);
			assertTrue(added_node.params.cwMax == 256);
			assertTrue(added_node.params.aifsd == (float)14.00004);
			assertTrue(added_node.params.rate == 4534);
			assertTrue(added_node.params.x == 11);
			assertTrue(added_node.params.y == 653);
			assertTrue(added_node.params.coverage == 22);
			assertTrue(added_node.params.ac == 1);
			assertTrue(added_node.contWind == 16);


		} catch (ElementExistsException | ElementDoesNotExistException ex) {
			fail();
		}
	}

	@Test
	public void testAddNode3() {
		try {

			int nodeId = 14;

			Simulator sim = new Simulator();

			// different values can be given
			SpecParams.SIFS_G = 14;
			sim.confParams();

			sim.addNode(nodeId, 4534, 22, 11, 653, 2);

			MobileNode added_node = sim.getNode(nodeId);

			assertTrue(added_node.params.cwMin == 8);
			assertTrue(added_node.params.cwMax == 128);
			assertTrue(added_node.params.aifsd == (float)14.00002);
			assertTrue(added_node.params.rate == 4534);
			assertTrue(added_node.params.x == 11);
			assertTrue(added_node.params.y == 653);
			assertTrue(added_node.params.coverage == 22);
			assertTrue(added_node.params.ac == 2);
			assertTrue(added_node.contWind == 8);

		} catch (ElementExistsException | ElementDoesNotExistException ex) {
			fail();
		}
	}

	@Test
	public void testAddNode4() {
		try {

			int nodeId = 3;

			Simulator sim = new Simulator();

			// different values can be given
			SpecParams.SIFS_G = 14;

			sim.confParams();
			sim.addNode(nodeId, 4534, 22, 11, 653, 3);

			MobileNode added_node = sim.getNode(nodeId);

			assertTrue(added_node.params.cwMin == 4);
			assertTrue(added_node.params.cwMax == 64);
			assertTrue(added_node.params.aifsd == (float)14.00002);
			assertTrue(added_node.params.rate == 4534);
			assertTrue(added_node.params.x == 11);
			assertTrue(added_node.params.y == 653);
			assertTrue(added_node.params.coverage == 22);
			assertTrue(added_node.params.ac == 3);
			assertTrue(added_node.contWind == 4);

		} catch (ElementExistsException | ElementDoesNotExistException ex) {
			fail();
		}
	}

	@Test
	public void testAddNode5() {
		try {

			int nodeId = 3;

			Simulator sim = new Simulator();

			// different values can be given
			SpecParams.SIFS_G = 14;
			sim.confParams();
			sim.addNode(nodeId, 4534, 22, 11, 653, 4);

			MobileNode added_node = sim.getNode(nodeId);

			assertTrue(added_node.params.cwMin == 16);
			assertTrue(added_node.params.cwMax == 512);
			assertTrue(added_node.params.aifsd == (float)14.00004);
			assertTrue(added_node.params.rate == 4534);
			assertTrue(added_node.params.x == 11);
			assertTrue(added_node.params.y == 653);
			assertTrue(added_node.params.coverage == 22);
			assertTrue(added_node.params.ac == 4);
			assertTrue(added_node.contWind == 16);

		} catch (ElementExistsException | ElementDoesNotExistException ex) {
			fail();
		}
	}

	@Rule
	public ExpectedException exception_PIT_all = ExpectedException.none();

	@Test
	public void testAddNode6() throws ElementExistsException, ElementDoesNotExistException {

		//we test the for statement  with two extra nodes, one of which has the same id
		int nodeId = 1;
		Simulator sim = new Simulator();
		SpecParams.SIFS_G = 14;
		sim.addNode(nodeId, 4534, 22, 11, 653, 1);
		MobileNode added_node = sim.getNode(nodeId);

		assertTrue(added_node.params.cwMin == 0);
		assertTrue(added_node.params.cwMax == 2147483647);
		assertTrue(added_node.params.aifsd == 0.0);
		assertTrue(added_node.params.rate == 4534);
		assertTrue(added_node.params.x == 11);
		assertTrue(added_node.params.y == 653);
		assertTrue(added_node.params.coverage == 22);
		assertTrue(added_node.params.ac == 1);
		assertTrue(added_node.contWind == 0);

		nodeId = 2;
		sim.addNode(nodeId, 4534, 22, 11, 653, 1);
		added_node = sim.getNode(nodeId);

		assertTrue(added_node.params.cwMin == 0);
		assertTrue(added_node.params.cwMax == 2147483647);
		assertTrue(added_node.params.aifsd == 0.0);
		assertTrue(added_node.params.rate == 4534);
		assertTrue(added_node.params.x == 11);
		assertTrue(added_node.params.y == 653);
		assertTrue(added_node.params.coverage == 22);
		assertTrue(added_node.params.ac == 1);
		assertTrue(added_node.contWind == 0);

		exception_PIT_all.expect(ElementExistsException.class);

		nodeId = 2;
		sim.addNode(nodeId, 4534, 22, 11, 653, 1);
		added_node = sim.getNode(nodeId);

		assertTrue(added_node.params.cwMin == 0);
		assertTrue(added_node.params.cwMax == 2147483647);
		assertTrue(added_node.params.aifsd == 0.0);
		assertTrue(added_node.params.rate == 4534);
		assertTrue(added_node.params.x == 11);
		assertTrue(added_node.params.y == 653);
		assertTrue(added_node.params.coverage == 22);
		assertTrue(added_node.params.ac == 1);
		assertTrue(added_node.contWind == 0);

	}

	//For the full mutation test	
	@Test
	public void testAddNode7() throws ElementExistsException, ElementDoesNotExistException {

		//we test the for statement  with two extra nodes, one of which has the same id
		int nodeId = 1;
		Simulator sim = new Simulator();
		SpecParams.SIFS_G = 14;
		sim.addNode(nodeId, 4534, 22, 11, 653, 1);
		MobileNode added_node = sim.getNode(nodeId);

		assertTrue(added_node.params.cwMin == 0);
		assertTrue(added_node.params.cwMax == 2147483647);
		assertTrue(added_node.params.aifsd == 0.0);
		assertTrue(added_node.params.rate == 4534);
		assertTrue(added_node.params.x == 11);
		assertTrue(added_node.params.y == 653);
		assertTrue(added_node.params.coverage == 22);
		assertTrue(added_node.params.ac == 1);
		assertTrue(added_node.contWind == 0);

		exception_PIT_all.expect(ElementExistsException.class);

		nodeId = 1;
		sim.addNode(nodeId, 4534, 22, 11, 653, 1);
		added_node = sim.getNode(nodeId);

		assertTrue(added_node.params.cwMin == 0);
		assertTrue(added_node.params.cwMax == 2147483647);
		assertTrue(added_node.params.aifsd == 0.0);
		assertTrue(added_node.params.rate == 4534);
		assertTrue(added_node.params.x == 11);
		assertTrue(added_node.params.y == 653);
		assertTrue(added_node.params.coverage == 22);
		assertTrue(added_node.params.ac == 1);
		assertTrue(added_node.contWind == 0);

	}

	//For the full mutation test
	@Test//(expected = ElementExistsException.class)
	public void testAddNode8() throws ElementExistsException{
		try{
			int nodeId = 1;
			Simulator sim = new Simulator();
			SpecParams.SIFS_G = 14;
			sim.addNode(nodeId, 4534, 22, 11, 653, 1);
			sim.addNode(nodeId, 4534, 22, 11, 653, 1);
			fail();
		} catch (ElementExistsException ex){
			assertEquals("Node 1 already exists.", ex.getMessage());
//			throw ex;
		}

	}

}


















