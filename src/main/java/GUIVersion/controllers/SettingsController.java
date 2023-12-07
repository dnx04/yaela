package GUIVersion.controllers;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

public class SettingsController implements Initializable {

  final String CONFIG_FILE_PATH =  (System.getProperty("user.dir") + "/src/main/java/GUIVersion/resources/config.properties");
  Properties properties;

  @FXML
  Button lightModeButton;

  @FXML
  Button darkModeButton;

  @FXML
  Button increaseFontSizeButton, decreaseFontSizeButton;

  @FXML
  Slider voiceRateSlider, voicePitchSlider, voiceVolumeSlider;

  FileInputStream fileInputStream;
  FileOutputStream fileOutputStream;
  public void setFontSize(ActionEvent e) {
    Button button = (Button) e.getSource();
    String mode = button.getText();

    int fontSizeRate = Integer.parseInt(properties.getProperty("fontSizeRate"));
    if (mode.equals("Increase") && fontSizeRate < 6) {
      properties.setProperty("fontSizeRate", Integer.toString(fontSizeRate + 1));
    } else if (mode.equals("Decrease") && fontSizeRate > 1) {
      properties.setProperty("fontSizeRate", Integer.toString(fontSizeRate - 1));
    } else {
      return;
    }


    Parent root = button.getScene().getRoot();
    TabPane tabPane = (TabPane) root;
    for (Tab tab : tabPane.getTabs()) {
      AnchorPane anchorPane = (AnchorPane) tab.getContent();
      for (Node node : anchorPane.getChildren()) {
        changeFontSize(node, mode);

      }
    }
  }

