package vsue.rpc;

import java.rmi.RemoteException;
import java.util.HashMap;

public class VSAuctionServiceImpl implements VSAuctionService {

    private final HashMap <VSAuction, VSAuctionEventHandler> auctions = new HashMap<>();
    private final HashMap <VSAuction, VSAuctionEventHandler> previousBidders = new HashMap<>();

    @Override
    public synchronized void registerAuction(VSAuction auction, int duration, VSAuctionEventHandler handler) throws VSAuctionException, RemoteException {

        if (duration < 0) {
            throw new IllegalArgumentException("[ERROR] - duration is negative");
        }

        for (VSAuction existing : auctions.keySet()) {
            if (existing.getName().equals(auction.getName())) {
                throw new VSAuctionException("[ERROR] - Auction already registered");
            }
        }

        auctions.put(auction, handler);
        previousBidders.put(auction, handler);

        new Thread(() -> {
            try {
                Thread.sleep(duration * 1000L);
                VSAuctionEventHandler winnerHandler = previousBidders.get(auction);
                VSAuctionEventHandler endHandler = auctions.get(auction);

                if (winnerHandler != null)
                    winnerHandler.handleEvent(VSAuctionEventType.AUCTION_WON, auction);
                if (endHandler != null)
                    endHandler.handleEvent(VSAuctionEventType.AUCTION_END, auction);
            } catch (InterruptedException | RemoteException e) {
                System.err.println("Error in auction timeour thread: " + e.getMessage());
            } finally {
                synchronized (this) {
                    auctions.remove(auction);
                    previousBidders.remove(auction);
                }
            }
        }).start();
    }

    @Override
    public synchronized VSAuction[] getAuctions() throws RemoteException {
        return auctions.keySet().toArray(new VSAuction[0]);
    }

    @Override
    public synchronized boolean placeBid(String userName, String auctionName, int price, VSAuctionEventHandler handler) throws VSAuctionException, RemoteException {
        for(VSAuction auction: auctions.keySet()) {
            if (auction.getName().equals(auctionName)) {
                if (price > auction.getPrice()) {
                    auction.setPrice(price);
                    VSAuctionEventHandler previousHandler = previousBidders.get(auction);
                    if (previousHandler != null)
                        previousHandler.handleEvent(VSAuctionEventType.HIGHER_BID, auction);
                    previousBidders.put(auction, handler);
                    return true;
                } else {
                    return false;
                }
            }
        }
        throw new VSAuctionException("[ERROR] - auctionName doesn't match any of the auctions");
    }
}
