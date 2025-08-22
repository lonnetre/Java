package vsue.raft;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of the raft protocol
 */
public class VSRaftProtocol implements VSRaftProtocolService {

	/**
	 * Roles of a replica as defined by raft
	 */
	enum VSRaftRole {
		LEADER,
		FOLLOWER,
		CANDIDATE
	}

	// Order of magnitude between timeouts, see raft paper section 5.6
	private static final int ELECTION_TIMEOUT = 3000;
	private static final int HEARTBEAT_TIMEOUT = 300;

	private final InetSocketAddress[] addresses;
	private final int myId;

	// Fields defined in Figure 2 of the raft paper
	// persistent
	private int currentTerm;
	private int votedFor;
	private VSRaftLog log;

	// volatile
	private long commitIndex;
	private long lastApplied;

	// volatile leader
	private long[] nextIndex;
	private long[] matchIndex;

	private final HashMap<Integer, VSRaftProtocolService> stubCache = new HashMap<>();
	private VSRaftRole role;
	private VSCounterServer server;
	private long lastHeardFromLeader;
	private ScheduledExecutorService heartbeatScheduler;

	/**
	 * Create a new instance of the raft protocol
	 *
	 * @param replicaId id of the current replica. addresses[replicaId] is the
	 *                  address of the registry for use by the current replica
	 * @param addresses addresses of the registry of every raft protocol instance
	 */
	public VSRaftProtocol(int replicaId, InetSocketAddress[] addresses) {
		myId = replicaId;
		this.addresses = addresses;

		/*
		 * TODO: initialize protocol state
		 */

		// persistent
		this.currentTerm = 0; // Figure 2
		this.votedFor = -1; // null is -1
		this.log = new VSRaftLog();

		// volatile
		this.commitIndex = 0; // Figure 2
		this.lastApplied = 0; // Figure 2

		// volatile leader
		this.nextIndex = new long[addresses.length];
		this.matchIndex = new long[addresses.length];
		for (int i = 0; i < addresses.length; i++) {
			nextIndex[i] = 1; // Figure 2: initialized to leader last log index + 1 (by initialisation leader last log index is 0)
			matchIndex[i] = 0; // Figure 2
		}
	}


	/**
	 * Initialize the raft protocol instance. Exports the protocol instance and
	 * makes it accessible via a registry instance for this protocol. Runs a
	 * connection test afterwards and initiates periodic protocol tasks.
	 *
	 * @param application Counter server application. Provides status(), applyRequest(),
	 *                    createSnapshot() and applySnapshot() methods
	 * @throws RemoteException Failed to export protocol or setup registry
	 */
	public void init(VSCounterServer application) throws RemoteException {
		/*
		 * TODO: Exercise Section 5.1: Export protocol via a registry, use testConnection()
		 *  Use the address from addresses[myId] for the registry
		 * TODO: Exercise Section 5.2: Start protocol thread
		 */

		/*
		Wird beim Start eines Replikats aufgerufen.
		Exportiert this als RMI-Objekt.
		Startet eine eigene RMI-Registry (lokal).
		Registriert sich dort unter dem Namen "VSRaft".
		Testet ggf. Verbindung zu anderen Replikaten.
		 */

		System.out.println("[INIT] - Replica " + myId + " started, currentTerm=" + currentTerm);

		server = application;

		// Exercise Section 5.1
		VSRaftProtocolService stub = (VSRaftProtocolService) UnicastRemoteObject.exportObject(this, 0);
		Registry registry = LocateRegistry.createRegistry(addresses[myId].getPort());
		registry.rebind("VSRaft", stub);
		testConnection();

		// When servers start up, they begin as followers.
		role = VSRaftRole.FOLLOWER;
		lastHeardFromLeader = System.currentTimeMillis();
		server.status(role, -1);

		/*
		- in einer Schleife läuft,
		- regelmäßig überprüft, ob ein Leader da ist,
		- ggf. requestVote() aufruft (wenn Timeout),
		- später auch Heartbeats versendet (ab 5.3).
		 */

		// Exercise Section 5.2
		AtomicInteger timeout = new AtomicInteger(getRandomElectionTimeout()); // 150–300ms as in paper

		new Thread(() -> {
			while (true) {
				if (role != VSRaftRole.LEADER) {
					long now = System.currentTimeMillis();

					// election timeout
					if (now - lastHeardFromLeader > timeout.get()) {
						/*
						If a follower receives no communication over a period of time called the election timeout,
						then it assumes there is no viable leader and begins an election to choose a new leader.
						 */
						System.out.println("[ELECTION] - Replica " + myId + " starts election for term " + currentTerm + " with timeout " + timeout.get() + " ms");
						timeout.set(getRandomElectionTimeout());
						startElection();
					}
				}

				// Das ist Raft-Thread – nur dieser Thread bearbeitet Raft-Zustand => applyCommittedEntries hier
				applyCommittedEntries();
			}
		}).start();
	}

