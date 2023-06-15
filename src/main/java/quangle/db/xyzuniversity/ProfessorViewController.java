package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import quangle.db.xyzuniversity.DatabaseConnector;
import quangle.db.xyzuniversity.models.Professor;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProfessorViewController implements Initializable {

    @FXML
    private TextField idField;

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
    private ComboBox<String> deptComboBox;

    // TableView attributes
    @FXML
    private TableView<Professor> professorTable;

    @FXML
    private TableColumn<Professor, Integer> idColumn;

    @FXML
    private TableColumn<Professor, String> firstNameColumn;

    @FXML
    private TableColumn<Professor, String> lastNameColumn;

    @FXML
    private TableColumn<Professor, String> addressColumn;

    @FXML
    private TableColumn<Professor, String> phoneNumberColumn;

    @FXML
    private TableColumn<Professor, String> emailColumn;

    @FXML
    private TableColumn<Professor, String> deptColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the table view columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        deptColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        try {
            Connection connection = DatabaseConnector.getConnection();

            // Create a list to hold the department names
            List<String> deptNames = new ArrayList<>();

            // Query the department table to get all department names
            String query = "SELECT DepartmentName FROM Departments";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            // Loop through the result set and add each department name to the list
            while (rs.next()) {
                String name = rs.getString("DepartmentName");
                deptNames.add(name);
            }

            //connection.close();
            deptComboBox.getItems().addAll(deptNames);

            displayProfessors();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProfessor(ActionEvent actionEvent) throws SQLException {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String department = deptComboBox.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || department.isEmpty()) {
            return;
        }

        Connection connection = DatabaseConnector.getConnection();
        String query = "INSERT INTO Professors (FirstName, LastName, Address, PhoneNumber, Email, DepartmentID) VALUES (?, ?, ?, ?, ?, (SELECT DepartmentID FROM Departments WHERE DepartmentName = ?))";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, address);
        statement.setString(4, phoneNumber);
        statement.setString(5, email);
        statement.setString(6, department);
        statement.executeUpdate();

        firstNameField.clear();
        lastNameField.clear();
        addressField.clear();
        phoneNumberField.clear();
        emailField.clear();
        deptComboBox.getSelectionModel().clearSelection();

        professorTable.getItems().clear();
        displayProfessors();
    }

    private void displayProfessors() throws SQLException {

        Connection connection = DatabaseConnector.getConnection();

        String query = "SELECT * FROM Professors p JOIN Departments d ON p.DepartmentID = d.DepartmentID;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Professor professor = new Professor(rs.getInt("ProfessorID"), rs.getString("FirstName"),
                    rs.getString("LastName"), rs.getString("Address"), rs.getString("PhoneNumber"),
                    rs.getString("Email"), rs.getString("DepartmentName"));
            //System.out.println(professor);
            professorTable.getItems().add(professor);
        }
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        SceneHelper.switchToScene(actionEvent, "LoginView.fxml");
    }
}
