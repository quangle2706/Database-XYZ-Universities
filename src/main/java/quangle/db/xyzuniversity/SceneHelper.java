package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHelper {

    public static void switchToScene(ActionEvent actionEvent, String sceneName) throws IOException {
        // Get the stage from the button
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(SceneHelper.class.getResource(sceneName));

        // Create the new scene
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        // Set the new scene on the stage
        stage.setScene(scene);
    }

}
