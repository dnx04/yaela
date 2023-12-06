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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

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
    System.out.println(fontSizeRate);
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
      textField.setFont(new Font(oldFont.getName(), oldFont.getSize() + +offset));
    }
    if (node instanceof Text) {
      Text text = (Text) node;
      Font oldFont = text.getFont();
      text.setFont(new Font(oldFont.getName(), oldFont.getSize() + +offset));
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
  }
  public void changeFontFamily(Node node) {
    String fontFamily = properties.getProperty("fontFamily");

    if (node instanceof Label) {

      Label label = (Label) node;
      Font oldFont = label.getFont();
      label.setFont(new Font(fontFamily, oldFont.getSize()));
      System.out.println(oldFont.getFamily());
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
  }

  public void setDarkMode(ActionEvent a) {
    Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
  }

  public void setLightMode(ActionEvent a) {
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
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

    properties.setProperty("fontSize", "3");

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