	private synchronized void applyCommittedEntries() {
		// ALL SERVERS: If commitIndex > lastApplied: increment lastApplied, apply log[lastApplied] to state machine (§5.3)
		while (commitIndex > lastApplied) {
			lastApplied++;
			VSRaftLogEntry entry = log.getEntry(lastApplied);
			server.applyRequest(entry);
		}
	}

	private synchronized int getRandomElectionTimeout() {
		// 150 + (0-150) => 150-300
		return (ELECTION_TIMEOUT/2) + (int)(Math.random() * ((double) ELECTION_TIMEOUT /2));
		// We recommend using a conservative election timeout such as 150–300ms;
		// such timeouts are unlikely to cause unnecessary leader changes and will still provide good availability
	}

	private synchronized void startElection() {
		// TODO: Exercise 5.2
		/*
		Exercise 5.2:
			Sobald ein Replikat nach Ablauf des Anführerwahl-Timeouts zum Candidate wird,
			besteht sein Ziel darin, Stimmen von einer Replikatsmehrheit zu sammeln.
			Hierzu stimmt das Replikat zunächst für sich selbst und ruft anschließend ... requestVote() auf.
		 */

		// Candidates (§5.2):
		// (1)
		currentTerm++;
		votedFor = myId;
		lastHeardFromLeader = System.currentTimeMillis(); // reset election timer

		// P. 5.2
		role = VSRaftRole.CANDIDATE;
		server.status(role, -1);

		System.out.println("[CANDIDATE] - Replica " + myId + " started, currentTerm=" + currentTerm);

		// wie in candidateUpToDate Methode
		long lastLogIndex = log.getLatestIndex();
		int lastLogTerm = (lastLogIndex >= 0) ? log.getEntry(lastLogIndex).term : 0;

		AtomicInteger votes = new AtomicInteger(1); // not 0, because candidate votes for himself
		int majority = (addresses.length / 2) + 1;

		// (2) Send RequestVote RPCs to all other servers
		for (int replicaId = 0; replicaId < addresses.length; replicaId++) {
			if (replicaId == myId) continue; // Nicht an sich selbst senden

			// P. 5.2: It then votes for itself and issues RequestVote RPCs in *parallel* to each of the other servers in the cluster.
			final int target = replicaId;
			try {
				VSRaftProtocolService stub = getStub(target);
				if (stub == null) return;

				// -> requestVote aufrufen (Raft §5.2 / Aufgabe 5.2)
				VSRaftRPCResult result = stub.requestVote(
						currentTerm, myId, lastLogIndex, lastLogTerm
				);

				handleVoteResponse(result, majority, votes);
			} catch (RemoteException e) {
				// TODO: Exception, ich glaube nicht???
				discardStub(target);
				return;
			}
		}
	}


