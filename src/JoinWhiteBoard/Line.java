import java.awt.*;

public class Line extends Shape {

    public Line(int xStart, int yStart, int xEnd, int yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawLine(xStart, yStart, xEnd, yEnd);
    }
}
