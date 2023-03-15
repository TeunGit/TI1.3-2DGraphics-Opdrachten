import java.awt.*;
import java.awt.geom.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;
    private MovingPoint[] movingPoints = new MovingPoint[4];
    private final int velocity = 1000;


    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        mainPane.resize(1920,1080);
        canvas = new ResizableCanvas(g -> draw(g), mainPane);;
        mainPane.setCenter(canvas);

        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        for (int i = 0; i < movingPoints.length; i++) {
            movingPoints[i] = new MovingPoint( canvas.getWidth()/2, canvas.getHeight()/2,(int)(360 *Math.random()),velocity);
        }
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
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        drawLines(graphics);
    }

    public void drawLines(FXGraphics2D graphics)
    {
        for (int i = 0; i < movingPoints.length; i++) {
            graphics.setColor(Color.getHSBColor(1f/movingPoints.length*i,1,1));
            if (i + 1 < movingPoints.length) {
                for (int j = 0; j < movingPoints[i].getPreviousPositions().length; j++) {
                    graphics.drawLine((int) movingPoints[i].getPreviousPositions()[j].getX(), (int) movingPoints[i].getPreviousPositions()[j].getY(), (int) movingPoints[i + 1].getPreviousPositions()[j].getX(), (int) movingPoints[i + 1].getPreviousPositions()[j].getY());
                }
                graphics.drawLine((int) movingPoints[i].getPosition().getX(), (int) movingPoints[i].getPosition().getY(), (int) movingPoints[i + 1].getPosition().getX(), (int) movingPoints[i + 1].getPosition().getY());
            } else {
                for (int j = 0; j < movingPoints[i].getPreviousPositions().length; j++) {
                    graphics.drawLine((int) movingPoints[0].getPreviousPositions()[j].getX(), (int) movingPoints[0].getPreviousPositions()[j].getY(), (int) movingPoints[i].getPreviousPositions()[j].getX(), (int) movingPoints[i].getPreviousPositions()[j].getY());
                }
                graphics.drawLine((int) movingPoints[0].getPosition().getX(), (int) movingPoints[0].getPosition().getY(), (int) movingPoints[i].getPosition().getX(), (int) movingPoints[i].getPosition().getY());
            }
        }
    }

    public void update(double deltaTime)
    {
        for (MovingPoint point: movingPoints) {
            point.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            point.checkForCollision();
            point.update(deltaTime);
        }
    }



    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }

}
