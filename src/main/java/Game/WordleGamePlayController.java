package Game;

import Game.WordleGame.GameState;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WordleGamePlayController implements Initializable {
  @FXML
  Button backButton;
  @FXML
  GridPane gridPane;
  @FXML
  Text winBanner;
  @FXML
  Text loseBanner;


  Stage stage;
  Scene scene;
  WordleGame gameObj;




  public void changeSceneToMenu(ActionEvent e) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/views/wordleGame/wordleGameMenu.fxml"));
    stage = (Stage)((Node)e.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }


  public void handleKeyEvent(KeyEvent event) throws SQLException {
    WordleGame.GameState gs = gameObj.getGs();
    if (gs == GameState.WIN) {
      winBanner.setVisible(true);
      if (event.getCode() == KeyCode.ENTER) {
        gameObj = new WordleGame();
        System.out.println(gameObj.getPick());
        updateUIState();
        winBanner.setVisible(false);
      }
    }
    if (gs == GameState.LOSE) {
      loseBanner.setVisible(true);
      if (event.getCode() == KeyCode.ENTER) {
        gameObj = new WordleGame();
        System.out.println(gameObj.getPick());
        updateUIState();
        loseBanner.setVisible(false);
      }
    }

    if (gs == GameState.IN_PROGRESS) {
      gameObj.setState(event);
      updateUIState();
    }
  }

  public void updateUIState() {

    char[][] word = gameObj.getWord();
    String pick = gameObj.getPick();
    Boolean[] exists = gameObj.getExists();
    WordleGame.TileState[][] ts = gameObj.getTs();
    int turn = gameObj.getTurn();
    int cur = gameObj.getCur();
    WordleGame.GameState gs = gameObj.getGs();

    if (gs == GameState.IN_PROGRESS) {
      for (javafx.scene.Node node : gridPane.getChildren()) {
        if (node instanceof Label) {
          int col = GridPane.getColumnIndex(node);
          int row = GridPane.getRowIndex(node);
          Label labeledLabel = (Label) node;
          labeledLabel.setText("" + word[row][col]);

          WordleGame.TileState tileState = ts[row][col];

          switch (tileState) {
            case BLANK:
              labeledLabel.getStyleClass().clear();
              labeledLabel.getStyleClass().add("blank-letter");
              break;
            case CORRECT:
              labeledLabel.getStyleClass().clear();
              labeledLabel.getStyleClass().add("true-letter-true-pos");
              break;
            case NOT_CONTAIN:
              labeledLabel.getStyleClass().clear();
              labeledLabel.getStyleClass().add("false-letter");
              break;
            case WRONG_POSITION:
              labeledLabel.getStyleClass().clear();
              labeledLabel.getStyleClass().add("true-letter-false-pos");
              break;
          }
        }
      }
    }
    if (gs == GameState.WIN) {
      winBanner.setVisible(true);
    }
    if (gs == GameState.LOSE) {
      loseBanner.setVisible(true);
    }

  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      gameObj = new WordleGame();
      System.out.println(gameObj.getPick());
      updateUIState();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


}
