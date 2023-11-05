package GUIVersion.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
import GoogleTranslateAPI.Translator;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class TranslatorController implements Initializable {

  @FXML
  private ChoiceBox inputLang;
  private final String[] inlang = {"Tiếng Việt", "English", "Not detected"};
  private final String[] outlang = {"Tiếng Việt", "English"};

  @FXML
  private ChoiceBox outputLang;
  final Clipboard clipboard = Clipboard.getSystemClipboard();


  @FXML
  private TextArea input;

  @FXML
  private TextArea output;

  @FXML
  Button go;
  @FXML
  Button copy;


  public String getLang(String choice) {
    switch (choice) {
      case "Tiếng Việt":
        return "vi";
      case "English":
        return "en";
    }
    return "";
  }

  public void translateInput() {
//    if(timing < 5000) return;
    try {
      String inputString = input.getText();
      String from = getLang(inputLang.getValue().toString());
      String to = getLang(outputLang.getValue().toString());
      output.setText(Translator.translate(from, to, inputString));
    }
    catch (Exception e) {}
  }

  public void copyText() {
    String text = output.getText();
    final ClipboardContent content = new ClipboardContent();
    content.putString(text);
    clipboard.setContent(content);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    inputLang.getItems().addAll(inlang);
    outputLang.getItems().addAll(outlang);
    inputLang.setValue(inlang[0]);
    outputLang.setValue(outlang[1]);
  }
}
