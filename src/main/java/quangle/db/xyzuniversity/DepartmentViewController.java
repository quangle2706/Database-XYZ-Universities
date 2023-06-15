package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import quangle.db.xyzuniversity.DatabaseConnector;
import quangle.db.xyzuniversity.models.Department;

import java.io.IOException;
import java.sql.*;

public class DepartmentViewController {

    @FXML
    private TextField departmentNameField;

    @FXML
    private TableView<Department> departmentTable;

    @FXML
    private TableColumn<Department, Integer> idColumn;

    @FXML
    private TableColumn<Department, String> nameColumn;

    private Connection connection;

    public void initialize() throws SQLException {

        connection = DatabaseConnector.getConnection();

        // Initialize the table view columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Load the list of professors from the database and add them to the table view
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Departments");
            while (resultSet.next()) {
                Department department = new Department(resultSet.getInt("DepartmentID"), resultSet.getString("DepartmentName"));
                departmentTable.getItems().add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addDepartment() {
        // Insert the new professor into the database
        try {
            String name = departmentNameField.getText();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Departments (DepartmentName) VALUES (?)");
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Clear the text field and update the table view
        departmentNameField.clear();
        departmentTable.getItems().clear();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Departments");
            while (resultSet.next()) {
                Department department = new Department(resultSet.getInt("DepartmentID"), resultSet.getString("DepartmentName"));
                departmentTable.getItems().add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        // Close the database connection
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        SceneHelper.switchToScene(actionEvent, "LoginView.fxml");
    }
}
