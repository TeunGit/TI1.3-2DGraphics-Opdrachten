import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] tiles;
    private BufferedImage currentFrame;
    private AffineTransform affineTransform = new AffineTransform();
    private int framecounter= 0;


    @Override
    public void start(Stage stage) throws Exception
    {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("Week3/002.MovingCharacter/resources/images/sprite.png");
            tiles = new BufferedImage[65];
            //knip de afbeelding op in 24 stukjes van 32x32 pixels.
            for(int i = 0; i < 24; i++)
                tiles[i] = image.getSubimage(32 * (i%6), 32 * (i/6), 32, 32);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        affineTransform.translate(0,canvas.getHeight()/2);


        graphics.drawImage(currentFrame,affineTransform,null );

    }


    public void update(double deltaTime)
    {

        affineTransform.translate(10,0);
        currentFrame = tiles[framecounter];
        if(framecounter < tiles.length) {
            framecounter++;
        }else{
            framecounter = 0;
        }

    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }

}
