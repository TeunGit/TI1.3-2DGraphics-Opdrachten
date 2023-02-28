import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Mirror extends Application {
    ResizableCanvas canvas;
   // AffineTransform affineTransform;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
      //  affineTransform = new AffineTransform();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(canvas.getWidth()/2,canvas.getHeight()/2);
        affineTransform.scale(1,-1);
        graphics.setTransform(affineTransform);


        graphics.setBackground(Color.white);
        graphics.setColor(Color.BLACK);
        graphics.clearRect((int)(-canvas.getWidth()/2), (int)(-canvas.getHeight()/2), (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.drawLine((int)(-canvas.getWidth()/2),0,(int)(canvas.getWidth()/2),0);
        graphics.drawLine(0,(int)(-canvas.getHeight()/2),0,(int)(canvas.getHeight()/2));
        graphics.drawLine((int)(-canvas.getWidth()/2),(int)(-2.5* (canvas.getWidth()/2)),(int)(canvas.getWidth()/2),(int)(2.5* (canvas.getWidth()/2)));
        Area rectangle = new Area(new Rectangle2D.Double(-50,200,100,100));
        graphics.setColor(Color.red);
        graphics.fill(rectangle);
        graphics.draw(rectangle);
        AffineTransform tx = new AffineTransform((2/(1+Math.pow(2.5,2))-1),(2*2.5/(1+Math.pow(2.5,2))),(2*2.5/(1+Math.pow(2.5,2))),(2*Math.pow(2.5,2)/(1+Math.pow(2.5,2)))-1,0,0);
        graphics.draw(tx.createTransformedShape(rectangle));


    }


    public static void main(String[] args)
    {
        launch(Mirror.class);
    }

}
