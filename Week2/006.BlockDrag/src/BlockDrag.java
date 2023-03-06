import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    ArrayList<Renderable> renderables;
    int indexClosestRenderable;
    boolean dragging;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        renderables = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            renderables.add(new Renderable(new Point2D.Double(i*100, 50.0),
                                            Color.getHSBColor((i-1) * 50 / 500f, 1, 1),
                                            new Point2D.Double(50,50)));

        }
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.resize(1000,1000);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        for (Renderable renderable: renderables) {
            Area area = new Area(renderable.getShape());
            graphics.setColor(renderable.getColor());
            graphics.fill(area);
            graphics.draw(area);

        }
    }


    public static void main(String[] args)
    {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e)
    {
        indexClosestRenderable = 0;
        double shortestdistance= renderables.get(0).getPosition().distance(e.getX(),e.getY());
        for (Renderable renderable : renderables) {
            if(renderable.getShape().contains(e.getX(),e.getY())) {
                if (shortestdistance > renderable.getPosition().distance(e.getX(), e.getY()))
                {
                    shortestdistance = renderable.getPosition().distance(e.getX(), e.getY());
                    indexClosestRenderable = renderables.indexOf(renderable);
                }
                dragging = true;
            }
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        dragging = false;
    }

    private void mouseDragged(MouseEvent e)
    {
        if(dragging) {
            renderables.get(indexClosestRenderable).setPosition(new Point2D.Double(e.getX(), e.getY()));
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        }
    }

}
