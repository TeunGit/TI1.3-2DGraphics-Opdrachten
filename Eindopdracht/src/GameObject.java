import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class GameObject {


    public abstract void addToWorld();
    public abstract void draw(FXGraphics2D graphics);



}
