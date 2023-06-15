package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import quangle.db.xyzuniversity.models.Course;
import quangle.db.xyzuniversity.models.Professor;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseViewController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField courseNameField;

    @FXML
    private ComboBox instructorComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField dayOfWeekField;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private TextField roomNumberField;

    @FXML
    private TextField semesterField;

    @FXML
    private Spinner numberCreditSpinner;

    // TableView attributes
    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, Integer> idColumn;

    @FXML
    private TableColumn<Course, String> nameColumn;

    @FXML
    private TableColumn<Course, Integer> instructorColumn;

    @FXML
    private TableColumn<Course, Date> startDateColumn;

    @FXML
    private TableColumn<Course, Date> endDateColumn;

    @FXML
    private TableColumn<Course, String> dayOfWeekColumn;

    @FXML
    private TableColumn<Course, String> startTimeColumn;

    @FXML
    private TableColumn<Course, String> endTimeColumn;

    @FXML
    private TableColumn<Course, String> roomNumberColumn;

    @FXML
    private TableColumn<Course, Integer> numberCreditColumn;

    @FXML
    private TableColumn<Course, String> semesterColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the table view columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<>("instructorID"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        dayOfWeekColumn.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        numberCreditColumn.setCellValueFactory(new PropertyValueFactory<>("numCredits"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));

        // Set default date picker
        startDatePicker.setValue(LocalDate.of(2023, 1, 1));
        endDatePicker.setValue(LocalDate.of(2023, 1, 1));

        try {
            Connection connection = DatabaseConnector.getConnection();

            // Create a list to hold the department names
            List<Integer> instructorIDs = new ArrayList<>();

            // Query the department table to get all department names
            String query = "SELECT ProfessorID FROM Professors";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            // Loop through the result set and add each department name to the list
            while (rs.next()) {
                Integer id = rs.getInt("ProfessorID");
                instructorIDs.add(id);
            }

            //connection.close();
            instructorComboBox.getItems().addAll(instructorIDs);

            displayCourses();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCourse(ActionEvent actionEvent) throws SQLException {

        String courseName = courseNameField.getText();
        Date startDate = Date.valueOf(startDatePicker.getValue());
        Date endDate = Date.valueOf(endDatePicker.getValue());
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        String dayOfWeek = dayOfWeekField.getText();
        String roomNumber = roomNumberField.getText();
        int numCredits = (int) numberCreditSpinner.getValue();
        String semester = semesterField.getText();

        if (instructorComboBox.getValue() == null || courseName.isEmpty() || startDate.toString().isEmpty() || endDate.toString().isEmpty()
                || startTime.isEmpty() || endTime.isEmpty() || dayOfWeek.isEmpty() || roomNumber.isEmpty() || semester.isEmpty()) {
            return;
        }

        int instructorID = (Integer) instructorComboBox.getValue(); // Get value if it's not null

        Connection connection = DatabaseConnector.getConnection();
        String query = "INSERT INTO Courses (CourseName, InstructorID, StartDate, EndDate, DayOfWeek, StartTime, EndTime, RoomNumber, Credits, Semester) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, courseName);
        statement.setInt(2, instructorID);
        statement.setDate(3, startDate);
        statement.setDate(4, endDate);
        statement.setString(5, dayOfWeek);
        statement.setString(6, startTime);
        statement.setString(7, endTime);
        statement.setString(8, roomNumber);
        statement.setInt(9, numCredits);
        statement.setString(10, semester);
        statement.executeUpdate();

        courseNameField.clear();
        instructorComboBox.getSelectionModel().clearSelection();
        //startDatePicker
        //endDatePicker
        dayOfWeekField.clear();
        startTimeField.clear();
        endTimeField.clear();
        roomNumberField.clear();
        //numberCreditSpinner
        semesterField.clear();

        courseTable.getItems().clear();
        displayCourses();
    }

    private void displayCourses() throws SQLException {

        Connection connection = DatabaseConnector.getConnection();

        String query = "SELECT * FROM Courses";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Course course = new Course(rs.getInt("CourseID"), rs.getString("CourseName"),
                    rs.getInt("InstructorID"), rs.getDate("StartDate"), rs.getDate("EndDate"),
                    rs.getString("StartTime"), rs.getString("EndTime"), rs.getString("DayOfWeek"),
                    rs.getString("RoomNumber"), rs.getInt("Credits"), rs.getString("Semester"));
            courseTable.getItems().add(course);
        }
    }

    public void backToLogin(ActionEvent actionEvent) throws IOException {
        SceneHelper.switchToScene(actionEvent, "LoginView.fxml");
    }
}
