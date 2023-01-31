import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spirograph extends Application {
    private TextField v1;
    private TextField v2;
    private TextField v3;
    private TextField v4;


    private boolean translated = false;
    private boolean rainbowLines = false;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(1920, 1080);
       
        VBox mainBox = new VBox();
        HBox topBar = new HBox();
        mainBox.getChildren().add(topBar);
        mainBox.getChildren().add(new Group(canvas));
        Button button= new Button("Draw");
        CheckBox checkbox = new CheckBox("Rainbow Lines");

        topBar.getChildren().add(v1 = new TextField("300"));
        topBar.getChildren().add(v2 = new TextField("1"));
        topBar.getChildren().add(v3 = new TextField("200"));
        topBar.getChildren().add(v4 = new TextField("10"));
        topBar.getChildren().add(button);
        topBar.getChildren().add(checkbox);


        button.setOnAction(event -> {
            if((float)Double.parseDouble((v1.getText()))+(float)Double.parseDouble((v3.getText()))>500){
               new Alert(Alert.AlertType.NONE,"\"Input box 1\" combined with \"input box 3\" exceeds 500", ButtonType.OK).show();
            }else {
                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }});
        checkbox.setOnAction(event -> {rainbowLines = checkbox.isSelected();});

        primaryStage.setScene(new Scene(mainBox));
        primaryStage.setTitle("Spirograph");

        primaryStage.show();;
    }
    
    
    public void draw(FXGraphics2D graphics) {
        if(!translated) {
            graphics.translate(1920 / 2, (1080/2)-40);
            translated = true;
        }
       graphics.scale(1,-1);
        float resolution = (float)0.0001;
        float scale = (float)0.5;
        //you can use Double.parseDouble(v1.getText()) to get a double value from the first textfield
        float a =(float)Double.parseDouble((v1.getText()));
        float b =(float)Double.parseDouble((v2.getText()));
        float c =(float)Double.parseDouble((v3.getText()));
        float d =(float)Double.parseDouble((v4.getText()));
        float x =(float)0;
        float y =(float)0;
        float x2 = (float)0;
        float y2 = (float)0;
        float increment = ((float)500.0/((float)Math.PI*2));

        for (float i = 0; i < 2* Math.PI ; i+= resolution) {
            if(!rainbowLines) {
                graphics.setColor(Color.getHSBColor(500.0f,1,1));
                graphics.setColor(Color.BLACK);
            }else{
                graphics.setColor(Color.getHSBColor(i * increment / 500.0f, 1, 1));

            }
            System.out.println(graphics.getColor());
            graphics.draw(new Line2D.Double(x, y, x2, y2));
            x2 = a * (float) Math.cos(b * i) + c * (float)Math.cos(d *i);
            y2 = a * (float)Math.sin(b * i) + c * (float)Math.sin(d * i);
            x = x2;
            y = y2;
        }
        //feel free to add more textfields or other controls if needed, but beware that swing components might clash in naming
    }
    
    
    
    public static void main(String[] args) {
        launch(Spirograph.class);

    }

}
