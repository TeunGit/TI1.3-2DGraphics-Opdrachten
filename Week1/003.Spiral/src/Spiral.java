import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.translate(1920/2,1080/2);
        graphics.scale(1,-1);

        graphics.setColor(Color.red);
        graphics.draw(new Line2D.Double(-1000,0,1000,0));
        graphics.setColor(Color.green);
        graphics.draw(new Line2D.Double(0,-1000,0,1000));
        graphics.setColor(Color.black);
        float resolution = (float)0.001;
        float scale = (float)50.0;
        float n = (float)0.5;
        float x = 0;
        float y = 0;
        float x2 = 0;
        float y2 = 0;
        for (float i = 0; i < 2*Math.PI ; i+=resolution) {
            graphics.draw(new Line2D.Double(x*scale,y*scale, x2*scale,y2*scale));
            x2 = n * i * (float) Math.cos(i);
            y2= n * i *(float) Math.sin(i);
            x = x2;
            y = y2;
        }

    }
    
    
    
    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
