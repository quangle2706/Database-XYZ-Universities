package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quangle.db.xyzuniversity.models.Student;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;

public class LoginViewController {

    // This is an account of the administrator
    private final String username = "admin";
    private final String password = "password";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    public void handleLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        Connection connection = DatabaseConnector.getConnection();
        String sql = "SELECT * FROM Students WHERE Email = ? AND LoginPassword = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, enteredUsername);
        preparedStatement.setString(2, enteredPassword);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Student exists in database
            int id = resultSet.getInt("StudentID");
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");
            String address = resultSet.getString("Address");
            String phoneNumber = resultSet.getString("PhoneNumber");
            String email = resultSet.getString("Email");
            Date dob = resultSet.getDate("DateOfBirth");
            String major = resultSet.getString("Major");
            Student student = new Student(id, firstName, lastName, address, phoneNumber, email, dob, major);

            // Create new instance of StudentViewScene and pass student object
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentView.fxml"));
            Parent root = loader.load();
            StudentViewController studentViewController = loader.getController();
            studentViewController.setStudent(student);

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = primaryScreenBounds.getWidth();
            double screenHeight = primaryScreenBounds.getHeight();
            Scene studentViewScene = new Scene(root, screenWidth, screenHeight);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(studentViewScene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Student not found");
            alert.setHeaderText(null);
            alert.setContentText("Sorry! Your email and password are wrong.");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    public void handleRegister(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent, "RegisterView.fxml");
    }

    public void switchToScene(ActionEvent actionEvent, String sceneName) throws IOException {
        // Get the stage from the button
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        // Load the new FXML file
        Parent root = FXMLLoader.load(getClass().getResource(sceneName));

        // Create the new scene
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        // Set the new scene on the stage
        stage.setScene(scene);
    }

    public void openProfessorAccess(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent, "ProfessorView.fxml");
    }

    public void openDepartmentAccess(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent, "DepartmentView.fxml");
    }

    public void openCourseAccess(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent, "CourseView.fxml");
    }

    public void openAdvisorAccess(ActionEvent actionEvent) throws IOException {
        switchToScene(actionEvent, "AdvisorView.fxml");
    }

    public void openAdvisingAccess(ActionEvent actionEvent) throws IOException {
        // Advising View is to choose Advisor for new Students
        switchToScene(actionEvent, "AdvisingView.fxml");
    }
}
