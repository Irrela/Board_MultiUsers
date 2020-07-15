import java.awt.*;

public class Text extends Shape {

    public Text(String text, int x, int y ) {
        this.text = text;
        this.xStart = x;
        this.yStart = y;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawString(text, xStart, yStart);
    }
}