	private synchronized void handleVoteResponse(VSRaftRPCResult result, int majority, AtomicInteger votes) {

		// ALL SERVERS: If RPC request or response contains term T > currentTerm: set currentTerm = T, convert to follower (§5.1)
		if (result.term > currentTerm) {
			currentTerm = result.term;
			votedFor = -1;
			role = VSRaftRole.FOLLOWER;
			server.status(role, -1);
			return;
		}

		// Wenn Stimme erhalten -> hochzählen
		// Nur gültig, wenn man noch Candidate ist und der Rückgabe-Term nicht veraltet ist
		if (result.success && role == VSRaftRole.CANDIDATE && result.term == currentTerm) {
			int voteTotal = votes.incrementAndGet();

			System.out.println("[RESPONSE] - Received vote response: success=" + result.success + ", term=" + result.term + ", myTerm=" + currentTerm);
			System.out.println("[RESPONSE] - Total votes so far: " + voteTotal);

			// Candidates (§5.2): If votes received from the majority of servers: become leader
			if (voteTotal >= majority) {
				role = VSRaftRole.LEADER;
				server.status(role, myId);

				System.out.println("[LEADER] - Replica " + myId + " becomes LEADER for term " + currentTerm);

				// STATE: Initialize nextIndex[i] and matchIndex[i]
				// When a leader first comes to power, it initializes all nextIndex values to the index just after the last one in its log (11 in Figure 7).
				long next = log.getLatestIndex() + 1;
				for (int i = 0; i < addresses.length; i++) {
					nextIndex[i] = next;
					matchIndex[i] = 0;
				}

				// TODO: Exercise 5.3
				// It then sends heartbeat messages to all of the other servers to establish its authority and prevent new elections.
				if (heartbeatScheduler == null || heartbeatScheduler.isShutdown()) {
					startHeartbeatLoop();
				}
			}
		}
	}

	private void startHeartbeatLoop() {
		heartbeatScheduler = Executors.newSingleThreadScheduledExecutor();
		heartbeatScheduler.scheduleAtFixedRate(() -> {
			// Übungsaufgabe: Liegen neue Log-Einträge vor, so repliziert der Protokoll-Thread des Leader diese per appendEntries()-Fernaufruf auf seine Follower.
			if (role == VSRaftRole.LEADER) {
				sendHeartbeats();
			}
		}, 0, HEARTBEAT_TIMEOUT, TimeUnit.MILLISECONDS); // every 300ms
	}

	private synchronized void sendHeartbeats() {
		// TODO: Exercise 5.3
		/*
		Leaders send periodic heartbeats (AppendEntries RPCs that carry no log entries)
		to all followers to maintain their authority.
		 */

		// VL: Timeouts werden zurückgesetzt, wenn ein Follower entweder einen requestVote akzeptiert
		// oder ein Heartbeat (appendEntries) vom Anführer erhält.

		// wie in startElection
		for (int replicaId = 0; replicaId < addresses.length; replicaId++) {
			if (replicaId == myId) continue; // Nicht an sich selbst senden

			final int target = replicaId;
			try {
				VSRaftProtocolService stub = getStub(target);
				if (stub == null) return;

					/*
					If last log index ≥ nextIndex for a follower: send AppendEntries RPC with log entries starting at nextIndex
					• If successful: update nextIndex and matchIndex for follower (§5.3)
					• If AppendEntries fails because of log inconsistency: decrement nextIndex and retry (§5.3)
					 */

				boolean success = false;
				while(!success) {
					// prevLogIndex = FOLLOWER nextIndex
					long prevLogIndex = nextIndex[target] - 1; // The leader maintains a nextIndex for each follower which is the index of the next log entry the leader will send to that follower => nextIndex-1
					int prevLogTerm = prevLogIndex >= 0 ? log.getEntry(prevLogIndex).term : 0;

					// f.e. lastLogIndex = 5, prevLogIndex = 2 =>
					// entries = log.getEntry(3), log.getEntry(4), log.getEntry(5)
					long lastLogIndex = log.getLatestIndex();
					VSRaftLogEntry[] entries = new VSRaftLogEntry[(int) (lastLogIndex - prevLogIndex)];
					for (int i = 0; i < entries.length; i++) {
						entries[i] = log.getEntry(prevLogIndex + 1 + i);
					}

					/// previous task:
					/// send initial empty AppendEntries RPCs (heartbeat) to each server;
					/// repeat during idle periods to prevent election timeouts (§5.2)

					VSRaftRPCResult result = stub.appendEntries(
							currentTerm, myId, prevLogIndex, prevLogTerm, entries, commitIndex
					);

					stepDownIfNecessary(result.term);

					if (result.success) {
						// Update nextIndex and matchIndex
						nextIndex[target] = prevLogIndex + entries.length + 1;  // nächste Log-Eintrag, den der Follower noch nicht hat
						matchIndex[target] = nextIndex[target] - 1; // höchste Index, den der Follower gespeichert hat
						success = true;
						// commit for LEADER
						updateCommitIndex();
					} else {
						// Inconsistent log -> decrement nextIndex and retry
						if (nextIndex[target] > 1) {
							nextIndex[target]--;
						} else {
							break; // can't decrement below 1
						}
					}
				}

			} catch (RemoteException e) {
				discardStub(target);
				System.err.println("[Error] - Heartbeat to replica " + target + " failed: " + e.getMessage());
			}
		}
	}

