package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdvisingViewController implements Initializable {

    @FXML
    private ComboBox advisorComboBox;

    @FXML
    private ComboBox studentComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            displayAdvisors();
            displayStudentWithAdvisor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayAdvisors() throws SQLException {

        List<Integer> advisorIDs = new ArrayList<>();

        Connection connection = DatabaseConnector.getConnection();
        String sql = "SELECT AdvisorID FROM Advisors";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            advisorIDs.add(rs.getInt("AdvisorID"));
        }
        advisorComboBox.getItems().addAll(advisorIDs);
    }

    private void displayStudentWithAdvisor() throws SQLException {

        List<Integer> studentIDs = new ArrayList<>();

        Connection connection = DatabaseConnector.getConnection();
        String sql = "SELECT Students.StudentID FROM Students " +
                "LEFT JOIN Advising ON Students.StudentID = Advising.StudentID " +
                "WHERE Advising.StudentID IS NULL";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            studentIDs.add(rs.getInt("StudentID"));
        }
        studentComboBox.getItems().addAll(studentIDs);
    }

    public void connectAdvisorToStudent(ActionEvent actionEvent) throws SQLException {

        if (studentComboBox.getValue() == null || advisorComboBox.getValue() == null) {
            return;
        }

        // Insert to Advising table
        Connection connection = DatabaseConnector.getConnection();
        String sql = "INSERT INTO Advising (StudentID, AdvisorID) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, (Integer) studentComboBox.getValue());
        preparedStatement.setInt(2, (Integer) advisorComboBox.getValue());
        int rs = preparedStatement.executeUpdate();

        // clear studentID & display new list
        studentComboBox.getItems().clear();
        displayStudentWithAdvisor();
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        SceneHelper.switchToScene(actionEvent, "LoginView.fxml");
    }
}
