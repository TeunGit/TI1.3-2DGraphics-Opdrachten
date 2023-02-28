import java.awt.*;
import java.awt.geom.*;

import com.sun.glass.ui.Size;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class YingYang extends Application {
    private ResizableCanvas canvas;
    final private double X =250;
    final private double Y = 150;
    final private double SIZE = 100;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.scale(1,-1);
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        //width 200/2 = 100 | 50/2 = 25 250 + 100 - 25 = 325
        Area mainCircle = new Area(new Ellipse2D.Double(X,Y,SIZE,SIZE));
        Area whiteCircle = new Area(new Ellipse2D.Double(X +(SIZE/4),Y,SIZE/2,SIZE/2));
        Area halfCircle = new Area(new Area(new Rectangle2D.Double(X+(SIZE/2),Y,SIZE/2,SIZE)));
        Area darkCircle = new Area(new Area(new Ellipse2D.Double(X+(SIZE/4),Y+(SIZE/2),SIZE/2,SIZE/2)));
        Area whiteDot = new Area(new Ellipse2D.Double(X +(SIZE/16*7),Y + (SIZE/16*11),SIZE/8,SIZE/8));
        Area blackDot = new Area(new Ellipse2D.Double(X +(SIZE/16*7),Y + (SIZE/16*3),SIZE/8,SIZE/8));
        halfCircle.intersect(mainCircle);
        graphics.setColor(Color.BLACK);
        graphics.fill(halfCircle);
        graphics.draw(halfCircle);
        graphics.fill(darkCircle);
        graphics.draw(darkCircle);

        graphics.setColor(Color.white);
        graphics.fill(whiteCircle);
        graphics.draw(whiteCircle);

        graphics.fill(whiteDot);
        graphics.draw(whiteDot);
        graphics.setColor(Color.BLACK);
        graphics.fill(blackDot);
        graphics.draw(blackDot);
        graphics.draw(mainCircle);

    }


    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
