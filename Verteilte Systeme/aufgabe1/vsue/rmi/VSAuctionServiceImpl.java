package vsue.rmi;

import java.rmi.RemoteException;
import java.util.HashMap;

public class VSAuctionServiceImpl implements VSAuctionService{

    // alle Aktionen
    private final HashMap <VSAuction, VSAuctionEventHandler> auctions = new HashMap<>();
    private final HashMap <VSAuction, VSAuctionEventHandler> previousBidders = new HashMap<>();

    /*
    1. Ein Aufruf von registerAuction() ermöglicht es einem Client,
     - eine Auktion auction zu starten,
     - die nach duration Sekunden abläuft;
    2. der Parameter handler dient der Übergabe einer (Remote-)Referenz über die der Auktionsdienst den Client später bei Ereignissen zurückrufen kann.
    3. Sollte eine Auktion mit demselben Namen bereits existieren, wird dies per VSAuctionException signalisiert
     */

    @Override
    public synchronized void registerAuction(VSAuction auction, int duration, VSAuctionEventHandler handler) throws VSAuctionException, RemoteException {
        // siehe interface
        if (duration < 0) throw new IllegalArgumentException("[ERROR] - \'duration\' is negative");
        // 3. Teilaufgabe:
        for(VSAuction actn: auctions.keySet()) {
            if (actn.getName().equals(auction.getName())) {
                throw new VSAuctionException("[ERROR] - Auction already registered");
            }
        }

        auctions.put(auction, handler);
        previousBidders.put(auction, handler);

        // 1. + 2. Teilaufgabe
        // TODO: Threads benutzen
        // Aus 0. Uebung:
        Thread test = new Thread(() -> {
            try {
                Thread.sleep(duration * 1000L);
                // handler.handleEvent(VSAuctionEventType.AUCTION_END, auction);
                previousBidders.get(auction).handleEvent(VSAuctionEventType.AUCTION_WON, auction);
                auctions.get(auction).handleEvent(VSAuctionEventType.AUCTION_END, auction);
                auctions.remove(auction);
            } catch (InterruptedException e) {
                // for sleep
                System.err.println(e.getMessage());
            } catch (RemoteException e) {
                // for handleEvent
                System.err.println(e.getMessage());
            }
        });
        test.start();
    }

    /*
    Die Methode getAuctions() liefert alle Auktionen zurück, die zum Zeitpunkt des Aufrufs laufen.
     */
    @Override
    public synchronized VSAuction[] getAuctions() throws RemoteException {
        VSAuction[] result = new VSAuction[auctions.size()];
        int i = 0;
        for (VSAuction auction : auctions.keySet()) {
            result[i++] = auction;
        }
        return result;
    }

    /*
    1. Mittels placeBid() kann ein Client userName ein neues Gebot price für eine Auktion auctionName einreichen.
    2. Am Rückgabewert lässt sich anschließend erkennen, ob der Client das aktuell höchste Gebot abgegeben hat.
    3. Existiert keine Auktion mit dem angegebenen Namen, wirft die Methode eine VSAuctionException.

    synchonized: um sicherzustellen, dass immer nur ein Thread gleichzeitig auf die Methode zugreifen kann
     */
    @Override
    public synchronized boolean placeBid(String userName, String auctionName, int price, VSAuctionEventHandler handler) throws VSAuctionException, RemoteException {
        for(VSAuction actn: auctions.keySet()) {
            if (actn.getName().equals(auctionName)) {
                if (actn.getPrice() < price) {
                    actn.setPrice(price);
                    // Notify the auction creator about the higher bid
                    previousBidders.get(actn).handleEvent(VSAuctionEventType.HIGHER_BID, actn);
                    auctions.put(actn, handler);
                    previousBidders.put(actn, handler);
                    return true;
                }
                return false;
            }
        }
        throw new VSAuctionException("[ERROR] - \'auctionName\' doesn't match any of the auctions");
    }


    // TODO: methode fuer HashMap
}