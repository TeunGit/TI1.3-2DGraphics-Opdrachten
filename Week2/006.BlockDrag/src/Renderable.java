import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Renderable {
    private Point2D position;
    private Color color;
    private Point2D size;

    public Renderable(Point2D position,Color color, Point2D size) {
        this.position = position;
        this.color = color;
        this.size = size;


    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point2D getSize() {
        return size;
    }

    public void setSize(Point2D size) {
        this.size = size;
    }

    public Shape getShape() {
        return new Rectangle2D.Double(position.getX(),position.getY(),size.getX(),size.getY());
    }

}
