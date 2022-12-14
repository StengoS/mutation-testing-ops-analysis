package pamvotis.sources;

import java.util.Vector;

import pamvotis.core.VirtualPacket;


/**
 * This abstract class represents a source that generates packets. Objects of this class should not created.
 * Create objects of the inherited class, that represent the type of source. This class is used by the simulation
 * engine to add packets to a node's queue. The abstract classes getnextPacket and synchronize should be implemented
 * from the inherited sources.
 * @author Dimitris El. Vassis
 */
public abstract class Source{

	/**
	 * The time slot value according to the physical layer. Used for synchronization
	 */
	public static float slot;
	/**
	 * The Id of the source
	 */
	public int _sourceId;
	/**
	 * Represents the current time in slots. used for synchronization
	 */
	public static long timer;
	/**
	 * The interarrival time of the packet to be generated.
	 */
	public int interArTime;
	/**
	 * The packet length of the packet to be generated.
	 */
	public int pktLength;
	/**
	 * The Id of the packet to be generated.
	 */
	public int packetToBeTransmittedID;
	/**
	 * A vector of virtual packets representing a session of packets. After generating a session of virtual packets,
	 * each one of them, when the time to be generated arrives, it is converted to a real packet and added to the source for transmission.
	 */
	public Vector<VirtualPacket> session = new Vector<VirtualPacket>();


	
	/**This abstract method is called by the source manager to get a packet generated by the packet source.
	*Implemented differently in each source.
	*/
	public abstract void getNextPacket();
	
	/**This method is called by the simulator in every slot, to make synchronization procedures with the sources.
	*Mainly needed for the video source, but can be used by every source as a help function that is called in
	*every time slot.
	*/
	public abstract void synchronize();

}
