package Game.wordleGameControllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WordleGameHighScoreController {

  @FXML
  Button backButton;

  @FXML
  private Label firstHighScoreLabel;

  @FXML
  private Label secondHighScoreLabel;

  @FXML
  private Label thirdHighScoreLabel;

  Stage stage;
  Scene scene;

  public void changeSceneToMenu(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/wordleGame/wordleGameMenu.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
  public List<Integer> readFileToArray() {
    List<Integer> lines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/main/java/GUIVersion/resources/highscore2.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            int score = Integer.parseInt(line.trim());
            lines.add(score);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return lines;
    }
  public void getHighscore() throws FileNotFoundException{
    List<Integer> lines = readFileToArray();
    Collections.sort(lines,Collections.reverseOrder());
    List<Integer> top3Scores = lines.subList(0, Math.min(lines.size(), 3));
    for(int i =0;i<top3Scores.size();i++){
        System.out.println(top3Scores.get(i));
    }
    if (top3Scores.size() >= 1) {
        firstHighScoreLabel.setText("1st: " + top3Scores.get(0));
    } else {
        firstHighScoreLabel.setText("1st: -");
    }

    if (top3Scores.size() >= 2) {
        secondHighScoreLabel.setText("2nd: " + top3Scores.get(1));
    } else {
        secondHighScoreLabel.setText("2nd: -");
    }

    if (top3Scores.size() >= 3) {
        thirdHighScoreLabel.setText("3rd: " + top3Scores.get(2));
    } else {
        thirdHighScoreLabel.setText("3rd: -");
    }

  }
}