	private synchronized void updateCommitIndex() {
		// Versuche, commitIndex auf einen höheren Index zu setzen,
		// wenn dieser auf einer Mehrheit von Servern repliziert ist
		long latestIndex = log.getLatestIndex();

		// LEADERS: If there exists an N such that N > commitIndex,
		// a majority of matchIndex[i] ≥ N, and log[N].term == currentTerm:
		// set commitIndex = N (§5.3, §5.4).
		for (long N = latestIndex; N > commitIndex; N--) {
			int count = 1; // Leader selbst hat diesen Eintrag
			for (int i = 0; i < addresses.length; i++) {
				if (i == myId) continue;
				if (matchIndex[i] >= N) {
					count++;
				}
			}
			int majority = (addresses.length / 2) + 1;
			if (count >= majority) {
				// Eintrag N ist auf Mehrheit repliziert UND aus currentTerm?
				if (log.getEntry(N).term == currentTerm) {
					commitIndex = N;
					notifyAll(); // wecke wartende Threads (z.B. orderRequest)
					break;
				}
			}
		}
	}

	private synchronized void stepDownIfNecessary(int remoteTerm) {
		// If AppendEntries RPC received from new leader: convert to follower
		if (remoteTerm > currentTerm) {
			if (heartbeatScheduler != null) {
				heartbeatScheduler.shutdownNow(); // better than shutDown, because it attempts to stop all actively executing tasks,
				heartbeatScheduler = null;
			}
			currentTerm = remoteTerm;
			votedFor = -1;
			role = VSRaftRole.FOLLOWER;
			server.status(role, -1);
			lastHeardFromLeader = System.currentTimeMillis();
		}
	}

