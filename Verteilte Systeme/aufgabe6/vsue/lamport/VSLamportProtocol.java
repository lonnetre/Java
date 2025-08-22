package exercise6_2.vsue.lamport;

import java.io.IOException;
import java.io.Serializable;
import java.util.PriorityQueue;

public class VSLamportProtocol {

	private final VSCluster cluster;
	private final int processID, size;

	// Lamport clock & own‐request state
	private int clock = 0;
	private int requestTimestamp = -1; // Welchen Timestamp hat der Request den Ich gerade am Laufen habe?
	private boolean requesting = false;  // Habe Ich momentan einen request am laufen? Ja oder Nein
	private boolean granted = false; // Wurde mir access für diesen Request gegranted? Ja oder Nein?

	// Highest timestamp seen from each process
	private final int[] lastTS;

	// Queue of outstanding requests (including our own)
	private final PriorityQueue<Request> queue = new PriorityQueue<>();

	// Hook to release the local semaphore
	private VSLamportLock lockObj;

	public VSLamportProtocol(VSCluster cluster) {
		this.cluster = cluster;
		this.processID = cluster.getProcessID();
		this.size = cluster.getSize();
		this.lastTS = new int[size];          // initialized to 0
	}

	// Called by VSLamportLock ctor
	void setLock(VSLamportLock lock) {
		this.lockObj = lock;
	}

	// No special init actions needed
	public void init() { }

	// Sequential event handler
	public synchronized void event(VSLamportEvent event) {
		switch (event.getType()) {

			// 1) Application called lock()
			case LOCK -> {
				requesting = true;
				granted = false;      // reset for new request
				clock++;                       // tick before sending
				requestTimestamp = clock;
				lastTS[processID] = clock;     // record our own
				queue.add(new Request(requestTimestamp, processID));
				try {
					cluster.multicast(new Message(Message.Type.REQUEST, requestTimestamp, processID));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 2) Application called unlock()
			case UNLOCK -> {
				if (requesting) {
					// remove our request from queue
					queue.remove(new Request(requestTimestamp, processID));
					requesting = false;
					clock++;                     // tick before sending
					lastTS[processID] = clock;  // update our timestamp
					try {
						cluster.multicast(new Message(Message.Type.RELEASE, clock, processID));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			// 3) A network Message arrived
			case MESSAGE -> {
				Message msg = (Message) event.getContent();
				// 3a) Lamport‐clock receive rule
				clock = Math.max(clock, msg.timestamp) + 1;
				// 3b) record sender’s timestamp
				lastTS[msg.processID] = msg.timestamp;

				switch (msg.type) {
					case REQUEST -> {
						if (msg.processID != processID) {
							// enqueue remote’s request
							queue.add(new Request(msg.timestamp, msg.processID));
							// reply with an ACK purely to propagate our clock
							try {
								cluster.unicast(new Message(Message.Type.ACK, clock, processID), msg.processID);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					case ACK -> {
						// nothing else to do—timestamp already recorded above
					}
					case RELEASE -> {
						// remove that process’s request
						if (msg.processID != processID) {
							queue.removeIf(r -> r.processID == msg.processID);
						}
					}
				}
			}
		}

		// 4) After ANY event, see if we can now grant the lock
		if (requesting && !granted && !queue.isEmpty() && queue.peek().processID == processID) {
			// Wenn Ich gerade etwas requeste -> True
			// Wenn request noch nicht gegranted wurde -> True
			// Wenn Que nicht empty ist
			// Erstes Element processID == processID

			// condition: everyone’s seen a timestamp ≥ our request
			boolean ok = true;
			for (int j = 0; j < size; j++) {
				if (lastTS[j] < requestTimestamp) {
					ok = false;
					break;
				}
			}
			if (ok) {
				lockObj.grant();
				granted = true;
			}
		}
	}

	// Simple serializable message wrapper
	static class Message implements Serializable {
		enum Type { REQUEST, ACK, RELEASE }
		final Type type;
		final int timestamp;
		final int processID;
		Message(Type t, int ts, int pid) {
			this.type = t;
			this.timestamp = ts;
			this.processID = pid;
		}
	}

	// Request‐queue entries, ordered by (timestamp, pid)
	private static class Request implements Comparable<Request> {
		final int timestamp, processID;
		Request(int ts, int pid) {
			this.timestamp = ts;
			this.processID = pid;
		}
		@Override
		public int compareTo(Request o) {
			int c = Integer.compare(timestamp, o.timestamp);
			return (c != 0) ? c : Integer.compare(processID, o.processID);
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Request)) return false;
			Request r = (Request) o;
			return r.timestamp == timestamp && r.processID == processID;
		}
		@Override
		public int hashCode() {
			return 31 * timestamp + processID;
		}
	}
}
