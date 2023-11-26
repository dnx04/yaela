package Game.mcqGameControllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MCQGamePlayController {

  Stage stage;
  Scene scene;
  public void changeSceneToMenu(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/mcqGame/mcqGameMenu.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
