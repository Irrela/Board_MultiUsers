import java.awt.*;

public class Oval extends Shape {

    public Oval(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawOval(Math.min(xStart, xEnd), Math.min(yStart, yEnd),
                            Math.abs(xStart - xEnd), Math.abs(yStart - yEnd));
    }
}
