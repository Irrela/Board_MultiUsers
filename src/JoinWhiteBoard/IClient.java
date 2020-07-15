
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {

    public void init() throws RemoteException;
    public void updateStatus(Shape[] serverShapeList, int serverIndex) throws RemoteException;
    public void showConfirmDialog(String serverMsg) throws RemoteException;
    public void showDialog(String serverMsg) throws RemoteException;
    public Shape[] getShapeList() throws RemoteException;
    public int getShapeIndex() throws RemoteException;
    public void setClientName(String clientName) throws RemoteException;
    public String getClientName() throws RemoteException;
    public void setText(String str) throws RemoteException;
    public void addWindowListener(IRemoteBoard remoteBoard, IClient remoteClient) throws RemoteException;
    public boolean isAlive() throws RemoteException;
    public void crush() throws RemoteException;
    public void setShapeIndex(int num) throws RemoteException;

    public void lock() throws RemoteException;
    public void unlock() throws RemoteException;
    public boolean islocked() throws RemoteException;

}
