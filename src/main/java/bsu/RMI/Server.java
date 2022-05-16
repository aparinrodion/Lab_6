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
 * @author Aparin Rodion
 * @see java.rmi.Remote
 * <b>LAB_7</b>
 */
public class Server extends UnicastRemoteObject implements RMIInterface {
    private final RMIInterface repository;
    /**
     * @throws RemoteException remote method call exception
     * adfaf {@link java.lang.String ssssssss} asdfas
     * @see String
     */
    public Server() throws RemoteException {
        //bsu.repository = new DatabaseRepository();
        repository = new StAXRepository();
        //bsu.repository = new JAXBRepository();
    }

    /**
     * @return {@link List List} of CitizenAndAddress from bsu.repository
     * @throws RemoteException remote method call exception
     */
    @Override
    public List<CitizenAndAddress> getList() throws RemoteException {
        return repository.getList();
    }

    /**
     * @return {@link Map Map} where key is city name and value is its population
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
     * @param citizenAndAddress citizen to add
     * @throws RemoteException remote method call exception
     */
    @Override
    public void addCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException {
        repository.addCitizen(citizenAndAddress);
    }

    /**
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
