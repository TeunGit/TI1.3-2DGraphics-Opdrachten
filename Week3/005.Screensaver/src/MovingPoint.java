import com.sun.javaws.exceptions.InvalidArgumentException;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MovingPoint {
    private double velocity = 0;
    private int direction = 0;
    private double xIncrement;
    private double yIncrement;
    private Point2D maxValues;
    private Point2D minValues;
    private Point2D position;
    private final int amountOfPreviousPoints = 25;
    private ArrayList<Point2D> previousPositions = new ArrayList<>(1);

    public MovingPoint(Point2D position, int direction, double velocity ) {
        this.direction = direction % 360;
        this.velocity = velocity;
        this.position = position;
        setDirIncrements();
    }
    public MovingPoint(double x, double y, int direction, double velocity) {
        this.direction = direction % 360;
        this.velocity = velocity;
        this.position = new Point2D.Double(x,y);
        setDirIncrements();
    }
    private void setDirIncrements(){
        this.yIncrement = Math.sin(Math.toRadians(direction)) * velocity;
        this.xIncrement = Math.cos(Math.toRadians(direction))* velocity;
    }
    public void update(){
        if(previousPositions.size() > amountOfPreviousPoints){
            previousPositions.remove(amountOfPreviousPoints);
            previousPositions.trimToSize();
        }
        position = new Point2D.Double(position.getX() + xIncrement, position.getY() + yIncrement);
        previousPositions.add(0,position);
    }
    public void update(double deltaTime) {
        if (previousPositions.size() > amountOfPreviousPoints) {
            previousPositions.remove(amountOfPreviousPoints);
            previousPositions.trimToSize();
        }
        position = new Point2D.Double(position.getX() + (xIncrement * deltaTime), position.getY() + (yIncrement * deltaTime));
        previousPositions.add(0,position);
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setDirection(int direction) {
        this.direction = direction%360;
        setDirIncrements();
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public double getVelocity() {
        return velocity;
    }

    public int getDirection() {
        return direction;
    }

    public Point2D getPosition() {
        return position;
    }
    public void setBounds(Point2D minValues, Point2D maxValues){
        this.maxValues = maxValues;
        this.minValues = minValues;
    }
    public void setBounds(double minX,double minY, double maxX, double maxY){
        this.maxValues =  new Point2D.Double(maxX,maxY);
        this.minValues = new Point2D.Double(minX,minY);
    }

    private boolean changedDirX = false;
    private boolean changedDirY = false;
    public void checkForCollision(){
        if(minValues != null || maxValues !=null) {
            if (position.getX() < minValues.getX() || position.getX() > maxValues.getX()) {
                if (!changedDirX) {
                    setDirection(-direction +180);
                    changedDirX = true;
                }
            }
            if (position.getY() < minValues.getY() || position.getY() > maxValues.getY()) {
                if (!changedDirY) {
                    setDirection(-direction);
                    changedDirY = true;
                }
            }
            if(position.getX() > minValues.getX() && position.getX() < maxValues.getX()){
                changedDirX = false;
            }
            if(position.getY() > minValues.getY()&& position.getY() < maxValues.getY()){
                changedDirY = false;

            }
        }
    }
    public Point2D[] getPreviousPositions(){
        return previousPositions.toArray(new Point2D[previousPositions.size()]);
    }

}
