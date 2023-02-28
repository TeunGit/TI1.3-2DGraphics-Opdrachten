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

public class Rainbow extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Rainbow");
        stage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(canvas.getWidth()/2,canvas.getHeight()/2);
        affineTransform.scale(1,-1);


        String word = "regenboog";
        char[] chars = new char[1];

        double angle = Math.PI/word.length();
        float colorIncrement = 500f/word.length();
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        Font font = new Font("ariel",Font.PLAIN,30);

        for (int i = 0; i < word.length(); i++) {

            affineTransform = new AffineTransform();
            affineTransform.translate(canvas.getWidth()/2,canvas.getHeight()/2);
            affineTransform.rotate((angle*i )+(Math.PI/2) );
            affineTransform.translate(0,50);
            affineTransform.scale(-1,-1);
            chars[0] = word.charAt(i);
            Shape letter = font.createGlyphVector(graphics.getFontRenderContext(),chars).getOutline();

            graphics.setColor(Color.getHSBColor((500f/i*colorIncrement),1,1));
            graphics.fill(affineTransform.createTransformedShape(letter));





        }
    }


    public static void main(String[] args)
    {
        launch(Rainbow.class);
    }

}
