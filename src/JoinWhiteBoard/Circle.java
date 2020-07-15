import java.awt.*;

public class Circle extends Shape {

    public Circle(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawOval(Math.min(xStart, xEnd), Math.min(yStart, yEnd), Math.max(Math.abs(xStart - xEnd),
                Math.abs(yStart - yEnd)), Math.max(Math.abs(xStart - xEnd), Math.abs(yStart - yEnd)));
    }
}
