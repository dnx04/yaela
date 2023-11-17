package GUIVersion;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DictionaryApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("yaela");
        stage.setScene(scene);
        stage.show();
    }

}
