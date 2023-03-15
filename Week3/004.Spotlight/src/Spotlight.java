import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private double mouseX;
    private double mouseY;
    private BufferedImage image;
    @Override
    public void start(Stage stage) throws Exception
    {
        try {
            image = ImageIO.read(getClass().getResource("pepe-the-frog.jpg"));
        }catch(IOException e){
            e.printStackTrace();
        }
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        mouseX = canvas.getWidth()/2;
        mouseY = canvas.getHeight()/2;
        canvas.setOnMouseMoved(e -> mouseMoved(e));
        canvas.setOnMouseClicked(e -> mouseClicked(e));
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
        stage.setTitle("Spotlight");
        stage.show();
        draw(g2d);
    }

    private void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    private boolean imageAsBackground = false;
    private void mouseClicked(MouseEvent e){
        imageAsBackground = !imageAsBackground;
    }



    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        Shape spotlightShape = new Ellipse2D.Double(mouseX - 100, mouseY - 100, 200, 200);

        graphics.draw(spotlightShape);
        graphics.setClip(spotlightShape);
        if (imageAsBackground) {
            AffineTransform tx = new AffineTransform();
            tx.scale(canvas.getWidth()/image.getWidth(),canvas.getHeight()/image.getHeight());
            graphics.drawImage(image, tx, null);
        } else {
            for (int i = 0; i < 100; i++) {
                graphics.setPaint(Color.getHSBColor((float) (500f * Math.random()), 1, 1));
                graphics.drawLine((int) (canvas.getWidth() * Math.random()), (int) (canvas.getHeight() * Math.random()), (int) (canvas.getWidth() * Math.random()), (int) (canvas.getHeight() * Math.random()));
            }
        }
        graphics.setClip(null);
    }



    public void update(double deltaTime)
    {

    }

    public static void main(String[] args)
    {
        launch(Spotlight.class);
    }

}
