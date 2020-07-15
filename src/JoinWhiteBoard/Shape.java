import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public abstract class Shape implements Serializable {

    public int xStart, xEnd, yStart, yEnd;
    public String text;
    public JPanel drawArea;
    public byte[] imageData;


    public abstract void draw(Graphics2D graphics2D) throws IOException;

}