  public void setFontFamily(ActionEvent e) throws IOException {
    Button button = (Button) e.getSource();
    String fontFamily = button.getText();
    properties.setProperty("fontFamily", fontFamily);
    fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH);
    properties.store(fileOutputStream, "GUI config");
    Parent root = button.getScene().getRoot();
    TabPane tabPane = (TabPane) root;
    for (Tab tab : tabPane.getTabs()) {
      AnchorPane anchorPane = (AnchorPane) tab.getContent();
      for (Node node : anchorPane.getChildren()) {
        changeFontFamily(node);
      }
    }
  }
  public void changeFontSize(Node node, String mode) {

    double offset;
    if (mode.equals("Increase")) {
      offset = 1;
    } else {
      offset = -1;
    }
    if (node instanceof Label) {

      Label label = (Label) node;
      Font oldFont = label.getFont();
      label.setFont(new Font(oldFont.getName(), oldFont.getSize() + offset));
    }
    if (node instanceof TextField) {
      TextField textField = (TextField) node;
      Font oldFont = textField.getFont();
      textField.setFont(new Font(oldFont.getName(), oldFont.getSize() + offset));
    }
    if (node instanceof Text) {
      Text text = (Text) node;
      Font oldFont = text.getFont();
      Font newFont = new Font(oldFont.getName(), oldFont.getSize() + offset);
      text.setFont(newFont);
    }
    if (node instanceof TextArea) {
      TextArea textArea = (TextArea) node;
      Font oldFont = textArea.getFont();
      textArea.setFont(new Font(oldFont.getName(), oldFont.getSize() + +offset));
    }
    if (node instanceof Button) {
      Button button = (Button) node;
      Font oldFont = button.getFont();
      button.setFont(new Font(oldFont.getName(), oldFont.getSize() + +offset));
    }
    if (node instanceof RadioButton) {
      RadioButton radioButton = (RadioButton) node;
      Font oldFont = radioButton.getFont();
      radioButton.setFont(new Font(oldFont.getName(), oldFont.getSize() + offset));
    }
    if (node instanceof ChoiceBox) {
      ChoiceBox choiceBox = (ChoiceBox) node;
      List<Label> labels = new ArrayList<>();
      for (Node label : choiceBox.lookupAll(".label")) {
        labels.add((Label) label);
      }

      for (Label label : labels) {
        changeFontSize(label, mode);
      }
    }

    if (node instanceof Pane) {
      Pane pane = (Pane) node;
      for (Node child : pane.getChildren()) {
        changeFontSize(child, mode);
      }
    }

    if (node instanceof ListView) {
      ListView listView = (ListView) node;
      List<Label> labels = new ArrayList<>();
      for (Node label : listView.lookupAll(".label")) {
        labels.add((Label) label);
      }

      for (Label label : labels) {
        changeFontSize(label, mode);
      }
    }


  }
  public void changeFontFamily(Node node) {

    String fontFamily = properties.getProperty("fontFamily");

    if (node instanceof Label) {

      Label label = (Label) node;
      Font oldFont = label.getFont();
      label.setFont(new Font(fontFamily, oldFont.getSize()));
    }
    if (node instanceof TextField) {
      TextField textField = (TextField) node;
      Font oldFont = textField.getFont();
      textField.setFont(new Font(fontFamily, oldFont.getSize() ));
    }
    if (node instanceof Text) {
      Text text = (Text) node;
      Font oldFont = text.getFont();
      text.setFont(new Font(fontFamily, oldFont.getSize() ));
    }
    if (node instanceof TextArea) {
      TextArea textArea = (TextArea) node;
      Font oldFont = textArea.getFont();
      textArea.setFont(new Font(fontFamily, oldFont.getSize() ));
    }
    if (node instanceof Button) {
      Button button = (Button) node;
      Font oldFont = button.getFont();
      button.setFont(new Font(fontFamily, oldFont.getSize() ));
    }
    if (node instanceof RadioButton) {
      RadioButton radioButton = (RadioButton) node;
      Font oldFont = radioButton.getFont();
      radioButton.setFont(new Font(fontFamily, oldFont.getSize() ));
    }
    if (node instanceof ChoiceBox) {
      ChoiceBox choiceBox = (ChoiceBox) node;
      List<Label> labels = new ArrayList<>();
      for (Node label : choiceBox.lookupAll(".label")) {
        labels.add((Label) label);
      }

      for (Label label : labels) {
        changeFontFamily(label);
      }
    }

    if (node instanceof Pane) {
      Pane pane = (Pane) node;
      for (Node child : pane.getChildren()) {
        changeFontFamily(child);
      }
    }

    if (node instanceof ListView) {
      ListView listView = (ListView) node;
      List<Label> labels = new ArrayList<>();
      for (Node label : listView.lookupAll(".label")) {
        labels.add((Label) label);
      }

      for (Label label : labels) {
        changeFontFamily(label);
      }
    }

    if (node instanceof WebView) {
      WebView webView = (WebView) node;
      String htmlContent = DictionaryController.htmlContent;
      htmlContent = changeHtmlFontFamily(htmlContent, fontFamily);
      DictionaryController.contentWebView = changeHtmlFontFamily(DictionaryController.contentWebView, fontFamily);
      webView.getEngine().loadContent(htmlContent);
    }
  }

  public void setDarkMode(ActionEvent a) {

    Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

    Button button = (Button) a.getSource();
    Parent root = button.getScene().getRoot();
    TabPane tabPane = (TabPane) root;
    for (Tab tab : tabPane.getTabs()) {
      AnchorPane anchorPane = (AnchorPane) tab.getContent();
      for (Node node : anchorPane.getChildren()) {
        if (node instanceof WebView) {
          WebView webView = (WebView) node;

          DictionaryController.htmlContent = changeHtmlBackground(DictionaryController.htmlContent, "#0c1118");
          DictionaryController.htmlContent = changeHtmlFontColor(DictionaryController.htmlContent, "white");
          DictionaryController.contentWebView = changeHtmlFontColor(DictionaryController.contentWebView, "white");
          DictionaryController.contentWebView = changeHtmlBackground(DictionaryController.contentWebView, "#0c1118");
          webView.getEngine().loadContent(DictionaryController.htmlContent);
        }
      }
    }
  }


  public void setLightMode(ActionEvent a) {

    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

    Button button = (Button) a.getSource();
    Parent root = button.getScene().getRoot();
    TabPane tabPane = (TabPane) root;
    for (Tab tab : tabPane.getTabs()) {
      AnchorPane anchorPane = (AnchorPane) tab.getContent();
      for (Node node : anchorPane.getChildren()) {
        if (node instanceof WebView) {
          WebView webView = (WebView) node;

          DictionaryController.htmlContent = changeHtmlBackground(DictionaryController.htmlContent, "white");
          DictionaryController.htmlContent = changeHtmlFontColor(DictionaryController.htmlContent, "black");
          DictionaryController.contentWebView = changeHtmlFontColor(DictionaryController.contentWebView, "black");
          DictionaryController.contentWebView = changeHtmlBackground(DictionaryController.contentWebView, "white");
          webView.getEngine().loadContent(DictionaryController.htmlContent);

        }
      }
    }
  }

  public String changeHtmlFontColor(String html, String color) {
    int index = html.indexOf(";color:");
    return html.substring(0, index) + ";color: " + color + "       " + html.substring(index + 15);
  }

  public String changeHtmlFontFamily(String html, String family) {
    int index = html.indexOf("font-family:");
    return html.substring(0, index) + "font-family: " + family + "       " + html.substring(index + 24);
  }

  public String changeHtmlBackground(String html, String color) {
    int index = html.indexOf("background-color:");
    return html.substring(0, index) + "background-color: " + color + "       " + html.substring(index + 24);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    properties = new Properties();
    fileInputStream = null;
    fileOutputStream = null;


    try {
      fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
      properties.load(fileInputStream);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    properties.setProperty("fontSizeRate", "3");

    double voiceRate = Double.parseDouble(properties.getProperty("voiceRate"));
    double voicePitch = Double.parseDouble(properties.getProperty("voicePitch"));
    double voiceVolume = Double.parseDouble(properties.getProperty("voiceVolume"));
    voiceRateSlider.setValue(voiceRate);
    voicePitchSlider.setValue(voicePitch);
    voiceVolumeSlider.setValue(voiceVolume);

    voicePitchSlider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        properties.setProperty("voicePitch", Double.toString(voicePitchSlider.getValue()));
        try {
          fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH);
          properties.store(fileOutputStream, "GUI config");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
    voiceRateSlider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        properties.setProperty("voiceRate", Double.toString(voiceRateSlider.getValue()));
        try {
          fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH);
          properties.store(fileOutputStream, "GUI config");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
    voiceVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        properties.setProperty("voiceVolume", Double.toString(voiceVolumeSlider.getValue()));
        try {
          fileOutputStream = new FileOutputStream(CONFIG_FILE_PATH);
          properties.store(fileOutputStream, "GUI config");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

  }
}
