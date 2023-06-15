package quangle.db.xyzuniversity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import quangle.db.xyzuniversity.models.Course;
import quangle.db.xyzuniversity.models.Student;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentCourseInformationViewController {

    // TableView attributes for Available courses
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

    private Student student;

    private void displayCourses() throws SQLException {

        Connection connection = DatabaseConnector.getConnection();

        // Available Courses
        String query = "SELECT Courses.*, Enrolments.Grade " +
                "FROM Courses " +
                "JOIN Enrolments ON Courses.CourseID = Enrolments.CourseID " +
                "WHERE Enrolments.StudentID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, student.getId());
        ResultSet rs = statement.executeQuery();

        List<Double> grades = new ArrayList<>();

        while (rs.next()) {
            Course course = new Course(rs.getInt("CourseID"), rs.getString("CourseName"),
                    rs.getInt("InstructorID"), rs.getDate("StartDate"), rs.getDate("EndDate"),
                    rs.getString("StartTime"), rs.getString("EndTime"), rs.getString("DayOfWeek"),
                    rs.getString("RoomNumber"), rs.getInt("Credits"), rs.getString("Semester"));
            courseTable.getItems().add(course);
            grades.add(rs.getDouble("Grade"));
        }

        TableColumn<Course, Double> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(cellData -> {
            // Get the row data for this cell
            Course rowData = cellData.getValue();
            // Get the index of this row in the table view
            int rowIndex = cellData.getTableView().getItems().indexOf(rowData);
            // Get the double value for this row
            Double gradeValue = grades.get(rowIndex);
            // Return the double value as a property
            return new SimpleDoubleProperty(gradeValue).asObject();
        });

        courseTable.getColumns().add(gradeColumn);
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
}
