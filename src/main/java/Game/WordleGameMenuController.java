package Game;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WordleGameMenuController {

  @FXML
  Button quitButton;
  @FXML
  Button playButton;
  @FXML
  Button highScoreButton;

  Stage stage;

  Scene scene;

  public void changeSceneToGameCenter(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);



    stage.show();
  }

  public void changeSceneToGamePlay(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/wordleGame/wordleGamePlay.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void changeSceneToHighScore(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/wordleGame/wordleGameHighScore.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }


}
