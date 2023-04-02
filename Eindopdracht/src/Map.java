import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Map extends GameObject{
    private Body floor;
    private Body ceiling;
    private Body leftWall;
    private Body rightWall;

    private ResizableCanvas canvas;
    private World world;
    private ArrayList<Body> mapParts = new ArrayList<>();
    private double size;
    private BufferedImage image;
    public Map(ResizableCanvas canvas, BufferedImage image, World world){
        this.world = world;
        this.image = image;
        this.canvas = canvas;
        buildMap();
    }
    private void buildMap(){
        floor = new Body();
        size = canvas.getWidth();
        floor.addFixture(new BodyFixture(Geometry.createRectangle(size,20)));
        floor.getTransform().setTranslation(canvas.getWidth()/2,canvas.getHeight()-20);
        floor.setMass(MassType.INFINITE);

        ceiling = new Body();
        ceiling.addFixture(new BodyFixture(Geometry.createRectangle(size,20)));
        ceiling.getTransform().setTranslation(canvas.getWidth()/2,-10);
        ceiling.setMass(MassType.INFINITE);

        leftWall = new Body();
        leftWall.addFixture(new BodyFixture(Geometry.createRectangle(20,canvas.getHeight())));
        leftWall.getTransform().setTranslation(-10,canvas.getHeight()/2);
        leftWall.setMass(MassType.INFINITE);

        rightWall = new Body();
        rightWall.addFixture(new BodyFixture(Geometry.createRectangle(20,canvas.getHeight())));
        rightWall.getTransform().setTranslation(canvas.getWidth() + 10,canvas.getHeight()/2);
        rightWall.setMass(MassType.INFINITE);

        mapParts.add(floor);
        mapParts.add(ceiling);
        mapParts.add(leftWall);
        mapParts.add(rightWall);





    }
    public void resizeToCanvas(){

        world.removeBody(floor);
        buildMap();
        addToWorld();

    }

    @Override
    public void addToWorld() {
        for (Body b: mapParts) {
            world.addBody(b);
        }
    }

    @Override
    public void draw(FXGraphics2D graphics) {
        if(image == null){
            return;
        }
        TexturePaint floorPaint = new TexturePaint(image,new Rectangle2D.Double(0,0,image.getWidth(),image.getHeight()));
        graphics.setPaint(floorPaint);
        Area floorShape = new Area(new Rectangle2D.Double(0,this.floor.getTransform().getTranslationY()-10, size,40));
        graphics.fill(floorShape);
    }

    public double getSize(){
        return size;
    }
}
