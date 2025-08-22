package exercise6_2.vsue.lamport;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class VSLamportLock {

	private final VSLamportProtocol protocol;
	private final Semaphore semaphore = new Semaphore(0);

	// TODO: Implement constructor
	public VSLamportLock(VSLamportProtocol protocol) {
		if (protocol == null) throw new IllegalArgumentException("protocol must not be null");
		this.protocol = protocol;
		protocol.setLock(this);
	}

	// TODO: Block until having acquired the lock; must not be interruptible
	public void lock() {
		// Enqueue a LOCK event
		protocol.event(new VSLamportEvent(VSLamportEventType.LOCK, null));
		// Wait for the permit
		semaphore.acquireUninterruptibly();
	}

	// TODO: Try to acquire the lock
	public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
		protocol.event(new VSLamportEvent(VSLamportEventType.LOCK, null));
		try {
			boolean ok = semaphore.tryAcquire(timeout, unit);
			if (!ok) {
				protocol.event(new VSLamportEvent(VSLamportEventType.UNLOCK, null));
			}
			return ok;
		} catch (InterruptedException e) {
			protocol.event(new VSLamportEvent(VSLamportEventType.UNLOCK, null));
			throw e;
		}
	}

	// TODO: Release the lock
	public void unlock() {
		protocol.event(new VSLamportEvent(VSLamportEventType.UNLOCK, null));
	}

	// Called by the protocol when it's OK to enter
	void grant() {
		semaphore.release();
	}
}
