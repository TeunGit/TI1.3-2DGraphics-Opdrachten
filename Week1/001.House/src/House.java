import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Line2D;

public class House extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        //walls
        graphics.draw(new Line2D.Double(100,1000 , 1000, 1000));
        graphics.draw(new Line2D.Double(100, 500, 100, 1000));
        graphics.draw(new Line2D.Double(1000, 500, 1000, 1000));
        //roof
        graphics.draw(new Line2D.Double(100, 500, 550, 100));
        graphics.draw(new Line2D.Double(1000, 500, 550, 100));
        //door
        graphics.draw(new Line2D.Double(200, 1000, 200, 700));
        graphics.draw(new Line2D.Double(400, 1000, 400, 700));
        graphics.draw(new Line2D.Double(200, 700, 400, 700));
        //window
        graphics.draw(new Line2D.Double(500, 600, 900, 600));
        graphics.draw(new Line2D.Double(500, 900, 900, 900));
        graphics.draw(new Line2D.Double(500, 600, 500, 900));
        graphics.draw(new Line2D.Double(900, 600, 900, 900));
    }



    public static void main(String[] args) {
        launch(House.class);
    }

}
