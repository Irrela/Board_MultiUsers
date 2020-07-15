
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Ruocheng Ning
 * @studentId 1106219
 * @Project Distributed System Assigment2
 *
 *
 * < Main class of Server program >
 *
 * Maintain board and in-room status with two lists.
 *
 * Board status is represented as a series Shapes,
 * repaint all shapes in the list each time to get the picture.
 *
 */

public class BoardServer extends UnicastRemoteObject implements IRemoteBoard {

    ManagerGUI managerGUI;
    List<IClient> clientList;
    int clientId;
    String currentFile;

    static Registry registry;


    public BoardServer() throws RemoteException {
        super();
        managerGUI = new ManagerGUI();
        managerGUI.intiGui();
        clientList = new ArrayList<>();
        clientId = 10000;
        currentFile = "";
    }

    public static void main(String[] args) {

        try {
            IRemoteBoard remoteBoard = new BoardServer();
            registry = LocateRegistry.createRegistry(Integer.parseInt(args[1]));

            // Notify all clients before Manager GUI shuts down.
            remoteBoard.addWindowListener();

            // Notify clients, notify them and update Server status.
            remoteBoard.addKickListener();

            // File management related listeners
            remoteBoard.addNewListener();
            remoteBoard.addOpenListener();
            remoteBoard.addSaveListener();
            remoteBoard.addSaveAsListener();

            registry.rebind("Board", remoteBoard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Manager decides if a new client can join the game.
     */
    @Override
    public boolean approve(String remoteClientName) throws RemoteException, NotBoundException {
        boolean answer;
        IClient remoteClient = (IClient) registry.lookup(remoteClientName);

        int n = JOptionPane.showConfirmDialog(managerGUI, "A new client wants to join this room. ", "New Player Requesting", JOptionPane.YES_NO_CANCEL_OPTION);

        if (n == 0) {
            remoteClient.showDialog("Successfully connect to Server\nWelcome " + remoteClientName);
            remoteClient.init();
            clientList.add(remoteClient);
            remoteClient.updateStatus(getShapeList(), getShapeIndex());
            broadcastClients();
            answer = true;
        } else {
            remoteClient.showDialog("Sorry, " + remoteClientName + "\nThe manager rejected your request.");
            registry.unbind(remoteClientName);
            answer = false;
        }

        return answer;
    }

    @Override
    public int generateClientId() throws RemoteException {
        return clientId++;
    }

    @Override
    public Shape[] getShapeList() throws RemoteException {
        return managerGUI.boardPanel.shapeList;
    }

    @Override
    public int getShapeIndex() throws RemoteException {
        return managerGUI.boardPanel.shapeIndex;
    }

    /**
     * Update Server shape list when clients draw something.
     */
    @Override
    public synchronized void updateStatus(Shape[] clientShapeList, int clientIndex) throws RemoteException {
        int i = managerGUI.boardPanel.shapeIndex;

        while (i < clientIndex) {
            managerGUI.boardPanel.shapeList[i] = clientShapeList[i];
            i++;
        }

        managerGUI.boardPanel.shapeIndex = i;

        managerGUI.boardPanel.repaint();
    }

    /**
     * Share latest shape list with every clients in room.
     */
    @Override
    public void broadcast() throws RemoteException {

        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).updateStatus(getShapeList(), getShapeIndex());
        }
    }


    @Override
    public void delete(IClient remoteClient) throws RemoteException {
        clientList.remove(remoteClient);
    }

    /**
     * Share latest in-room status with every clients in room.
     */
    @Override
    public void broadcastClients() throws RemoteException {
        String list = "";
        for (int i = 0; i < clientList.size(); i++) {
            list += clientList.get(i).getClientName() + "\n";
        }

        managerGUI.clientsList.setText(list);

        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).setText(list);
        }
    }

    @Override
    public void addWindowListener() throws RemoteException {
        managerGUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    for (int i = 0; i < clientList.size(); i++) {
                        clientList.get(i).showDialog("The Server has been shut down\nThanks for your use, " + clientList.get(i).getClientName());
                    }

                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    @Override
    public void addKickListener() throws RemoteException {
        managerGUI.kickButton.addActionListener(e -> {

            String clientName = managerGUI.kickBox.getText();
            try {
                for (int i = 0; i < clientList.size(); i++) {
                    if (clientList.get(i).getClientName().equals(clientName)) {
                        clientList.get(i).showDialog("You have been kicked out by manager.");
                        clientList.get(i).crush();
                        registry.unbind(clientName);
                        delete(clientList.get(i));
                        broadcastClients();
                        JOptionPane.showMessageDialog(managerGUI, clientName + " has been kicked out.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(managerGUI, "There is no one named " + clientName);
            }
        });
    }


    @Override
    public  void addNewListener() throws RemoteException {
        managerGUI.newButton.addActionListener(e -> {

                currentFile = "";

            try {
                for (int i = 0; i < clientList.size(); i++) {
                    clientList.get(i).lock();
                }

                managerGUI.boardPanel.shapeIndex = 0;
                managerGUI.repaint();
                for (int i = 0; i < clientList.size(); i++) {
                    clientList.get(i).setShapeIndex(0);
                }

                for (int i = 0; i < clientList.size(); i++) {
                    clientList.get(i).unlock();
                }

                broadcast();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void addOpenListener() throws RemoteException {
        managerGUI.openButton.addActionListener(e -> {

            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showOpenDialog(null);
            File file = jFileChooser.getSelectedFile();

            if(file != null) {

                currentFile = file.getAbsolutePath();
                BufferedImage bufferedImage;

                try {
                    bufferedImage = ImageIO.read(file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "jpg", baos);
                    byte[] imageData = baos.toByteArray();

                    for (int i = 0; i < clientList.size(); i++) {
                        clientList.get(i).lock();
                    }

                    managerGUI.boardPanel.shapeIndex = 0;
                    for (int i = 0; i < clientList.size(); i++) {
                        clientList.get(i).setShapeIndex(0);
                    }

                    managerGUI.boardPanel.shapeList[0] = new Image(managerGUI.boardPanel, imageData);
                    managerGUI.boardPanel.shapeIndex = 1;
                    managerGUI.repaint();

                    for (int i = 0; i < clientList.size(); i++) {
                        clientList.get(i).unlock();
                    }

//                    broadcast();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * If the picture being edited has never been saved, it will act as 'saveAs' operation.
     *
     */
    @Override
    public void addSaveListener() throws RemoteException {
        managerGUI.saveButton.addActionListener(e -> {

            BufferedImage bImgage = new BufferedImage(managerGUI.boardPanel.getWidth(), managerGUI.boardPanel.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2D = bImgage.createGraphics();
            managerGUI.boardPanel.paintAll(graphics2D);

            if(currentFile.equals("")) {

                String fileName = JOptionPane.showInputDialog(managerGUI, "Save this picure as :", JOptionPane.OK_OPTION);

                try {
                    if (ImageIO.write(bImgage, "png", new File("/Users/sakagami/Desktop/" + fileName + ".png"))){
                        currentFile = "/Users/sakagami/Desktop/" + fileName + ".png";
                        JOptionPane.showMessageDialog(managerGUI, "Picture Saved");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
                try {
                    if (ImageIO.write(bImgage, "png", new File(currentFile))){
                        JOptionPane.showMessageDialog(managerGUI, "Picture Saved");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void addSaveAsListener() throws RemoteException {
        managerGUI.saveAsButton.addActionListener(e -> {

            BufferedImage bImgage = new BufferedImage(managerGUI.boardPanel.getWidth(), managerGUI.boardPanel.getHeight(),
                    BufferedImage.TYPE_INT_RGB );

            Graphics2D graphics2D = bImgage.createGraphics();
            managerGUI.boardPanel.paintAll(graphics2D);

            String fileName = JOptionPane.showInputDialog(managerGUI,"Save this picure as :");

            try{
                if (ImageIO.write(bImgage, "png", new File("/Users/sakagami/Desktop/"+ fileName +".png")));
                {
                    JOptionPane.showMessageDialog(managerGUI, "Picture Saved");
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }



}

