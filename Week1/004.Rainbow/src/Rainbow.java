import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;

import static javafx.application.Application.launch;

public class Rainbow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        graphics.translate(1920/2,1000);
        graphics.scale(1,-1);

        float resolution = (float)0.001;
        float radiusBinnen = (float) 600;
        float radiusBuiten = (float) 700;
        float increment = ((float)500.0/((float)Math.PI));
        for(float i = 0; i < Math.PI; i+= resolution) {

            graphics.setColor(Color.getHSBColor(i*increment/500.0f, 1, 1));
            float x1 = radiusBinnen * (float)Math.cos(i);
            float y1 = radiusBinnen * (float)Math.sin(i);
            float x2 = radiusBuiten * (float)Math.cos(i);
            float y2 = radiusBuiten * (float)Math.sin(i);
            graphics.draw(new Line2D.Double(x1,y1,x2,y2));
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
