package Game.mcqGameControllers;

import Game.MCQGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class MCQGamePlayController {
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

  public int remainingLives = 3;

  public void initialize() {
    mcqGame = new MCQGame();
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
    if (!mcqGame.isGameEnded()) {
      String s = Integer.toString(mcqGame.getScore());
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
      String s = Integer.toString(mcqGame.getScore());
      scoreLabel.setText(s);
    }
  }
  public void updateUI() {

  }
  public void handleAnswerSelection(ActionEvent event) {
    Button selectedButton = (Button) event.getSource();
    String choice = selectedButton.getText();
    //boolean isCorrect = mcqGame.checkUserAnswer(choice);
    mcqGame.setState(choice);
    //set mạng
//    if (!isCorrect) {
//      remainingLives--;
//      switch (remainingLives) {
//        case 2:
//          life3.isVisible(false);
//          break;
//        case 1:
//          life2.isVisible(false);
//          break;
//        case 0:
//          life1.isVisible(false);
//          break;
//        default:
//          break;
//      }
//    }
    loadQuestion();
    updateUI();
  }
}
