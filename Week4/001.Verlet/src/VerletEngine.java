import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class VerletEngine extends Application implements Serializable {

    private ResizableCanvas canvas;
    private java.util.ArrayList<Particle> particles = new java.util.ArrayList<>();
    private java.util.ArrayList<Constraint> constraints = new java.util.ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);
    private Saver saver = new Saver();
    private boolean paused;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        Button cloth = new Button("generate Cloth");
        Button load = new Button("Load");
        Button save = new Button("save");
        HBox hBox = new HBox();
        hBox.getChildren().add(cloth);
        hBox.getChildren().add(load);
        hBox.getChildren().add(save);
        mainPane.setTop(hBox);
        cloth.setOnAction(event -> generateCloth());
        load.setOnAction(event -> loadParticles());
        save.setOnAction(event -> saveParticles());
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                if(!paused) {
                    update((now - last) / 1000000000.0);
                }
                last = now;
                draw(g2d);
            }
        }.start();

        // Mouse Events
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Verlet Engine");
        stage.show();
        draw(g2d);
    }

    private void saveParticles() {
        saver.setPath("Week4/001.Verlet/resources/constraints.txt");
        ArrayList<Particle[]> particleConnections = new ArrayList<>();
        for (Constraint c : constraints ) {
            particleConnections.add(c.getConnectedParticles());
        }
        saver.save(particleConnections);

    }

    private void loadParticles() {
        saver.setPath("Week4/001.Verlet/resources/constraints.txt");
        particles.clear();
        constraints.clear();
        System.out.println(saver.load());
        ArrayList<Particle[]> tempArray = saver.load();
        for ( Particle[] connectedParticles : tempArray) {
            for (Particle particle: connectedParticles) {
                if(particle != null && !particles.contains(particle))
                    particles.add(particle);
            }
            if(connectedParticles.length == 1) {
                constraints.add(new PositionConstraint(connectedParticles[0]));
            }else if(connectedParticles.length == 2){
                constraints.add(new DistanceConstraint(connectedParticles[0],connectedParticles[1]));
            }else{
                constraints.add(new RopeConstraint(particles.get(particles.indexOf(connectedParticles[0])),particles.get(particles.indexOf(connectedParticles[1]))));
            }
        }
        paused = !paused;
    }

    private void generateCloth() {
        paused = true;
        int clothWidth = 10;
        int clothHeight = 10;
        particles.clear();
        constraints.clear();
        for (int i = 0; i < clothWidth*clothHeight; i++) {
            particles.add(new Particle(new Point2D.Double(100 + 50 * (i%clothWidth), (100 + 50*(i/clothWidth)))));
        }
        for (int i = 0; i < clothHeight; i++) {
            for (int j = 0; j < clothWidth; j++) {
                if(i == 0){
                    constraints.add(new PositionConstraint(particles.get(j)));
                }
                if(j < clothWidth-1) {
                    constraints.add(new DistanceConstraint(particles.get(j + clothWidth * i), particles.get(j + 1 + clothWidth * i)));
                }
                if(i < clothHeight-1) {
                    constraints.add(new DistanceConstraint(particles.get(j + clothWidth *i),particles.get(j + clothWidth * (i+1))));
                }
            }
        }

    }

    public void init() {
        paused = true;
        for (int i = 0; i < 20; i++) {
            particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        }

        for (int i = 0; i < 10; i++) {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));
        }

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint c : constraints) {
            c.draw(graphics);
        }

        for (Particle p : particles) {
            p.draw(graphics);
        }
    }

    private void update(double deltaTime) {
        for (Particle p : particles) {
            p.update((int) canvas.getWidth(), (int) canvas.getHeight());
        }
            for (Constraint c : constraints) {
                c.satisfy();

        }
    }

    private void mouseClicked(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        Particle newParticle = new Particle(mousePosition);
        particles.add(newParticle);
        if(e.getButton() == MouseButton.PRIMARY) {
            if(e.isControlDown() ){
                constraints.add(new PositionConstraint(newParticle));
            }else {
                constraints.add(new DistanceConstraint(newParticle, nearest));
            }
        }

        if (e.getButton() == MouseButton.SECONDARY) {
            java.util.ArrayList<Particle> sorted = new java.util.ArrayList<>();
            sorted.addAll(particles);

            //sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
            Collections.sort(sorted, new Comparator<Particle>() {
                @Override
                public int compare(Particle o1, Particle o2) {
                    return (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition));
                }
            });
            if(e.isShiftDown()){
                if(particles.size() > 1) {
                    particles.remove(particles.size() - 1);
                    constraints.add(new DistanceConstraint(particles.get(particles.indexOf(sorted.get(1))), particles.get(particles.indexOf(sorted.get(2)))));
                }
            }else if(e.isControlDown()){
                constraints.add(new DistanceConstraint(newParticle,sorted.get(1),100));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2),100));
            }else {
                constraints.add(new DistanceConstraint(newParticle,sorted.get(1)));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
            }
        } else if (e.getButton() == MouseButton.MIDDLE) {
            // Reset
            particles.clear();
            constraints.clear();
            init();
        }
    }

    private Particle getNearest(Point2D point) {
        Particle nearest = particles.get(0);
        for (Particle p : particles) {
            if (p.getPosition().distance(point) < nearest.getPosition().distance(point)) {
                nearest = p;
            }
        }
        return nearest;
    }

    private void mousePressed(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10) {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e) {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e) {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    public static void main(String[] args) {
        launch(VerletEngine.class);
    }

    public java.util.ArrayList<Particle> getParticles() {
        return particles;
    }

    public java.util.ArrayList<Constraint> getConstraints() {
        return constraints;
    }
}
