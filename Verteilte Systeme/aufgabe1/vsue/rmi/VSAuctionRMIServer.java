package vsue.rmi;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class VSAuctionRMIServer {
    // Folie 1.2:11 (Aufgabe 1: RMI, 1:1 Implementierung)
    // TODO: RemoteException (createRegistry, bind), 
    // TODO: AlreadyBoundException(bind), 
    // TODO: InterruptedException (sleep)

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException{
        // Remote-Objekt erzeugen
        VSAuctionServiceImpl auctionServiceImpl = new VSAuctionServiceImpl();
        /*
        Remote-Objekt auf Port 12678 exportieren 
        (Server stellt Anwendung als Remoteobjekt bereit)
        */
        VSAuctionService auctionService = (VSAuctionService) UnicastRemoteObject.exportObject(auctionServiceImpl, ServerConfig.RMI_EXPORT_PORT);
        // Auktionsdienst muss über eine Registry bekannt gemacht werden
        Registry registry = LocateRegistry.createRegistry(ServerConfig.RMI_REGISTRY_PORT);
        // bind - Zuordnung eines Objekts zu einem eindeutigen Namen
        registry.bind("auctionService", auctionService);

        System.out.println("Server started at port " + ServerConfig.RMI_REGISTRY_PORT);

        // Prozess weiterlaufen lassen
        Thread.sleep(Long.MAX_VALUE);
    }
}


