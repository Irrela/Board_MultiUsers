import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * @author Ruocheng Ning
 * @studentId 1106219
 * @Project Distributed System Assigment2
 *
 */

public class ClientGUI extends JFrame {

    JPanel mainPanel;
    JPanel leftPanel;
    CanvasPanel boardPanel;
    JPanel fileManagePanel;
    JPanel operationPanel;
    JPanel inputPanel;
    JPanel statusPanel;
    ButtonGroup buttonGroup;
    JTextArea inputText;
    JTextArea clientsList;


    public ClientGUI() throws HeadlessException {
        super();
    }

    public void intiGui() {

        this.setSize(700, 600);
        this.setTitle("Client Window");
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //Main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        this.add(mainPanel);

        //Board panel
        boardPanel = new CanvasPanel();
        boardPanel.setBackground(Color.white);
        mainPanel.add(boardPanel,BorderLayout.CENTER);

//        BufferedImage bImage = new BufferedImage(500, 600, BufferedImage.TYPE_INT_RGB);
//        Image image = new Image(boardPanel, bImage);
//        boardPanel.addShape(image, 0);

        //Left panel
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(100, 200));
        leftPanel.setLayout(new BorderLayout(5, 0));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        //File management panel
        fileManagePanel = new JPanel();
        fileManagePanel.setPreferredSize(new Dimension(100, 200));
        fileManagePanel.setLayout(new BorderLayout());
        fileManagePanel.setBackground(Color.GRAY);
        leftPanel.add(fileManagePanel, BorderLayout.NORTH);

        operationPanel = new JPanel();
        operationPanel.setPreferredSize(new Dimension(100, 200));
        operationPanel.setLayout(new FlowLayout(0));
        operationPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.add(operationPanel, BorderLayout.CENTER);

        inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(100, 200));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Color.GRAY);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);

        //Server status list
        statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(100, 0));
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBackground(Color.GRAY);
        mainPanel.add(statusPanel, BorderLayout.EAST);

        statusPanel.add(new JLabel("Clients in room"), BorderLayout.NORTH);

        clientsList = new JTextArea();
        clientsList.setLineWrap(true);
        clientsList.setBackground(Color.LIGHT_GRAY);
        statusPanel.add(clientsList, BorderLayout.CENTER);


        buttonGroup = new ButtonGroup();
        {
            JRadioButton lineButton = new JRadioButton("Line");
            JRadioButton circleButton = new JRadioButton("Circle");
            JRadioButton ovalButton = new JRadioButton("Oval");
            JRadioButton rectButton = new JRadioButton("Rectangle");
            JRadioButton textButton = new JRadioButton("Text");


            lineButton.setActionCommand("drawLine");
            circleButton.setActionCommand("drawCircle");
            ovalButton.setActionCommand("drawOval");
            rectButton.setActionCommand("drawRectangle");
            textButton.setActionCommand("drawText");

            buttonGroup.add(lineButton);
            buttonGroup.add(circleButton);
            buttonGroup.add(ovalButton);
            buttonGroup.add(rectButton);
            buttonGroup.add(textButton);

            operationPanel.add(new JLabel("Draw Mode:"));
            operationPanel.add(lineButton);
            operationPanel.add(circleButton);
            operationPanel.add(ovalButton);
            operationPanel.add(rectButton);
            operationPanel.add(textButton);

        }

        inputText = new JTextArea("Text here, and click board to put on your word.");
        inputText.setLineWrap(true);
        inputText.setBackground(Color.LIGHT_GRAY);
        inputPanel.add(new JLabel("Text below:"), BorderLayout.NORTH);
        inputPanel.add(inputText, BorderLayout.CENTER);

        boardPanel.buttonGroup = this.buttonGroup;
        boardPanel.inputText = this.inputText;

        this.setVisible(true);


    }



}

