package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quangle.db.xyzuniversity.models.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UpdatingStudentViewController {

    @FXML
    private Text studentIDText;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private ComboBox<String> majorComboBox;

    private Student student;

    public void updateInformation(ActionEvent actionEvent) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        // Update student information
        String sqlUpdate = "UPDATE Students SET FirstName=?, LastName=?, Address=?, PhoneNumber=?, Email=?, DateOfBirth=?, Major=? WHERE StudentID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
        preparedStatement.setString(1, firstNameField.getText());
        preparedStatement.setString(2, lastNameField.getText());
        preparedStatement.setString(3, addressField.getText());
        preparedStatement.setString(4, phoneNumberField.getText());
        preparedStatement.setString(5, emailField.getText());
        preparedStatement.setDate(6, Date.valueOf(dobDatePicker.getValue()));
        preparedStatement.setString(7, majorComboBox.getValue());
        preparedStatement.setInt(8, Integer.parseInt(studentIDText.getText()));
        int result = preparedStatement.executeUpdate();
        if (result > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText(null);
            alert.setContentText("Your information is updated.");
            alert.showAndWait();

            // Need to update student again!
            setStudentInfoFromFXML();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("CANNOT UPDATE");
            alert.setHeaderText(null);
            alert.setContentText("Please check your information and try again.");
            alert.showAndWait();
            return;
        }
    }

    /**
     * This function will receive student from previous scene
     * and then set to its FXML UI
     * @param student
     */
    public void setStudent(Student student) {
        this.student = student;
        studentIDText.setText(Integer.toString(student.getId()));
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        addressField.setText(student.getAddress());
        phoneNumberField.setText(student.getPhoneNumber());
        emailField.setText(student.getEmail());
        dobDatePicker.setValue(student.getDateOfBirth().toLocalDate());
        majorComboBox.setValue(student.getMajor());
    }

    /**
     * Before back to previous, or go to next scene and we need an updated student.
     * This function will get student info after updated and then back to or go to next scene
     */
    public void setStudentInfoFromFXML() {
        student.setFirstName(firstNameField.getText());
        student.setLastName(lastNameField.getText());
        student.setAddress(addressField.getText());
        student.setPhoneNumber(phoneNumberField.getText());
        student.setEmail(emailField.getText());
        student.setDateOfBirth(Date.valueOf(dobDatePicker.getValue()));
        student.setMajor(majorComboBox.getValue());
        student.setId(Integer.parseInt(studentIDText.getText()));
    }

    public void backToMainScene(ActionEvent actionEvent) throws IOException, SQLException {

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
    }

}
