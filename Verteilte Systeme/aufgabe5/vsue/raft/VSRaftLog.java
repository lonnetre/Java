package vsue.raft;

import java.util.ArrayList;

/**
 * Implementation of a log for the raft protocol. It provides methods to add and
 * retrieve log entries and offers support for garbage collecting the log. The
 * log always starts with a placeholder entry containing an initial index/term
 * or the index/term of the latest snapshot used for garbage collection. This
 * avoids special cases when determining the prevLogIndex / prevLogTerm.
 */
public class VSRaftLog {
	/**
	 * Log size limit. Processing more requests requires support for garbage collection.
	 */
	private static final int MAX_LOG_SIZE = 1000000;
	/**
	 * List of log entries
	 */
	private final ArrayList<VSRaftLogEntry> log = new ArrayList<>();
	/**
	 * Index of first log element. This is increased after garbage collecting
	 * requests before a certain log index.
	 */
	private long startIndex = 0;

	/**
	 * Creates a new log instance.
	 */
	public VSRaftLog() {
		// initialize log
		collectGarbage(0, -1);
	}

	/**
	 * Add entry to the log. Sanity checks that there are no gaps in the log.
	 *
	 * @param entry new log entry, must contain a request
	 * @throws IllegalArgumentException If the entry would cause a gap in the
	 *                                  log or does not contain a request
	 * @throws IllegalStateException    Log is too large and must be compacted
	 *                                  using the snapshot mechanism of raft
	 */
	public void addEntry(VSRaftLogEntry entry) {
		if (log.isEmpty()) {
			throw new InternalError("Log was not initialized properly");
		} else {
			if (entry.index != getLatestIndex() + 1) {
				throw new IllegalArgumentException("Index number gaps / duplications" +
						" are not allowed! Expected " + (getLatestIndex() + 1)
						+ " got " + entry.index);
			}
		}
		if (log.size() > MAX_LOG_SIZE) {
			throw new IllegalStateException("Log is too large and requires snapshotting");
		}
		if (entry.request == null) {
			throw new IllegalArgumentException("Stub entries without a request" +
					" must not be added to the log");
		}
		log.add(entry);
	}

	/**
	 * Retrieve log entry at <code>index</code>. Returns null if the index was
	 * already garbage collected or does not exist yet.
	 *
	 * @param index index of entry to retrieve
	 * @return the log entry or null if it does not exist in the log
	 */
	public VSRaftLogEntry getEntry(long index) {
		long offset = index - startIndex;
		if (offset >= log.size() || offset < 0) {
			return null;
		}
		return log.get((int) offset);
	}

	/**
	 * Retrieves an array of entries starting from <code>startIndex</code> until
	 * the latest log entry. <code>startIndex</code> is included in the array.
	 *
	 * @param startIndex index of first entry to retrieve. Must exist in the log.
	 * @return array of log entries for requested log range
	 */
	public VSRaftLogEntry[] getEntriesSince(long startIndex) {
		long latestIndex = getLatestIndex();
		VSRaftLogEntry[] entries = new VSRaftLogEntry[(int) (latestIndex - startIndex + 1)];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = getEntry(startIndex + i);
		}
		return entries;
	}

	/**
	 * Appends new and replaces existing log entries. New log entries are
	 * appended to the log. If log entries with an index already contained in
	 * the log are passed, then the new log entries replace already existing ones.
	 *
	 * @param entries Log entries to store
	 * @throws IllegalStateException    Some entry indexes were already garbage
	 *                                  collected
	 * @throws IllegalArgumentException Tried to add log entry without request
	 */
	public void storeEntries(VSRaftLogEntry[] entries) {
		for (VSRaftLogEntry entry : entries) {
			long offset = entry.index - startIndex;
			if (offset < 0) {
				throw new IllegalStateException("Trying to store entries for an" +
						" already garbage collected index");
			}
			if (entry.request == null) {
				throw new IllegalArgumentException("Stub entries must never be transferred");
			}
			if (offset < log.size()) {
				// skip if entries are identical
				if (log.get((int) offset).equals(entry)) {
					continue;
				}

				// truncate log otherwise
				for (int i = log.size() - 1; i >= offset; i--) {
					log.remove(i);
				}
			}

			log.add(entry);
		}
	}

	/**
	 * Returns the latest log entry.
	 *
	 * @return latest log entry
	 */
	public VSRaftLogEntry getLatestEntry() {
		return log.get(log.size() - 1);
	}

	/**
	 * Returns the index of the latest log entry
	 *
	 * @return latest log index
	 */
	public long getLatestIndex() {
		return getLatestEntry().index;
	}

	/**
	 * Returns the first index contained in the log. Increases after garbage
	 * collection. The first index belongs to the log start / snapshot placeholder
	 * entry.
	 *
	 * @return log start index
	 */
	public long getStartIndex() {
		return startIndex;
	}

	/**
	 * Remove all log entries before <code>lastSnapshotIndex</code> and set the
	 * log start accordingly. Drops the log if the new first entry does not match
	 * <code>lastSnapshotIndex</code> and <code>lastSnapshotTerm</code>. Creates
	 * a stub log entry with <code>lastSnapshotIndex</code> and
	 * <code>lastSnapshotTerm</code>, if the garbage collection resulted in an
	 * empty log.
	 *
	 * @param lastSnapshotIndex index of new log start entry
	 * @param lastSnapshotTerm  term of new log start entry
	 * @throws IllegalArgumentException The lastSnapshotIndex must increase.
	 */
	public void collectGarbage(long lastSnapshotIndex, int lastSnapshotTerm) {
		if (lastSnapshotIndex < startIndex) {
			throw new IllegalArgumentException("The log start index is not allowed to decrease");
		}
		startIndex = lastSnapshotIndex;
		log.removeIf((e) -> e.index < startIndex);

		if (!log.isEmpty()) {
			VSRaftLogEntry first = log.get(0);
			// drop log if the index/term don't match the expectations.
			if (first.index != lastSnapshotIndex || first.term != lastSnapshotTerm) {
				log.clear();
			}
		}

		if (log.size() == 0) {
			// add stub element
			log.add(new VSRaftLogEntry(lastSnapshotIndex, lastSnapshotTerm, null));
		} else {
			// replace log start with stub element
			log.set(0, new VSRaftLogEntry(lastSnapshotIndex, lastSnapshotTerm, null));
		}
	}
}
