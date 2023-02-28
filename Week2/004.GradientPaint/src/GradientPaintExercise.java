import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        canvas.setOnMouseDragged(mouseEvent -> draw(new FXGraphics2D(canvas.getGraphicsContext2D())));
        primaryStage.show();

    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        Area rectangle = new Area(new Rectangle2D.Double(0,0, canvas.getWidth(),canvas.getHeight()));
        float[] fractions = {0.1f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,1};
        Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.magenta, Color.ORANGE, Color.PINK, Color.RED,Color.YELLOW};
        graphics.setPaint(new RadialGradientPaint(new Point2D.Double(canvas.getWidth()/2,canvas.getHeight()/2),500, MouseInfo.getPointerInfo().getLocation(), fractions,colors,MultipleGradientPaint.CycleMethod.REFLECT));
        graphics.fill(rectangle);
        graphics.draw(rectangle);
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
