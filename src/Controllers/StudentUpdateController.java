package Controllers;

import Models.Course;
import Models.Student;
import Utilities.MagicData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StudentUpdateController implements Initializable {

    @FXML
    private TableView<Student> tableView;

    @FXML
    private TableColumn<Student, Integer> studentNumCol;

    @FXML
    private TableColumn<Student, String> firstNameCol;

    @FXML
    private TableColumn<Student, String> lastNameCol;

    @FXML
    private TableColumn<Student, String> avgGradeCol;

    @FXML
    private TableColumn<Student, Integer> numOfCoursesCol;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<Course> coursesComboBox;

    @FXML
    private Spinner<Integer> gradeSpinner;

    @FXML
    private Label rowsReturnedLabel;

    @FXML
    private Label studentSelectedLabel;

    @FXML
    private Button addCourseButton;

    @FXML
    private RadioButton allStudentRadioButton;

    @FXML
    private RadioButton honourRollRadioButton;

    private ArrayList<Student> allStudents;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allStudents = MagicData.getStudents();

        //configure the TableView
        studentNumCol.setCellValueFactory(new PropertyValueFactory<>("studNum"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        avgGradeCol.setCellValueFactory(new PropertyValueFactory<>("avgGradeString"));
        numOfCoursesCol.setCellValueFactory(new PropertyValueFactory<>("numOfCourses"));
        tableView.getItems().addAll(allStudents);
        updateLabels();

        //configure the search textfield to filter the objects in the
//        //tableview.  This is using an anonymous inner class
//        searchTextField.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue,
//                                String oldString, String searchText) {
//
//                ArrayList<Student> filtered = new ArrayList<>();
//                for (Student student:allStudents)
//                {
//                    if (student.contains(searchText))
//                        filtered.add(student);
//                }
//
//                //clear the ObservableList from the tableview and add in ONLY
//                //the filtered students
//                tableView.getItems().clear();
//                tableView.getItems().addAll(filtered);
//                updateLabels();
//            }
//        });

        //Search functionality, with a lambda expression
        searchTextField.textProperty().addListener((observableValue, oldString, searchText)->
            {
                ArrayList<Student> filtered = new ArrayList<>();
                for (Student student:allStudents)
                {
                    if (student.contains(searchText))
                        filtered.add(student);
                }

                //clear the ObservableList from the tableview and add in ONLY
                //the filtered students
                tableView.getItems().clear();
                tableView.getItems().addAll(filtered);
                updateLabels();
            });

        //configure the RadioButton's and ToggleGroup
        toggleGroup = new ToggleGroup();
        allStudentRadioButton.setToggleGroup(toggleGroup);
        honourRollRadioButton.setToggleGroup(toggleGroup);
        allStudentRadioButton.setUserData("allStudents");
        honourRollRadioButton.setUserData("honourRoll");

        //add a change listener to the ToggleGroup
        toggleGroup.selectedToggleProperty().addListener((obs, oldButton, buttonSelected)->{
            String button = buttonSelected.getUserData().toString();
            tableView.getItems().clear();

            if (button.equalsIgnoreCase("honourRoll"))
            {
                tableView.getItems().addAll(//stream that filters for honour roll
                        allStudents.stream()
                                    .filter(student -> student.getAvgMark()>= 80)
                                    .collect(Collectors.toList()));
            }
            else if (button.equals("allStudents"))
                tableView.getItems().addAll(allStudents);

            updateLabels();
        });


        //Configure the combobox
        coursesComboBox.setPromptText("Select a course");

        //configure the spinner object
        SpinnerValueFactory<Integer> gradeFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 75);
        gradeSpinner.setValueFactory(gradeFactory);
        gradeSpinner.setEditable(true);
        TextField spinnerEditor = gradeSpinner.getEditor();
        spinnerEditor.textProperty().addListener((observableValue, oldValue, newValue)->
        {
            try {
                Integer.parseInt(newValue);
            } catch (NumberFormatException e)
            {
                spinnerEditor.setText(oldValue);
            }
        });
    }

    private void updateLabels()
    {
        rowsReturnedLabel.setText("Rows Returned: "+tableView.getItems().size());
    }

    @FXML
    private void addGrade()
    {
        Student student = tableView.getSelectionModel().getSelectedItem();
        Course course = coursesComboBox.getValue();
        int grade = gradeSpinner.getValue();

        if (student != null && course != null && grade>=0 && grade <= 100)
        {
            student.addCourse(course, grade);
        }
        updateLabels();
    }
}
