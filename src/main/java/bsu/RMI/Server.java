package bsu.RMI;

import bsu.model.CitizenAndAddress;
import bsu.repository.StAXRepository;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * <h1 style="color:green">BSU FAMCS</h1>
 * <h2 style="color:green">LAB_6</h2>
 * @author Aparin Rodion
 * @see java.rmi.Remote
 */
public class Server extends UnicastRemoteObject implements RMIInterface {
    private final RMIInterface repository;
    /**
     * @throws RemoteException remote method call exception
     */
    public Server() throws RemoteException {
        //bsu.repository = new DatabaseRepository();
        repository = new StAXRepository();
        //bsu.repository = new JAXBRepository();
    }

    /**
     * @return {@link List List} of {@link CitizenAndAddress CitizenAndAddress} from Repository
     * @throws RemoteException remote method call exception
     */
    @Override
    public List<CitizenAndAddress> getList() throws RemoteException {
        return repository.getList();
    }

    /**
     * @return {@link Map Map} where <i>key</i> is <b>city name</b> and <i>value</i> is its <b>population</b>
     * @throws RemoteException remote method call exception
     */
    @Override
    public Map<String, Integer> getCitiesAndPopulation() throws RemoteException {
        return repository.getCitiesAndPopulation();
    }

    /**
     * @return number of citizens
     * @throws RemoteException remote method call exception
     */
    @Override
    public int getNumber() throws RemoteException {
        return repository.getNumber();
    }

    /**
     * <p>Method for adding CitizenAndAddress to repository</p>
     * @param citizenAndAddress citizen to add
     * @throws RemoteException remote method call exception
     */
    @Override
    public void addCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        repository.addCitizen(citizenAndAddress);
    }

    /**
     * <p>Method for deleting CitizenAndAddress from repository</p>
     * @param citizenAndAddress citizen to delete
     * @throws RemoteException remote method call exception
     */
    @Override
    public void deleteCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        repository.deleteCitizen(citizenAndAddress);
    }

    /**
     * <p>Method to start server</p>
     * @param args args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting server");
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Server", server);
            System.out.println("Server is working");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
