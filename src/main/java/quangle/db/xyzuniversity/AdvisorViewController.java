package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import quangle.db.xyzuniversity.DatabaseConnector;
import quangle.db.xyzuniversity.models.Advisor;
import quangle.db.xyzuniversity.models.Department;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdvisorViewController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    // TableView attributes
    @FXML
    private TableView<Advisor> advisorTable;

    @FXML
    private TableColumn<Advisor, Integer> idColumn;

    @FXML
    private TableColumn<Advisor, String> firstNameColumn;

    @FXML
    private TableColumn<Advisor, String> lastNameColumn;

    @FXML
    private TableColumn<Advisor, String> phoneNumberColumn;

    @FXML
    private TableColumn<Advisor, String> emailColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the table view columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            Connection connection = DatabaseConnector.getConnection();

            displayAdvisors();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAdvisor(ActionEvent actionEvent) throws SQLException {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            return;
        }

        Connection connection = DatabaseConnector.getConnection();
        String query = "INSERT INTO Advisors (FirstName, LastName, PhoneNumber, Email) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, phoneNumber);
        statement.setString(4, email);
        statement.executeUpdate();

        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();

        advisorTable.getItems().clear();
        displayAdvisors();
    }

    private void displayAdvisors() throws SQLException {

        Connection connection = DatabaseConnector.getConnection();

        String query = "SELECT * FROM Advisors";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Advisor advisor = new Advisor(rs.getInt("AdvisorID"), rs.getString("FirstName"),
                    rs.getString("LastName"), rs.getString("PhoneNumber"), rs.getString("Email"));
            advisorTable.getItems().add(advisor);
        }
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        SceneHelper.switchToScene(actionEvent, "LoginView.fxml");
    }
}
