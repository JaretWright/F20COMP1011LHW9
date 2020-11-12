import Models.Course;
import Models.Student;
import Utilities.MagicData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Views/studentUpdateView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("View Students");
        stage.getIcons().add(new Image("grad_icon.png"));
        stage.show();
    }
}
