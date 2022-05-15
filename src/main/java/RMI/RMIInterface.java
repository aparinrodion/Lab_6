package RMI;

import model.CitizenAndAddress;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface RMIInterface extends Remote {
    List<CitizenAndAddress> getList() throws RemoteException;

    Map<String, Integer> getCitiesAndPopulation() throws RemoteException;

    int getNumber() throws RemoteException;

    void addCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException;

    void deleteCitizen(CitizenAndAddress citizenAndAddress) throws RemoteException;
}
