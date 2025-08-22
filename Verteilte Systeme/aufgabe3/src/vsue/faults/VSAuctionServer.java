package vsue.faults;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class VSAuctionServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException{
        try {
            System.setProperty("sun.rmi.registry.registryFilter", "vsue.**");
            VSAuctionServiceImpl auctionServiceImpl = new VSAuctionServiceImpl();
            VSAuctionService stub = (VSAuctionService) VSRemoteObjectManager.getInstance().exportObject(auctionServiceImpl);
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(VSConfig.REGISTRY_PORT);
            } catch (RemoteException e) {
                System.out.println("Registry already exists, retrieving existing one...");
                registry = LocateRegistry.getRegistry(VSConfig.REGISTRY_PORT);
            }
            registry.bind("auctionService", stub);
            System.out.println("Start VSAuctionServer on port " + VSConfig.REGISTRY_PORT);
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            System.err.println("Server failed to start:");
            e.printStackTrace();
        }
    }
}
