package smith.firstscreen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This application is for C482 - Software 1.
 * An application  that manages inventory.
 * FUTURE ENHANCEMENT: In the future, I would add the option to modify the part within the product screen in order to streamline the process.
 * JavaDocs located in Javadoc folder under FirstScreen.
 * @author Kyle Smith
 * */

/** Launches the Main Application. */
public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 852, 330);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}