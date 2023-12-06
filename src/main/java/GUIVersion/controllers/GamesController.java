package GUIVersion.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class GamesController {
  @FXML
  Button wordleButton;
  @FXML
  Button mcqButton;

  Stage stage;
  Scene scene;



  public void handleWordleButton(ActionEvent e) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/views/wordleGame/wordleGameMenu.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

  }

  public void handleMCQButton(ActionEvent e) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("/views/mcqGame/mcqGameMenu.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();

  }

}
