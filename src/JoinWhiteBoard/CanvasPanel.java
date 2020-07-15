import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * @author Ruocheng Ning
 * @studentId 1106219
 * @Project Distributed System Assigment2
 *
 */

public class CanvasPanel extends JPanel {

    static final int MAX_SHAPE_CAPACIY = 3000;

    ButtonGroup buttonGroup;
    JTextArea inputText;

    Shape[] shapeList;
    int shapeIndex;

    CanvasPanel() {
        shapeList = new Shape[MAX_SHAPE_CAPACIY];
        shapeIndex = 0;
        this.addPaintListener();
    }

    void draw(Graphics2D graphics2D, Shape shape) throws IOException {
        shape.draw(graphics2D);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        int i = 0;

        while(i < shapeIndex){
            try {
                draw(graphics2D, shapeList[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }

    }

    void addPaintListener() throws NullPointerException{

        addMouseListener(new MouseListener() {

            String drawMode;
            int xStart, yStart;

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ButtonModel buttonModel = buttonGroup.getSelection();
                    drawMode = buttonModel.getActionCommand();

                    if (drawMode.equals("drawText")) {

                        synchronized (this) {
                            addShape(new Text(inputText.getText(), e.getX(), e.getY()), shapeIndex);
                            repaint();
                        }
                    }
                }catch (Exception ex){
                    System.out.println("Please choose a draw mode.");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                xStart = e.getX();
                yStart = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    ButtonModel buttonModel = buttonGroup.getSelection();
                    drawMode = buttonModel.getActionCommand();

                    synchronized (this) {
                        if (drawMode.equals("drawLine")) {
                            addShape(new Line(xStart, yStart, e.getX(), e.getY()), shapeIndex);
                            repaint();
                        } else if (drawMode.equals("drawCircle")) {
                            addShape(new Circle(xStart, yStart, e.getX(), e.getY()), shapeIndex);
                            repaint();
                        } else if (drawMode.equals("drawOval")) {
                            addShape(new Oval(xStart, yStart, e.getX(), e.getY()), shapeIndex);
                            repaint();
                        } else if (drawMode.equals("drawRectangle")) {
                            addShape(new Rectangle(xStart, yStart, e.getX(), e.getY()), shapeIndex);
                            repaint();
                        }
                    }
                }catch (Exception ex){
                    System.out.println("Please choose a draw mode.");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
    }

    void addShape(Shape shape, int index) {
        shapeList[index] = shape;
        shapeIndex++;
    }
}

