import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteBoard extends Remote {

    public boolean approve(String remoteClientId) throws RemoteException, NotBoundException;

    public int generateClientId() throws RemoteException;

    public Shape[] getShapeList() throws RemoteException;

    public int getShapeIndex() throws RemoteException;

    public void updateStatus(Shape[] clientShapeList, int clientIndex) throws RemoteException;

    public void broadcast() throws RemoteException;

    public void delete(IClient remoteClient) throws  RemoteException;

    public void broadcastClients() throws  RemoteException;

    public void addWindowListener() throws RemoteException;

    public void addKickListener() throws RemoteException;

    public void addNewListener() throws RemoteException;

    public void addOpenListener() throws RemoteException;

    public void addSaveListener() throws RemoteException;

    public void addSaveAsListener() throws RemoteException;

    public void addCloseListener() throws RemoteException;

}