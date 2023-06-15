package quangle.db.xyzuniversity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quangle.db.xyzuniversity.models.ButtonCell;
import quangle.db.xyzuniversity.models.Course;
import quangle.db.xyzuniversity.models.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageClassesViewController { // implements Initializable {

    // TODO: Need to show list of courses available with button ADD or DROP
    // TODO: The button ADD beside the COURSE x STUDENT not in ENROLMENTS
    // TODO: The button DROP beside the COURSE x STUDENT in ENROLMENTS

    // Step 1: Need to show FXML the table of courses available
    // Step 2: With relatively row of course -> ADD button or DROP button
    // Step 3: INSERT INTO Enrolments table
    // Step 4: ...

    // Change the way to show:
    // Show 2 TableView for already added courses
    // and another TableView for available courses to Add
    // 2 button for add & drop

    @FXML
    private ComboBox availableCoursesComboBox;

    @FXML
    private ComboBox enrolledCoursesComboBox;

    // TableView attributes for Available courses
    @FXML
    private TableView<Course> availableCourseTable;

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

    // TableView attributes for Enrolled courses
    @FXML
    private TableView<Course> enrolledCourseTable;

    @FXML
    private TableColumn<Course, Integer> idColumnEnrolled;

    @FXML
    private TableColumn<Course, String> nameColumnEnrolled;

    @FXML
    private TableColumn<Course, Integer> instructorColumnEnrolled;

    @FXML
    private TableColumn<Course, Date> startDateColumnEnrolled;

    @FXML
    private TableColumn<Course, Date> endDateColumnEnrolled;

    @FXML
    private TableColumn<Course, String> dayOfWeekColumnEnrolled;

    @FXML
    private TableColumn<Course, String> startTimeColumnEnrolled;

    @FXML
    private TableColumn<Course, String> endTimeColumnEnrolled;

    @FXML
    private TableColumn<Course, String> roomNumberColumnEnrolled;

    @FXML
    private TableColumn<Course, Integer> numberCreditColumnEnrolled;

    @FXML
    private TableColumn<Course, String> semesterColumnEnrolled;

    private Student student;

    private void displayCourses() throws SQLException {

        // Remove all previous UI
        // ComboBox + TableView
        availableCoursesComboBox.getItems().clear();
        enrolledCoursesComboBox.getItems().clear();
        availableCourseTable.getItems().clear();
        enrolledCourseTable.getItems().clear();

        Connection connection = DatabaseConnector.getConnection();

        // Available Courses
        String query = "SELECT Courses.* FROM Courses " +
                "LEFT JOIN Enrolments ON Courses.CourseID = Enrolments.CourseID AND Enrolments.StudentID = ? " +
                "WHERE Enrolments.CourseID IS NULL";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, student.getId());
        ResultSet rs = statement.executeQuery();

        // Create a list to hold the department names
        List<String> availableCourses = new ArrayList<>();

        while (rs.next()) {
            Course course = new Course(rs.getInt("CourseID"), rs.getString("CourseName"),
                    rs.getInt("InstructorID"), rs.getDate("StartDate"), rs.getDate("EndDate"),
                    rs.getString("StartTime"), rs.getString("EndTime"), rs.getString("DayOfWeek"),
                    rs.getString("RoomNumber"), rs.getInt("Credits"), rs.getString("Semester"));
            availableCourseTable.getItems().add(course);
            availableCourses.add(Integer.toString(course.getId()));
        }

        // update combo box for available course
        availableCoursesComboBox.getItems().addAll(availableCourses);

        // Create a list to hold the department names
        List<String> enrolledCourses = new ArrayList<>();

        // Enrolled Courses
        String enrolledQuery = "SELECT Courses.* " +
                "FROM Courses " +
                "JOIN Enrolments ON Courses.CourseID = Enrolments.CourseID " +
                "WHERE Enrolments.StudentID = ?";
        PreparedStatement enrolledStatement = connection.prepareStatement(enrolledQuery);
        enrolledStatement.setInt(1, student.getId());
        ResultSet enrolledRs = enrolledStatement.executeQuery();
        while (enrolledRs.next()) {
            Course course = new Course(enrolledRs.getInt("CourseID"), enrolledRs.getString("CourseName"),
                    enrolledRs.getInt("InstructorID"), enrolledRs.getDate("StartDate"), enrolledRs.getDate("EndDate"),
                    enrolledRs.getString("StartTime"), enrolledRs.getString("EndTime"), enrolledRs.getString("DayOfWeek"),
                    enrolledRs.getString("RoomNumber"), enrolledRs.getInt("Credits"), enrolledRs.getString("Semester"));
            enrolledCourseTable.getItems().add(course);
            enrolledCourses.add(Integer.toString(course.getId()));
        }

        // update combo box for enrolled course
        enrolledCoursesComboBox.getItems().addAll(enrolledCourses);
    }

    public void setStudent(Student student) {
        this.student = student;
        updateUI();
    }

    private void updateUI() {
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

        idColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("name"));
        instructorColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("instructorID"));
        startDateColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        startTimeColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        dayOfWeekColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
        roomNumberColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        numberCreditColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("numCredits"));
        semesterColumnEnrolled.setCellValueFactory(new PropertyValueFactory<>("semester"));

        try {
            displayCourses();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void addNewCourse(ActionEvent actionEvent) throws SQLException {


        if (availableCoursesComboBox.getValue() == null) {
            return;
        }

        // Add a new course
        int courseID = Integer.parseInt((String) availableCoursesComboBox.getValue());

        Connection connection = DatabaseConnector.getConnection();

        String sql = "INSERT INTO Enrolments (StudentID, CourseID, Grade) VALUES (?, ?, 0.0)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, student.getId());
        preparedStatement.setInt(2, courseID);
        int rs = preparedStatement.executeUpdate();

        // Update available courses table & enrolled courses table & update 2 combo box
        displayCourses();

    }

    public void dropCourse(ActionEvent actionEvent) throws SQLException {

        if (enrolledCoursesComboBox.getValue() == null) {
            return;
        }

        int courseID = Integer.parseInt((String) enrolledCoursesComboBox.getValue());

        // Delete an enrolment of student and course
        Connection connection = DatabaseConnector.getConnection();
        String sql = "DELETE FROM Enrolments WHERE StudentID = ? AND CourseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, student.getId());
        preparedStatement.setInt(2, courseID);
        int rs = preparedStatement.executeUpdate();

        // Update available courses table & enrolled courses table
        displayCourses();

    }
}
