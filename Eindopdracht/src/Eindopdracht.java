import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Force;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Eindopdracht extends Application {
    private ResizableCanvas canvas;
    private Settings settings = new Settings();
    private World world;
    private Map map;
    private boolean onDebugDraw;
    private boolean canvasSet;
    private KeyCode pressedKey;
    private boolean jumped;
    private ArrayList<GameObject> gameObjects;
    private BufferedImage backgroundImage;

    @Override
    public void start(Stage stage) throws Exception {
        backgroundImage = ImageIO.read(getClass().getResource("/background.png"));
        canvasSet = false;
        BorderPane mainPane = new BorderPane();
        mainPane.resize(1920,991);
        CheckBox debugDraw = new CheckBox("debugDraw");
        debugDraw.setFocusTraversable(false);
        debugDraw.setOnAction(e ->{this.onDebugDraw = debugDraw.isSelected();});
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.resize(1920,991);
        setup();
        canvasSet = true;
        VBox vBox = new VBox();
        vBox.getChildren().add(debugDraw);
        vBox.getChildren().add(canvas);
        mainPane.setCenter(vBox);

        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        canvas.setFocusTraversable(true);
        canvas.setOnKeyReleased(event -> onKeyRelease(event.getCode()));
        canvas.setOnKeyPressed(event -> onKeyPress(event.getCode()) );

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


        stage.setMaximized(true);
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }

    private boolean keyWPressed;
    private boolean keySPressed;
    private boolean keyAPressed;
    private boolean keyDPressed;
    private void onKeyRelease(KeyCode key){
        if(key.equals(KeyCode.W)){
            keyWPressed = false;
            jumped = false;
        }
        if(key.equals(KeyCode.S)){
            keySPressed = false;

        }
        if(key.equals(KeyCode.A)){
            keyAPressed = false;

        }
        if(key.equals(KeyCode.D)){
            keyDPressed = false;
        }
    }
    private void onKeyPress(KeyCode key){
        if(key.equals(KeyCode.W)){
            keyWPressed = true;
        }
        if(key.equals(KeyCode.S)){
            keySPressed = true;
        }
        if(key.equals(KeyCode.A)){
            keyAPressed = true;
        }
        if(key.equals(KeyCode.D)){
            keyDPressed = true;
        }
    }
    private void setup(){
        gameObjects = new ArrayList<>();
        BufferedImage carImage;
        BufferedImage ballImage;
        BufferedImage mapImage;
        try{
            carImage = ImageIO.read(getClass().getResource("/spr_rally_0.png"));
            ballImage = ImageIO.read(getClass().getResource("/SoccerBall.png"));
            mapImage = ImageIO.read(getClass().getResource("/floor.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pressedKey = null;
        world = new World();
        world.setGravity(new Vector2(0,19.81));
        settings.setMaximumTranslation(100.0);
        world.setSettings(settings);
        car = new Car(canvas.getWidth()/2,canvas.getHeight()-100,60,carImage,world);
        map = new Map(canvas,mapImage,world);
        ball = new Ball(canvas.getWidth()/2,canvas.getHeight()/2,90,ballImage,world);
        gameObjects.add(map);
        gameObjects.add(ball);
        gameObjects.add(car);
        ball.addToWorld();
        car.addToWorld();
        map.addToWorld();
    }
    private Car car;
    private Ball ball;

    private void update(double deltaTime){
        moveCar();
        car.updateBoost(keySPressed);
        world.update(deltaTime);

    }

    private int jumpsLeft = 1;

    private boolean facingLeft;
    private boolean canRotate = false;
    private int boostSpeed = 250;
    public void moveCar(){
        Body carBody = car.getBody();
        double rotation = carBody.getTransform().getRotation();
        double xVelocity = carBody.getLinearVelocity().x;
        double yVelocity = carBody.getLinearVelocity().y;

        if(carBody.getTransform().getTranslationY() > canvas.getHeight()- 65) {
            rotation=0;
            canRotate = false;
        }else if(carBody.getTransform().getTranslationY() < canvas.getHeight() - 70){
            canRotate = true;
        }
        if (keySPressed){
            if(boostSpeed < 500){
                boostSpeed += 10;
            }
            if(!facingLeft){
                xVelocity = boostSpeed* Math.cos(rotation);
                yVelocity = boostSpeed* Math.sin(rotation);

            }else{
                xVelocity = -boostSpeed* Math.cos(rotation);
                yVelocity = -boostSpeed* Math.sin(rotation);
            }
        }else{
            if (boostSpeed > 250) {
                boostSpeed -= 10;
            }
        }
        if(car.inAir()) {
            if(jumpsLeft < 2&& canRotate) {
                if (keyAPressed) {
                    rotation -= 0.03;
                }
                if (keyDPressed) {
                    rotation += 0.03;
                }
                if (keyWPressed) {
                    if (!jumped && jumpsLeft != 0) {
                        xVelocity = xVelocity+ 400* Math.sin(rotation);
                        yVelocity = -500* Math.cos(rotation);
                        jumpsLeft = 0;
                    }
                }

            }
        }else{

            jumped = false;
            jumpsLeft = 1;
            if (keyAPressed) {
                xVelocity = -250;
                facingLeft = true;

                //  carBody.getTransform().setTranslation(carBody.getTransform().getTranslationX() - 1, carBody.getTransform().getTranslationY());
            }
            if (keyDPressed) {
                xVelocity = 250;
                facingLeft = false;

            }
            if (keyWPressed) {
                if (!jumped) {
                    yVelocity = -400;
                    jumped = true;


                }
            }

         }
        car.setfacingLeft(facingLeft);
        carBody.setLinearVelocity(xVelocity,yVelocity);
        carBody.getTransform().setRotation(rotation);

    }
    private void draw(FXGraphics2D g2d) {
        if(canvasSet) {
            g2d.setTransform(new AffineTransform());
            g2d.setBackground(Color.white);
            AffineTransform backgroundTransform = new AffineTransform();
            backgroundTransform.scale(canvas.getWidth()/backgroundImage.getWidth(),canvas.getHeight()/backgroundImage.getHeight());
            g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
            g2d.drawImage(backgroundImage,backgroundTransform,null);

            for (GameObject object: gameObjects) {
                object.draw(g2d);
            }
            if (onDebugDraw) {
                g2d.setColor(Color.RED);
                DebugDraw.draw(g2d, world, 1);
            }
        }
    }

    public static void main(String[] args) {
        launch(Eindopdracht.class);

    }
//    public void unStuck(Car car){
//        car.getBody().getTransform().setTranslation(car.getBody().getTransform().getTranslationX(),car.getBody().getTransform().getTranslationY() -10);
//    }
}



