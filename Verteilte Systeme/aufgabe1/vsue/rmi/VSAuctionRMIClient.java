package vsue.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class VSAuctionRMIClient extends VSShell implements VSAuctionEventHandler {

	// The user name provided via command line.
	private final String userName;

	private VSAuctionService auctionService;
	private VSAuctionEventHandler exportedHandler;

	public VSAuctionRMIClient(String userName) {
		this.userName = userName;
	}


	// #############################
	// # INITIALIZATION & SHUTDOWN #
	// #############################

	public void init(String registryHost, int registryPort) {
		/*
		 * TODO: Implement client startup code
		 
		 * Eine Verbindung zum RMI-Server herstellen
		 * Den Auktionsdienst finden
		 * Den Client für Callbacks vorbereiten
		 */

		try {
			// Folie 1.2:12 (Aufgabe 1: RMI)
			Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
			// lookup - Rückgabe der Remote-Referenz zu einem Namen
			auctionService = (VSAuctionService) registry.lookup("auctionService");
			// this - Instanz 
			exportedHandler = (VSAuctionEventHandler) UnicastRemoteObject.exportObject(this, 0);
		} catch (RemoteException | NotBoundException e) {
			System.err.println(e.getMessage());
		} 
	}

	public void shutdown() {
		/*
		 * TODO: Implement client shutdown code

		 * Die Verbindung zum Server sauber beenden
		 */

		 try {
			// force = true (If the force parameter is true, the object is forcibly unexported even if there are pending calls to the remote object or the remote object still has calls in progress)
			// this - Instanz 
			UnicastRemoteObject.unexportObject(this, true);
			System.out.println("Client unexported");
		 } catch (NoSuchObjectException e) {
			System.err.println(e.getMessage());
		 }
	}


	// #################
	// # EVENT HANDLER #
	// #################

	@Override
	public void handleEvent(VSAuctionEventType event, VSAuction auction) {
		/*
		 * TODO: Implement event handler

		 * Auf verschiedene Ereignistypen reagieren
		 * Dem Benutzer entsprechende Nachrichten anzeigen
		 */
		System.out.println("\n[Callback on : " + userName + "] - Event: " + event + ", Auction: " + auction.getName());
		
		switch (event) {
			case HIGHER_BID:
				System.out.println("[NOTIFICATION] - Higher bid was placed on auction: " + auction.getName() + " with new price: " + auction.getPrice());
				break;
			case AUCTION_END:
				System.out.println("[NOTIFICATION] - Auction: " + auction.getName() + " ended with final price: " + auction.getPrice());
				break;
			case AUCTION_WON:
				System.out.println("[NOTIFICATION] - You won an auction: " + auction.getName() + " with your bid closing price: " + auction.getPrice());
				break;
			default:
				break;
		}

	}


	// ##################
	// # CLIENT METHODS #
	// ##################

	public void register(String auctionName, int duration, int startingPrice) {
		/*
		 * TODO: Register auction

		 * Eine neue Auktion erstellen
		 * Sie beim Server registrieren
		 */
		
		try {
			VSAuction auction = new VSAuction(auctionName, startingPrice);
			auctionService.registerAuction(auction, duration, exportedHandler);
			System.out.println("You registered an auction: " + auctionName + "\n\t- with duration: " + duration + " seconds \n\t- starting price: " + startingPrice);
		} catch (VSAuctionException | RemoteException e) {
			System.err.println(e.getMessage());
		} 
	}

	public void list() {
		/*
		 * TODO: List all auctions that are currently in progress

		 * Alle aktuellen Auktionen vom Server abrufen
		 * Sie dem Benutzer anzeigen
		 */
		try {
			System.out.println("--- Auctions ---");
			VSAuction[] auctions = auctionService.getAuctions();
			
			for (int i = 0; i < auctions.length; i++){
				System.out.println((i+1) + ") name: " + auctions[i].getName() + " | bid: " + auctions[i].getPrice());
			}
		} catch (RemoteException e) {
			System.err.println(e.getMessage());
		}

	}

	public void bid(String auctionName, int price) {
		/*
		 * TODO: Place a new bid

		 * Ein Gebot für eine Auktion abgeben
		 * Den Client als Handler registrieren, um über Änderungen informiert zu werden
		 */

		try {
			boolean placedBid = auctionService.placeBid(userName, auctionName, price, exportedHandler);
			if (placedBid) {
				System.out.println("Bid is placed!");
			} else {
				System.out.println("Failed to place the bid. Try higher price!");
			}
		} catch (VSAuctionException | RemoteException e) {
			System.err.println(e.getMessage());
		}
	}


	// #########
	// # SHELL #
	// #########

	protected boolean processCommand(String[] args) {
		switch (args[0]) {
		case "help":
		case "h":
			System.out.println("The following commands are available:\n"
					+ "  help\n"
					+ "  bid <auction-name> <price>\n"
					+ "  list\n"
					+ "  register <auction-name> <duration> [<starting-price>]\n"
					+ "  quit"
			);
			break;
		case "register":
		case "r":
			if (args.length < 3)
				throw new IllegalArgumentException("Usage: register <auction-name> <duration> [<starting-price>]");
			int duration = Integer.parseInt(args[2]);
			int startingPrice = (args.length > 3) ? Integer.parseInt(args[3]) : 0;
			register(args[1], duration, startingPrice);
			break;
		case "list":
		case "l":
			list();
			break;
		case "bid":
		case "b":
			if (args.length < 3) throw new IllegalArgumentException("Usage: bid <auction-name> <price>");
			int price = Integer.parseInt(args[2]);
			bid(args[1], price);
			break;
		case "exit":
		case "quit":
		case "x":
		case "q":
			return false;
		default:
			throw new IllegalArgumentException("Unknown command: " + args[0] + "\nUse \"help\" to list available commands");
		}
		return true;
	}


	// ########
	// # MAIN #
	// ########

	public static void main(String[] args) {
		checkArguments(args);
		createAndExecuteClient(args);
	}

	private static void checkArguments(String[] args) {
		if (args.length < 3) {
			System.err.println("usage: java " + VSAuctionRMIClient.class.getName() + " <user-name> <registry_host> <registry_port>");
			System.exit(1);
		}
	}

	private static void createAndExecuteClient(String[] args) {
		String userName = args[0];
		VSAuctionRMIClient client = new VSAuctionRMIClient(userName);

		String registryHost = args[1];
		int registryPort = Integer.parseInt(args[2]);
		client.init(registryHost, registryPort);
		client.shell();
		client.shutdown();
	}
}
