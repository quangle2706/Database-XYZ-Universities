package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quangle.db.xyzuniversity.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterViewController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField rePasswordField;

    @FXML
    public void handleRegister(ActionEvent actionEvent) {

        // Step 1: Check all fields are filled
        String emailTxt, passwordTxt, rePasswordTxt;
        emailTxt = emailField.getText();
        passwordTxt = passwordField.getText();
        rePasswordTxt = rePasswordField.getText();

        if (emailTxt.isEmpty() || passwordTxt.isEmpty() || rePasswordTxt.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing information");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a value in the text field.");
            alert.showAndWait();
            return;
        }

        // Step 2: Check passwords are equal
        if (!passwordTxt.equals(rePasswordTxt)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Password not match");
            alert.setHeaderText(null);
            alert.setContentText("Please re-enter the password.");
            alert.showAndWait();
            return;
        }

        // Step 3: Create connection to database
        try {
            Connection connection = DatabaseConnector.getConnection();

            // Step 4: Insert to Student table with email and password
            String sql = "INSERT INTO Students (Email, LoginPassword) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, emailTxt);
            preparedStatement.setString(2, passwordTxt);
            int rows = preparedStatement.executeUpdate();

            // Step 5: Solve few situation (existed email, lost connection ...)
            if (rows > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulation~");
                alert.setHeaderText(null);
                alert.setContentText("You are now a student of XYZ University.\nPlease login to update your information.");
                alert.showAndWait();

                switchToLoginScene(actionEvent);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) throws IOException {
        switchToLoginScene(actionEvent);
    }

    public void switchToLoginScene(ActionEvent actionEvent) throws IOException {
        // Get the stage from the button
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));

        // Create the new scene
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        // Set the new scene on the stage
        stage.setScene(scene);
    }
}
