package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.awt.*;

public class Controller {

    //public Rectangle rec;
    @FXML
    private Label label;
    @FXML
    private Rectangle rec;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        if (javaVersion.equals("23.0.2") && javafxVersion.equals("23.0.2")) {
            rec.setFill(Color.GREEN);
        }

        label.setText("JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");

    }
}
