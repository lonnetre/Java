package vsue.raft;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

/**
 * Run one replica of the counter application.
 */
public class VSCounterReplica {
	public static void main(String[] args) throws IOException, InterruptedException {
		// Check arguments
		if (args.length < 2) {
			System.err.println("usage: java " + VSCounterReplica.class.getName() + " <replica-id> <replica-address-config>");
			System.err.println("  replica-config-file must contain lines of the form" +
					" \"replicaX=hostname:port\" (without quotes), where X is the replica id");
			System.exit(1);
		}

		// Parse arguments
		int replicaId = Integer.parseInt(args[0]);
		InetSocketAddress[] protocolAddresses = parseAddresses(args[1]);
		if (replicaId < 0 || replicaId >= protocolAddresses.length) {
			System.err.println("expected replica id between 0 and " + (protocolAddresses.length - 1) + " (inclusive)");
			System.exit(1);
		}
		System.out.println("Loaded " + protocolAddresses.length + " addresses:");
		for (int i = 0; i < protocolAddresses.length; i++) {
			System.out.println("    " + i + " " + protocolAddresses[i] + ((replicaId == i) ? " <-- this replica" : ""));
		}

		// Use separate ports for the registry of the protocol
		InetSocketAddress[] appAddresses = deriveClientAddresses(protocolAddresses);

		// Reduce handshakeTimeout to a reasonable value
		System.setProperty("sun.rmi.transport.tcp.handshakeTimeout", "100");
		// Limit execution time of rpc, must not be used on the client side
		// Breaks up RPC deadlocks between replicas by timing out stuck RPCs
		System.setProperty("sun.rmi.transport.tcp.responseTimeout", "100");

		// Create application and protocol
		VSRaftProtocol protocol = new VSRaftProtocol(replicaId, protocolAddresses);
		VSCounterServer app = new VSCounterServer(protocol, replicaId, protocolAddresses.length);
		// Start everything
		app.init(appAddresses[replicaId].getPort());

		// keep on running
		Thread.sleep(Long.MAX_VALUE);
	}

	/**
	 * Parse the config file into a list of InetSocketAddresses. The config
	 * file must contain lines "replicaX=host:port", where X is the replica id
	 * and host and port are the hostname and port for that replica.
	 *
	 * @param configFilename Filename of the replica configuration file
	 * @return parsed addresses
	 * @throws IOException if the config file cannot be read
	 */
	static InetSocketAddress[] parseAddresses(String configFilename) throws IOException {
		Properties properties;
		try (FileInputStream inputStream = new FileInputStream(configFilename)) {
			InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			properties = new Properties();
			properties.load(isr);
		}

		ArrayList<String> addresses = new ArrayList<>();
		int counter = 0;
		while (true) {
			String address = properties.getProperty("replica" + counter);
			counter++;
			if (address == null) break;
			addresses.add(address);
		}

		InetSocketAddress[] clientAddresses = new InetSocketAddress[addresses.size()];
		for (int i = 0; i < clientAddresses.length; i++) {
			// Parse address
			String[] parts = addresses.get(i).split(":");
			clientAddresses[i] = new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
		}
		return clientAddresses;
	}

	/**
	 * Generate an array of addresses for use by the client connection to counter instances.
	 * The array is indexed by the replica id. Each counter instance is assigned
	 * a unique port based on the corresponding protocol address.
	 *
	 * @param protocolAddresses addresses used for the protocol instances
	 * @return addresses for use by the counter application instances
	 */
	static InetSocketAddress[] deriveClientAddresses(InetSocketAddress[] protocolAddresses) {
		HashSet<Integer> usedPorts = new HashSet<>();
		for (InetSocketAddress appAddress : protocolAddresses) {
			usedPorts.add(appAddress.getPort());
		}

		InetSocketAddress[] addAddresses = new InetSocketAddress[protocolAddresses.length];
		for (int i = 0; i < protocolAddresses.length; i++) {
			InetSocketAddress address = protocolAddresses[i];
			// find next free port
			int port = address.getPort();
			while (usedPorts.contains(port)) {
				port++;
			}
			// use each port only once
			usedPorts.add(port);
			addAddresses[i] = new InetSocketAddress(address.getHostName(), port);
		}
		return addAddresses;
	}
}
