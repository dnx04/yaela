package Game.mcqGameControllers;

import java.io.IOException;
import java.util.ArrayList;

import Game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MCQGameMenuController {

  @FXML
  Button quitButton;
  @FXML
  Button playButton;
  @FXML
  Button highScoreButton;
  TabPane tabPane;
  Stage stage;
  Scene scene;
  
  public void changeSceneToGameCenter(ActionEvent e) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
    Parent root = loader.load();
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);

    tabPane = (TabPane) loader.getNamespace().get("tabPane");
    int tabIndex = 3;
    tabPane.getSelectionModel().select(tabIndex);

    stage.show();
  }

  public void changeSceneToGamePlay(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/mcqGame/mcqGamePlay.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void changeSceneToHighScore(ActionEvent e) throws IOException {
    /*Parent root = FXMLLoader.load(getClass().getResource("/views/mcqGame/mcqGameHighScore.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();*/
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mcqGame/mcqGameHighScore.fxml"));
    Parent root = loader.load();
    
    MCQGameHighScoreController controller = loader.getController();
    controller.getHighscore();

    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    
  }

}
