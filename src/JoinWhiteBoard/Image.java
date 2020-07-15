import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class Image extends Shape  {

    public Image(JPanel drawArea, byte[] imageData) {
        this.drawArea = drawArea;
        this.imageData = imageData;
    }

    @Override
    public void draw(Graphics2D graphics2D) throws IOException {
        graphics2D.drawImage(toBufferImage(imageData), 0, 0, drawArea);
    }

    public BufferedImage toBufferImage(byte[] imageData) throws IOException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
        BufferedImage bImage = ImageIO.read(inputStream);
        return bImage;

    }
}