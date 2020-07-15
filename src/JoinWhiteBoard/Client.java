import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Ruocheng Ning
 * @studentId 1106219
 * @Project Distributed System Assigment2
 *
 */

public class Client extends UnicastRemoteObject implements IClient {

    ClientGUI clientGUI;
    String clientId;
    boolean isAlive;
    boolean islocked;

    static Registry registry;

    public Client() throws RemoteException {
        super();
        clientGUI = new ClientGUI();
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        try {

            registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            IRemoteBoard remoteBoard = (IRemoteBoard) registry.lookup("Board");

            IClient remoteClient = new Client();
            remoteClient.setClientName(args[2]);
            registry.bind(remoteClient.getClientName(), remoteClient);

            // After being accpted by Manager, keep listening the status of Server
            // until disconnecting or being kicked out.
            if (remoteBoard.approve(remoteClient.getClientName())) {

                remoteClient.addWindowListener(remoteBoard, remoteClient);

                while (remoteClient.isAlive()) {
                    if(!remoteClient.islocked()) {
                        if (remoteBoard.getShapeIndex() < remoteClient.getShapeIndex()) {
                            synchronized (remoteClient) {
                                remoteBoard.updateStatus(remoteClient.getShapeList(), remoteClient.getShapeIndex());
                                remoteBoard.broadcast();
                            }
                        }
                        if (remoteClient.getShapeIndex() < remoteBoard.getShapeIndex()) {
                            remoteClient.updateStatus(remoteBoard.getShapeList(), remoteBoard.getShapeIndex());
                        }
                    }
                }
            }

            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }


    @Override
    public void init() throws RemoteException {
        clientGUI.intiGui();
        isAlive = true;
        islocked = false;
    }

    // Update local shape list if delayed.
    @Override
    public  void updateStatus(Shape[] serverShapeList, int serverIndex) throws RemoteException {

        int i = clientGUI.boardPanel.shapeIndex;

        while (i < serverIndex) {
            clientGUI.boardPanel.shapeList[i] = serverShapeList[i];
            i++;
        }

        clientGUI.boardPanel.shapeIndex = i;

        clientGUI.boardPanel.repaint();
    }

    @Override
    public void showConfirmDialog(String serverMsg) {
        JOptionPane.showConfirmDialog(clientGUI, serverMsg, "Server Response", JOptionPane.OK_CANCEL_OPTION);
    }

    @Override
    public void showDialog(String serverMsg) throws RemoteException {
        JOptionPane.showMessageDialog(clientGUI, serverMsg);
    }

    @Override
    public Shape[] getShapeList() throws RemoteException {
        return clientGUI.boardPanel.shapeList;
    }

    @Override
    public int getShapeIndex() throws RemoteException {
        return clientGUI.boardPanel.shapeIndex;
    }

    @Override
    public void setClientName(String remoteClientId) throws RemoteException {
        clientId = remoteClientId;
    }

    @Override
    public String getClientName() throws RemoteException {
        return clientId;
    }

    @Override
    public void setText(String str) throws RemoteException {
        clientGUI.clientsList.setText(str);
    }

    // Unbind and delete clients from Server clientList before
    // clients diconnect, close their GUI,
    @Override
    public void addWindowListener(IRemoteBoard remoteBoard, IClient remoteClient) throws RemoteException {
        clientGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    registry.unbind(remoteClient.getClientName());
                    remoteBoard.delete(remoteClient);
                    remoteBoard.broadcastClients();
                } catch (RemoteException | NotBoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return isAlive;
    }

    // Modify the signal to terminate listening.
    @Override
    public void crush() throws RemoteException {
        isAlive = false;
    }

    @Override
    public  void setShapeIndex(int num) throws RemoteException {
        clientGUI.boardPanel.shapeIndex = num;
    }

    // Lock all clients if Server is doing file management.
    @Override
    public void lock() throws RemoteException {
        islocked = true;
    }

    @Override
    public void unlock() throws RemoteException {
        islocked = false;
    }

    @Override
    public boolean islocked() throws RemoteException {
        return islocked;
    }

}


