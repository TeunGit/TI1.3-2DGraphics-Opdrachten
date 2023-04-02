import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public class Catapult extends GameObject {

    public Catapult(String imageFile, Body body, Vector2 offset, double scale) {
        super(imageFile, body, offset, scale);
    }
}
