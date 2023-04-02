import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Ball extends GameObject{
    private Point2D position;
    private double size;
    private Body ball;
    private World world;
    private BufferedImage image;
    public Ball(double xPos, double yPos, double size,BufferedImage image, World world){
        this.position = new Point2D.Double(xPos,yPos);
        this.size = size;
        this.image = image;
        this.world =world;
        makeBall();
    }
    private void makeBall(){
        ball = new Body();
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(size));
        fixture.setRestitution(0.7);
        ball.addFixture(fixture);
        ball.getTransform().setTranslation(position.getX(),position.getY());
        ball.setMass(new Mass(new Vector2(position.getX(),position.getY()),10,0));
        ball.setGravityScale(20);
    }

    @Override
    public void addToWorld() {
        world.addBody(ball);
    }

    @Override
    public void draw(FXGraphics2D graphics) {
        if(image == null){
            return;
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(ball.getTransform().getTranslationX(), ball.getTransform().getTranslationY());
        tx.rotate(ball.getTransform().getRotation());
        tx.scale(size*2/image.getWidth(), size*2/image.getHeight());
        tx.translate(-image.getWidth()/2, -image.getHeight()/2);

        graphics.drawImage(image,tx,null);
    }

}
