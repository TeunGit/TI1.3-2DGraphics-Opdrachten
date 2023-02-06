import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;
    private ToggleGroup colorGroup;



    private boolean translated = false;

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
        BorderPane borderPane = new BorderPane();

        VBox sideBar = new VBox();
        HBox topBar = new HBox();

        borderPane.setTop(topBar);
        borderPane.setLeft(sideBar);
        borderPane.setCenter(new Group(canvas));


        Label colorLabel = new Label("Line Color;");

        colorGroup = new ToggleGroup();
        RadioButton black = new RadioButton("BLACK");
        black.setToggleGroup(colorGroup);
        black.setSelected(true);
        RadioButton red = new RadioButton("RED");
        red.setToggleGroup(colorGroup);
        RadioButton green = new RadioButton("GREEN");
        green.setToggleGroup(colorGroup);
        RadioButton blue = new RadioButton("BLUE");
        blue.setToggleGroup(colorGroup);
        RadioButton orange = new RadioButton("ORANGE");
        orange.setToggleGroup(colorGroup);
        RadioButton magenta = new RadioButton("MAGENTA");
        magenta.setToggleGroup(colorGroup);
        RadioButton rainbow = new RadioButton("RAINBOW");
        rainbow.setToggleGroup(colorGroup);
        sideBar.getChildren().addAll(colorLabel, black, red, green, blue, orange, magenta, rainbow);


        Button draw = new Button("Draw");
        Button clear = new Button("Clear");


        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("200"));
        topBar.getChildren().add(v4 = new TextField("10"));
        topBar.getChildren().add(draw);
        topBar.getChildren().add(clear);


        draw.setOnAction(event -> {
            if((float)Double.parseDouble((v1.getText()))+(float)Double.parseDouble((v3.getText()))>500){
               new Alert(Alert.AlertType.NONE,"\"Input box 1\" combined with \"input box 3\" exceeds 500", ButtonType.OK).show();
            }else {
                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }});
        clear.setOnAction(event -> {canvas.getGraphicsContext2D().clearRect(-960,-540,1920,1080);});


        primaryStage.setScene(new Scene(borderPane));

        primaryStage.setTitle("Spirograph");
        primaryStage.show();
    }
    


    public void draw(FXGraphics2D graphics) {

        if(!translated) {
            graphics.translate(1920 / 2, (1080/2)-40);
            translated = true;
        }
       graphics.scale(1,-1);
        float resolution = (float)0.001;

        //you can use Double.parseDouble(v1.getText()) to get a double value from the first textfield
        float a =(float)Double.parseDouble((v1.getText()));
        float b =(float)Double.parseDouble((v2.getText()));
        float c =(float)Double.parseDouble((v3.getText()));
        float d =(float)Double.parseDouble((v4.getText()));
        float x = (float)(a * Math.cos(b * 0) + c * Math.cos(d *0));
        float y = (float)(a * Math.sin(b * 0) + c * Math.sin(d * 0));
        float x2;
        float y2;
        float increment = ((float)500.0/((float)Math.PI*2));
        graphics.setColor(Color.getHSBColor(500.0f,1,1));
        graphics.setColor(getButtonColor());

        for (float i = 0; i < 2* Math.PI ; i+= resolution) {
            if(((RadioButton)colorGroup.getSelectedToggle()).getText().equals("RAINBOW")) {
                graphics.setColor(Color.getHSBColor(i * increment / 500.0f, 1, 1));
            }
            x2 = (float)((a * Math.cos(b * i)) + (c * Math.cos(d *i)));
            y2 = (float)((a * Math.sin(b * i)) + (c * Math.sin(d * i)));
            graphics.draw(new Line2D.Double(x, y, x2, y2));
            x = x2;
            y = y2;
        }
        //feel free to add more textfields or other controls if needed, but beware that swing components might clash in naming
    }
    private Color getButtonColor(){
        if(((RadioButton)colorGroup.getSelectedToggle()).getText().equalsIgnoreCase("black")) {
            return Color.BLACK;
        }
        if(((RadioButton)colorGroup.getSelectedToggle()).getText().equalsIgnoreCase("red")) {
            return Color.RED;
        }
        if(((RadioButton)colorGroup.getSelectedToggle()).getText().equalsIgnoreCase("green")) {
            return Color.GREEN;
        }
        if(((RadioButton)colorGroup.getSelectedToggle()).getText().equalsIgnoreCase("blue")) {
            return Color.BLUE;
        }
        if(((RadioButton)colorGroup.getSelectedToggle()).getText().equalsIgnoreCase("orange")) {
            return Color.ORANGE;
        }
        if(((RadioButton)colorGroup.getSelectedToggle()).getText().equalsIgnoreCase("magenta")) {
            return Color.MAGENTA;
        }
        return Color.BLACK;
    }
    
    
    
    public static void main(String[] args) {
        launch(Spirograph.class);

    }

}
