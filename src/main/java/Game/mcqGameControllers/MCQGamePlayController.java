package Game.mcqGameControllers;

import Game.MCQGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MCQGamePlayController implements Initializable {
  @FXML
  Button quitButton;
  @FXML
  Button buttonA;
  @FXML
  Button buttonB;
  @FXML
  Button buttonC;
  @FXML
  Button buttonD;
  @FXML
  Label scoreLabel;
  @FXML
  Label numberQuestion;
  @FXML
  Label questionLabel;
  @FXML
  ImageView life1;
  @FXML
  ImageView life2;
  @FXML
  ImageView life3;

  Stage stage;
  Scene scene;

  MCQGame mcqGame;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mcqGame = new MCQGame();
    questionLabel.setWrapText(true);
    loadQuestion();
    updateUI();
  }
  
  public void changeSceneToMenu(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/mcqGame/mcqGameMenu.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void updateQuestionLabel(String newQuestion) {
    questionLabel.setText("Từ sau đây có nghĩa là " + newQuestion);
  }

  public void loadQuestion() {
    String s = Integer.toString(mcqGame.getScore());
    if (!mcqGame.isGameEnded()) {
      numberQuestion.setText(s);
      scoreLabel.setText(s);

      String question = mcqGame.getCurrentQuestion();
      String choiceA = mcqGame.getChoices().get(0);
      String choiceB = mcqGame.getChoices().get(1);
      String choiceC = mcqGame.getChoices().get(2);
      String choiceD = mcqGame.getChoices().get(3);

      updateQuestionLabel(question);
      buttonA.setText(choiceA);
      buttonB.setText(choiceB);
      buttonC.setText(choiceC);
      buttonD.setText(choiceD);
    } else {
      //scoreLabel.setText(s);
      GameOverAlert();
    }
  }
  public void updateUI() {
    int errorCount = mcqGame.getErrorCount();
    if(errorCount == 0){
      life1.setVisible(true);
      life2.setVisible(true);
      life3.setVisible(true);
    }
    if (errorCount >= 1) {
      life3.setVisible(false);
    }
    if (errorCount >= 2) {
      life2.setVisible(false);
    }
    if (errorCount >= 3) {
      life1.setVisible(false);
    }
  }
  public void handleAnswerSelection(ActionEvent event) {
    Button selectedButton = (Button) event.getSource();
    String choice = selectedButton.getId().substring(6);
    mcqGame.setState(choice);
    updateUI();
    loadQuestion();
  }
  public void GameOverAlert(){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("Điểm số của bạn là: " +mcqGame.getScore());
    alert.setContentText("Bấm Ok để chơi lại!");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      resetGame();
    } else {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mcqGame/mcqGameMenu.fxml"));
        Parent root = loader.load();
        Scene menuScene = new Scene(root);

        Stage currentStage = (Stage) quitButton.getScene().getWindow();
        currentStage.setScene(menuScene);
        currentStage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  public void resetGame(){
    mcqGame.init();
    updateUI();
    loadQuestion();
  }
}
