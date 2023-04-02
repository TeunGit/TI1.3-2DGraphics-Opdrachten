import com.sun.javafx.geom.TransformedShape;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Car extends GameObject {
    private double size;
    private Point2D position;
    private boolean hasJump;
    private World world;
    private BufferedImage image;
    private Boolean facingLeft = false;

    public Car(double xPos, double yPos, double size, BufferedImage image, World world){
        this.world = world;
        this.position = new Point2D.Double(xPos,yPos);
        this.image = image;
        this.size = size;
        buildCar();
    }


    private Body jumpSensor;
    private Body carBody;

    private void buildCar(){
        jumpSensor = new Body();
        BodyFixture sensor = new BodyFixture(Geometry.createRectangle(0.5*(2*size),2));
        sensor.setSensor(true);

        jumpSensor.addFixture(sensor);
        jumpSensor.getTransform().setTranslation(new Vector2(position.getX(),position.getY()+(size/2)));
        jumpSensor.setAutoSleepingEnabled(false);
        jumpSensor.setMass(MassType.NORMAL);

        carBody = new Body();
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(2*size,size));
        fixture.setRestitution(0);
        fixture.setFriction(5);
        carBody.addFixture(fixture);
        carBody.getTransform().setTranslation(new Vector2(position.getX(),position.getY()));
        Mass mass = new Mass(new Vector2(carBody.getTransform().getTranslationX(),carBody.getTransform().getTranslationY()),200,0);
        carBody.setMass(mass);

        carBody.setGravityScale(30);


        carBody.setAutoSleepingEnabled(false);



    }
        @Override
    public void addToWorld(){
        world.addBody(carBody);
        world.addBody(jumpSensor);
        world.addJoint(new WeldJoint(carBody, jumpSensor,new Vector2(0,0)));
    }
    public void setfacingLeft(Boolean facingLeft){
        this.facingLeft = facingLeft;
    }
    @Override
    public void draw(FXGraphics2D graphics) {
        if(image == null){
            return;
        }
        AffineTransform tx = new AffineTransform();
        tx.translate(carBody.getTransform().getTranslationX(), carBody.getTransform().getTranslationY());
        tx.rotate(carBody.getTransform().getRotation());
        if(facingLeft) {
            tx.scale((size * 2) / image.getWidth() * 1.5, size / image.getHeight() * 1.5);
        }else {
            tx.scale(-(size * 2) / image.getWidth() * 1.5, size / image.getHeight() * 1.5);
        }
        tx.translate(-image.getWidth()/2, -image.getHeight()/2);
        if(!boostParticles.isEmpty()){
            graphics.setColor(Color.ORANGE);
            for (Shape particle: boostParticles) {
                graphics.fill(particle);
            }
        }
        graphics.drawImage(image,tx,null);
    }
    private LinkedList<Shape> boostParticles = new LinkedList<>();
    public void updateBoost(boolean boosting){
        if(boosting) {
            AffineTransform tx = new AffineTransform();
            tx.translate(carBody.getTransform().getTranslationX(), carBody.getTransform().getTranslationY());

            tx.rotate(carBody.getTransform().getRotation());
            tx.translate(-15,-15);
            Shape boostparticle  = new Ellipse2D.Double(0,0, 30, 30);
            boostParticles.add(tx.createTransformedShape(boostparticle));
            if (boostParticles.size() > 60) {
                boostParticles.removeFirst();
            }
        }else{
            if (!boostParticles.isEmpty())
            boostParticles.removeFirst();
        }
    }



    public Body getBody(){
        return carBody;
    }
    public boolean inAir(){
        return jumpSensor.getContacts(true).isEmpty();
    }



}