	/**
	 * Returns a VSRaftProtocolService stub for the specified replica.
	 *
	 * @param replicaId Id of replica to connect to
	 * @return VSRaftProtocolService stub
	 * @throws RemoteException Failed to retrieve the stub
	 */
	private VSRaftProtocolService getStub(int replicaId) throws RemoteException {
		/*
		 * TODO: Exercise Section 5.1: Implement stub retrieval
		 */

		/*
		Holt den RMI-Stub eines anderen Replikats über dessen Adresse.
		Cacht den Stub, um wiederholte Netzwerk-Operationen zu vermeiden.
		Holt den Stub nur neu, wenn keiner existiert oder bei Fehler.
		 */
		if (stubCache.containsKey(replicaId)) return stubCache.get(replicaId);

		InetSocketAddress stubAdress = addresses[replicaId];
		Registry stubRegistry = LocateRegistry.getRegistry(stubAdress.getHostString(), stubAdress.getPort());
		VSRaftProtocolService stub;
        try {
            stub = (VSRaftProtocolService) stubRegistry.lookup("VSRaft");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
		stubCache.put(replicaId, stub);
		return stub;
	}

	/**
	 * Discard a cached VSRaftProtocolService stub for the specified replica.
	 *
	 * @param replicaId Id of replica whose stub should be dropped
	 */
	private void discardStub(int replicaId) {
		/*
		 * TODO: Exercise Section 5.1: Forget broken stub for replicaId
		 */

		/*
		Löscht einen defekten Eintrag aus dem Stub-Cache.
		Wird z.B. benutzt, wenn getStub() fehlschlägt oder RMI abbricht.
		 */

		stubCache.remove(replicaId);
	}

	/**
	 * Basic test whether communication with the other replicas is possible.
	 */
	private void testConnection() {
		for (int i = 0; i < addresses.length; i++) {
			if (i == myId) {
				continue;
			}

			while (true) {
				VSRaftProtocolService stub = null;
				try {
					stub = getStub(i);
				} catch (RemoteException e) {
					System.out.println("Retrying getStub for replica " + i + " after exception: " + e);
				}
				if (stub != null) {
					try {
						// issue request for old term which must always be rejected
						stub.requestVote(-1, myId, -1, -1);
						break;
					} catch (RemoteException e) {
						System.out.println("Retrying connection to replica " + i + " after exception: " + e);
						discardStub(i);
					} catch (UnsupportedOperationException e) {
						// UnsupportedOperationException is thrown by the rpc stub implementation
						break;
					}
				}
				try {
					//noinspection BusyWait
					Thread.sleep(500);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
			System.out.println("Connection to replica " + i + " successful");
		}
		System.out.println("Connection test completed");
	}


	@Override
	// see VSRaftProtocolService.requestVote for the documentation
	public synchronized VSRaftRPCResult requestVote(int term, int candidateId, long lastLogIndex, int lastLogTerm) {
		/*
		 * TODO: Exercise Section 5.2: Implement leader election
		 */

		// (1) bereits in einem höheren Term ist als vom Candidate angefragt
		if (term < currentTerm) {
			System.out.println("[VOTE] - Rejected vote from " + candidateId + " for old term " + term + " < my term " + currentTerm);
			return new VSRaftRPCResult(term, false);
		}

		currentTerm = term;
		votedFor = -1; // -1 is null, 0 isn't
		role = VSRaftRole.FOLLOWER;
		server.status(role, -1);

		boolean upToDate = candidateUpToDate(lastLogIndex, lastLogTerm);

		System.out.println("[VOTE] - Replica " + myId + " received vote request from " + candidateId + " for term " + term);

		// (2) If votedFor is null or candidateId, and candidate’s log is at least as up-to-date as
		// receiver’s log, grant vote(§5.2,§5.4)
		if ((votedFor == -1 || votedFor == candidateId) && upToDate) {
			votedFor = candidateId; // if votedFor = -1
			server.status(role, votedFor);
			System.out.println("[VOTE] - My currentTerm=" + currentTerm + ", votedFor=" + votedFor + ", candidateUpToDate=" + upToDate);

			lastHeardFromLeader = System.currentTimeMillis();

			return new VSRaftRPCResult(term,true);
		}

		// (2) zwar denselben Term aufweist, allerdings schon für ein anderes Replikat gestimmt hat
		return new VSRaftRPCResult(term,false);
	}

	private boolean candidateUpToDate(long lastLogIndex, int lastLogTerm) {
		VSRaftLogEntry latestEntry = log.getLatestEntry();
		long currentLogIndex = log.getLatestIndex();
		int currentLogTerm = (latestEntry != null) ? latestEntry.term : 0;

		if (lastLogTerm > currentLogTerm) return true;
		if (lastLogTerm == currentLogTerm && lastLogIndex >= currentLogIndex) return true;
		return false;
	}


	@Override
	// see VSRaftProtocolService.appendEntries for the documentation
	public synchronized VSRaftRPCResult appendEntries(int term, int leaderId, long prevLogIndex, int prevLogTerm,
	                                                  VSRaftLogEntry[] entries, long leaderCommit) {
		/*
		 * TODO: Exercise Section 5.3: Implement log replication
		 */

//		System.out.print("[" + LocalTime.now().truncatedTo(ChronoUnit.MILLIS) + " ");
//		System.out.println("HEARTBEAT] - AppendEntries received at " + myId + " from leader " + leaderId + " (term=" + term + ")");
//		System.out.println("[HEARTBEAT] - Accepted, prevLogIndex=" + prevLogIndex + ", prevLogTerm=" + prevLogTerm);

		// (1) Reply false if term < currentTerm (§5.1)
		// Kandidat erkennt anderen Leader => false
		if (term < currentTerm) {
			return new VSRaftRPCResult(term, false);
		}

		lastHeardFromLeader = System.currentTimeMillis();

        // Step down if term ≥ currentTerm – only valid from AppendEntries (Raft §5.2)
		stepDownIfAppendEntries(term, leaderId);

		// (2) Reply false if log doesn’t contain an entry at prevLogIndex whose term matches prevLogTerm (§5.3)
		if (prevLogIndex >= 0) {
			VSRaftLogEntry prevEntry = log.getEntry(prevLogIndex);
			if (prevEntry == null || prevEntry.term != prevLogTerm) {
				System.out.println("[HEARTBEAT] - Rejected! Log mismatch at index " + prevLogIndex);
				return new VSRaftRPCResult(currentTerm, false);
			}
		}

		// (3) If an existing entry conflicts with a new one (same index but different terms), delete the existing entry and all that follow it (§5.3)
		for (int i = 0; i < entries.length; i++) {
			long index = prevLogIndex + 1 + i;

			VSRaftLogEntry localEntry = log.getEntry(index);
			if (localEntry == null || localEntry.term != entries[i].term) {
				// (4) Append any new entries not already in the log
				VSRaftLogEntry[] entriesToKeep = new VSRaftLogEntry[entries.length - i];
				System.arraycopy(entries, i, entriesToKeep, 0, entriesToKeep.length);
				log.storeEntries(entriesToKeep);
				break;
			}
		}

		// (5) If leaderCommit > commitIndex, set commitIndex = min(leaderCommit, index of last new entry)
		// commit for FOLLOWER
		long lastNewIndex = prevLogIndex + entries.length;
		if (leaderCommit > commitIndex) {
			commitIndex = Math.min(leaderCommit, lastNewIndex);
		}

		return new VSRaftRPCResult(currentTerm, true);
//		throw new UnsupportedOperationException("installSnapshot: Not yet implemented");
	}

	private void stepDownIfAppendEntries(int remoteTerm, int leaderId) {
		if (remoteTerm > currentTerm || (remoteTerm == currentTerm && role != VSRaftRole.FOLLOWER)) {
			System.out.println("[STEPDOWN] - Replica " + myId + " steps down to FOLLOWER. New term=" + remoteTerm + ", from leader=" + leaderId);
			if (heartbeatScheduler != null) {
				heartbeatScheduler.shutdownNow();
				heartbeatScheduler = null;
			}
			currentTerm = remoteTerm;
			role = VSRaftRole.FOLLOWER;
			votedFor = -1;
			server.status(role, leaderId);
			lastHeardFromLeader = System.currentTimeMillis();
		}
	}

	/**
	 * Appends a request to the log for ordering. If the current replica is not
	 * the leader at the moment, the request is rejected. Called by the
	 * VSCounterServer.
	 *
	 * @param request Request to append to the log
	 * @return True when the current replica is the leader, false otherwise
	 */
	public synchronized boolean orderRequest(Serializable request) {
		/*
		 * TODO: Exercise Section 5.3: Implement log replication
		 */

		// Übungsaufgabe: (1) Nicht-Leader-Replikate müssen solche Aufrufe durch Rückgabe von false zurückweisen ohne weitere Aktionen durchzuführen.
		// nur Leader darf mit dem Client sprechen
		if (role != VSRaftRole.LEADER) {
			return false; // Nur der Leader darf neue Anfragen akzeptieren
		}

		// Übungsaufgabe: (2) Neuen Logeintrag erzeugen
		long newIndex = log.getLatestIndex() + 1;
		VSRaftLogEntry newEntry = new VSRaftLogEntry(newIndex, currentTerm, request);

		// Übungsaufgabe: (3) In das eigene Log einfügen
		log.addEntry(newEntry);

		// Übungsaufgabe: (4) Anstoßen der Replikation durch Aufwecken seines Protokoll-Threads:
		// Da Protokoll-Thread sowieso regelmäßig Heartbeats sendet, genügt es, darauf zu vertrauen, dass `sendHeartbeats()` die neue Anfrage mitschickt.
		// Alternativ: Man könnte explizit die Heartbeat-Schleife wecken, wenn man wait/notify nutzt (optional).

		// LEADERS: If command received from client: append entry to local log, respond after entry applied to state machine (§5.3)
		// Wir warten, bis der Log-Eintrag erfolgreich repliziert (committed) und auf dem Leader angewendet wurde.
		// Laut Raft (§5.3) darf der Leader dem Client erst antworten, wenn der Eintrag in einer Mehrheit der Replikate gespeichert und in den Status übernommen wurde.
		// Der Leader überprüft das regelmäßig in applyCommittedEntries() und weckt diesen Thread mit notifyAll().

		while (commitIndex < newIndex) {
			try {
				wait(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return false;
			}
		}

		return true;
//		throw new UnsupportedOperationException("orderRequest: Not yet implemented");
	}


	@Override
	// see VSRaftProtocolService.installSnapshot for the documentation
	public synchronized int installSnapshot(int term, int leaderId, long lastIncludedIndex,
	                                        int lastIncludedTerm, Serializable data) {
		/*
		 * TODO: Exercise Section 5.4: Implement snapshotting
		 */
		return 0;
//		throw new UnsupportedOperationException("installSnapshot: Not yet implemented");
	}
}
