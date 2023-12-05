package GUIVersion.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Database.QueryEngine;

public class DictionaryController implements Initializable {
    private static final int WORD_SEARCH_LIMIT = 10;
    private static final String NO_WORD_NOTI = "No words found!";
    private QueryEngine queryEngine = new QueryEngine("avdict.db");

    @FXML
    private AnchorPane searchPane;

    @FXML
    private TextField input;

    @FXML
    private ChoiceBox<String> dictChoiceBox;
    private final String[] dicts = {"Việt - Anh", "Anh - Việt"};

    @FXML
    private ListView<String> searchList;

    @FXML
    private WebView webView;
    private static String contentWebView = "";
    private static String word = "";
    
    @FXML
    private Button speaker;

    public String getDict(String choice) {
        switch (choice) {
          case "Việt - Anh":
            return "va";
          case "Anh - Việt":
            return "av";
        }
        return "";
      }

    public void search(KeyEvent e) throws SQLException {
        String inputText = input.getText();
        searchList.getItems().clear();
        ObservableList<String> listWord = searchList.getItems();
        ArrayList<String> listDefinition = new ArrayList<String>();
        listDefinition.clear();
        WebEngine webEngine = webView.getEngine();

        input.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                event.consume();
            }
        });

        if (inputText == "" || inputText == null) {
            searchList.setVisible(false);
        } else {
            try {
                String dictChoice = getDict(dictChoiceBox.getValue());
                String query = "SELECT * FROM '" + dictChoice 
                    + "' WHERE word LIKE '" + inputText + "%' LIMIT " + WORD_SEARCH_LIMIT;
                ResultSet rs = queryEngine.makeQuery(query);
                while (rs.next()) {
                    String word = rs.getString(2);
                    String def = rs.getString(3);
                    listWord.add(word);
                    listDefinition.add(def);
                }
                if (listWord.size() == 0) {
                    listWord.add(NO_WORD_NOTI);
                }

                // Set height of search list appropriate to the size of list
                searchList.prefHeightProperty().bind(Bindings.size(listWord).multiply(41));
                searchList.setVisible(true);

                if (listWord.size() != 0 && !(listWord.get(0).equals(NO_WORD_NOTI))) {

                    // Add listener when chosen for each item in list
                    searchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && newValue != NO_WORD_NOTI) {
                            int selectedIndex = searchList.getSelectionModel().getSelectedIndex();
                            contentWebView = "<html><head><style>body {font-family: \"Calibri\", \"Helvetica\", sans-serif;}</style></head><body>" 
                                + listDefinition.get(selectedIndex) + "</body></html>";
                            word = listWord.get(selectedIndex);
                            searchList.setVisible(false);
                            if (dictChoice == getDict("Anh - Việt")) {
                                speaker.setVisible(true);
                            }
                        }
                        webEngine.loadContent(contentWebView);
                    });

                    
                }
                webView.setVisible(true);

                // Make search list disappear when clicking outside search list region
                searchPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (!searchList.getBoundsInParent().contains(event.getX(), event.getY())) {
                        searchList.setVisible(false);
                    }
                });

            } catch (SQLException | RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void pronounce() {
        Sound.TextToSpeech.TextSpeech(word);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictChoiceBox.getItems().addAll(dicts);
        searchList.setVisible(false);
        //speaker.setVisible(false);
    }
}
