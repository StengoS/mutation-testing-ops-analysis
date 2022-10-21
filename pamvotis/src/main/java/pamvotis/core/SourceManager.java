// This is mutant program.
// Author : ysma

package pamvotis.core;

import java.util.Vector;

import pamvotis.exceptions.ElementDoesNotExistException;
import pamvotis.exceptions.ElementExistsException;
import pamvotis.sources.Source;

public class SourceManager {

	pamvotis.core.MobileNode _node = null;

	Vector<Source> _vActiveSources = new Vector<>();

	long _lastTimerValue = 0;

	public SourceManager(pamvotis.core.MobileNode node) {
		_node = node;
	}

	public Vector<Packet> pollPacketsFromSources() {
		Vector<Packet> newPackets = new Vector<>();
		Packet tmpPkt = null;
		for (int i = 0; i < _vActiveSources.size(); i++) {
			tmpPkt = takePacketFromSource(
					(pamvotis.sources.Source) _vActiveSources.elementAt(i), 1);
			if (tmpPkt != null) {
				newPackets.add(tmpPkt);
			}
		}
		return newPackets;
	}

	public void addSource(Source s) throws ElementExistsException {
		boolean sourceExists = false;
		for (int i = 0; i < _vActiveSources.size(); i++) {
			if (((Source) _vActiveSources.elementAt(i))._sourceId == s._sourceId) {
				sourceExists = true;
				break;
			}
		}
		if (sourceExists) {
			throw new ElementExistsException("Source " + s._sourceId
					+ " already exists.");
		} else {
			_vActiveSources.add(s);
		}
	}

	private pamvotis.core.Packet takePacketFromSource(
			pamvotis.sources.Source s, long elapsedTime) {
		s.interArTime -= elapsedTime;
		pamvotis.core.Packet p = null;
		if (s.interArTime <= 0) {
			if (s.packetToBeTransmittedID != -1) {
				p = new pamvotis.core.Packet();
				p.length = s.pktLength;
				p.generationTime = MobileNode.timer;
				p.id = s.packetToBeTransmittedID;
			}
			s.getNextPacket();
		}
		return p;
	}

	public boolean removeSource(int sourceId)
			throws ElementDoesNotExistException {
		int position = -1;
		for (int i = 0; i < _vActiveSources.size(); i++) {
			if (((Source) _vActiveSources.elementAt(i))._sourceId == sourceId) {
				position = i;
				break;
			}
		}
		if (position != -1) {
			_vActiveSources.removeElementAt(position);
			return true;
		} else {
			throw new ElementDoesNotExistException("Source " + sourceId
					+ " does not exist.");
		}
	}

	public pamvotis.sources.Source getSource(int id)
			throws pamvotis.exceptions.ElementDoesNotExistException {
		pamvotis.sources.Source s = null;
		for (int i = 0; i < _vActiveSources.size(); i++) {
			if (((pamvotis.sources.Source) _vActiveSources.elementAt(i))._sourceId == id) {
				s = (pamvotis.sources.Source) _vActiveSources.elementAt(i);
				break;
			}
		}
		if (s == null) {
			throw new pamvotis.exceptions.ElementDoesNotExistException("Source"
					+ id + "does not exist.");
		}
		return s;
	}

	public void clear() {
		_vActiveSources.clear();
	}

}
