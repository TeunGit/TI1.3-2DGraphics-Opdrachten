import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.event.EventTarget;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private BufferedImage[] tiles;



    @Override
    public void start(Stage stage) throws Exception
    {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("/images/sprite.png"));
            tiles = new BufferedImage[65];
            //knip de afbeelding op in 24 stukjes van 32x32 pixels.
            for(int i = 0; i < 65; i++) {
                tiles[i] = image.getSubimage(64 * (i%8),64*(i/8),64,64);

            }
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
        }.start();;
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Moving Character");
        stage.show();
        canvas.setOnMouseClicked(e -> onMouseClick(e));
        draw(g2d);
    }

    int frame;
    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(0,canvas.getHeight()/2);
        affineTransform.translate(positionx,positiony);
        graphics.drawImage(tiles[frame],affineTransform,null );

    }
    double positionx = 0;
    double positiony = 0;
    boolean jumping = false;
    boolean falling = false;
    public void update(double deltaTime)
    {

        if(jumping){
            if(positiony <= -100){
                falling = true;
            }
            if(!falling && positiony > -100) {
                positiony -= 1;
                frame = 40+(-1*(int) -positiony/(100/5))%5;
            }else if(positiony < 0 && falling){
                positiony += 1;
                frame = 44+(-1*(int)positiony/(100/3))%3;
            }
            if(positiony == 0){
                jumping = false;
                falling = false;
            }

        }else{
            if(positionx + 64 >= canvas.getWidth()){
                positionx = 0;
            }else{
                positionx += 1;
            }
            frame = 32+((int)positionx/10)%8 ;
        }


    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }
    private void onMouseClick(MouseEvent ev){
        jumping = true;
    }

}
