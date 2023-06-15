package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quangle.db.xyzuniversity.models.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentViewController {

    @FXML
    private Text studentIdText;

    @FXML
    private Text firstNameText;

    @FXML
    private Text lastNameText;

    @FXML
    private Text addressText;

    @FXML
    private Text phoneNumberText;

    @FXML
    private Text emailText;

    @FXML
    private Text dobText;

    @FXML
    private Text majorText;

    @FXML
    private Text advisorText;

    private Student student;

    public void setStudent(Student student) throws SQLException {
        this.student = student;
        //System.out.println("Hello " + student);
        studentIdText.setText(Integer.toString(student.getId()));
        firstNameText.setText(student.getFirstName());
        lastNameText.setText(student.getLastName());
        addressText.setText(student.getAddress());
        phoneNumberText.setText(student.getPhoneNumber());
        emailText.setText(student.getEmail());
        dobText.setText(student.getDateOfBirth().toString());
        majorText.setText(student.getMajor());
        updateAdvisorName();
    }

    private void updateAdvisorName() throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        String sql = "SELECT Advisors.FirstName, Advisors.LastName FROM Advisors " +
                "JOIN Advising ON Advisors.AdvisorID = Advising.AdvisorID " +
                "WHERE Advising.StudentID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, student.getId());
        ResultSet rs = preparedStatement.executeQuery();
        String name = "";
        if (rs.next()) {
            name = rs.getString("FirstName") + " " + rs.getString("LastName");
        }
        advisorText.setText(name);
    }

    public void updateInformation(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatingStudentView.fxml"));
        Parent root = loader.load();
        UpdatingStudentViewController updatingStudentViewController = loader.getController();
        updatingStudentViewController.setStudent(student);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene updatingStudentViewScene = new Scene(root, screenWidth, screenHeight);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(updatingStudentViewScene);
        stage.show();
    }

    public void manageYourCourses(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageClassesView.fxml"));
        Parent root = loader.load();
        ManageClassesViewController manageClassesViewController = loader.getController();
        manageClassesViewController.setStudent(student);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene manageClassesScene = new Scene(root, screenWidth, screenHeight);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(manageClassesScene);
        stage.show();
    }

    public void seeYourSchedule(ActionEvent actionEvent) throws IOException {

        // Create new instance of StudentViewScene and pass student object
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentCourseInformationView.fxml"));
        Parent root = loader.load();
        StudentCourseInformationViewController studentCourseInformationController = loader.getController();
        studentCourseInformationController.setStudent(student);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene studentViewScene = new Scene(root, screenWidth, screenHeight);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(studentViewScene);
        stage.show();

    }

    public void logOutYourAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
        Parent root = loader.load();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        Scene loginScene = new Scene(root, screenWidth, screenHeight);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(loginScene);
        stage.show();
    }
}
