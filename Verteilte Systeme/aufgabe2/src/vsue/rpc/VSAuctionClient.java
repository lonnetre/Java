package vsue.rpc;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class VSAuctionClient extends VSShell implements VSAuctionEventHandler{

    private final String userName;

    private VSAuctionService auctionService;

    public VSAuctionClient(String userName) {
        this.userName = userName;
    }


    // #############################
    // # INITIALIZATION & SHUTDOWN #
    // #############################

    public void init(String registryHost, int registryPort) {
        try {
            Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
            auctionService = (VSAuctionService) registry.lookup("auctionService");
            // Erstelle einen VSRemoteObjectManager und exportiere den VSAuctionClient (neuer VSServer wenn noch nicht existiert)
            VSRemoteObjectManager.getInstance().exportObject(this); // <- Das hier ist unser Client-Stub!!

            // increment remote counter (Aufgerufen vom Client bei Stub-Erzeugung (per Fernaufruf)
            int objectID = VSRemoteObjectManager.getInstance().getExportedObjectID(this);
            try {
                VSRemoteObjectManager.getInstance().dirty(objectID);
            } catch (RemoteException e) {
                System.err.println(e.getMessage() + " - dirty call failed");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void shutdown() {
        VSRemoteObjectManager objManager = VSRemoteObjectManager.getInstance();
        objManager.unexportObject(this);
        // decrement remote counter (Aufgerufen vom Client bei Stub-Freigabe (per Fernaufruf))
        int objectID = VSRemoteObjectManager.getInstance().getExportedObjectID(this);
        try {
            VSRemoteObjectManager.getInstance().clean(objectID);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + " - fail of dirty call");
        }
    }


    // #################
    // # EVENT HANDLER #
    // #################

    @Override
    public void handleEvent(VSAuctionEventType event, VSAuction auction) throws RemoteException {
        System.out.println("\n[Callback on: " + userName + "] - Event: " + event + ", Auction: " + auction.getName());

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
        try {
            VSAuction auction = new VSAuction(auctionName, startingPrice);
            auctionService.registerAuction(auction, duration, this);
            System.out.println("You registered an auction: " + auctionName + "\n\t- duration: " + duration + " seconds \n\t- starting price: " + startingPrice);
        } catch (VSAuctionException | RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public void list() {
        try {
            System.out.println("--- AUCTIONS ---");
            VSAuction[] auctions = auctionService.getAuctions();

            if (auctions == null || auctions.length == 0) {
                System.out.println("Empty");
                return;
            }

            for (int i = 0; i < auctions.length; i++){
                System.out.println((i+1) + ") name: " + auctions[i].getName() + " | bid: " + auctions[i].getPrice());
            }
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }

    }

    public void bid(String auctionName, int price) {
        try {
            boolean placedBid = auctionService.placeBid(userName, auctionName, price, this);
            if (placedBid) {
                System.out.println("[NOTIFICATION] - Bid is placed!");
            } else {
                System.out.println("[NOTIFICATION] - Failed to place the bid. Try higher price!");
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
            System.err.println("usage: java " + VSAuctionClient.class.getName() + " <user-name> <registry_host> <registry_port>");
            System.exit(1);
        }
    }

    private static void createAndExecuteClient(String[] args) {
        String userName = args[0];
        VSAuctionClient client = new VSAuctionClient(userName);

        String registryHost = args[1];
        int registryPort = Integer.parseInt(args[2]);
        client.init(registryHost, registryPort);
        client.shell();
        client.shutdown();
    }
}
