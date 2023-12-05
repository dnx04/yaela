package GUIVersion.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import CommandVersion.Word;
import Database.QueryEngine;
import GUIVersion.DictionaryApplication;

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
    private final String[] dicts = {"Anh - Việt", "Việt - Anh"};

    @FXML
    private ListView<String> searchList;

    @FXML
    private WebView webView;
    private static String contentWebView = "";
    private static String word = "";
    private static String description = "";
    private static String dictionary = "";
    
    @FXML
    private Button speaker;

    @FXML
    private Tooltip pronounceTooltip;


    @FXML
    private Button addToFav;
    private final String DIALOG_TITLE = "Add to favourites";

    @FXML
    private Tooltip favoriteTooltip;

    // @FXML
    // private DialogPane dialogPane;
    // private Dialog<Word> addToFavDialog = new Dialog<>();
    // @FXML
    // private TextField wordField;
    
    // @FXML
    // private TextField noteField;

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
        ArrayList<String> listDescription = new ArrayList<String>();
        listDefinition.clear();
        listDescription.clear();
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
                    String resultWord = rs.getString(2);
                    String resultDef = rs.getString(3);
                    String resultDescription = rs.getString(4);
                    listWord.add(resultWord);
                    listDefinition.add(resultDef);
                    listDescription.add(resultDescription);
                }
                if (listWord.size() == 0) {
                    listWord.add(NO_WORD_NOTI);
                }

                // Set height of search list appropriate to the size of list
                searchList.prefHeightProperty().bind(Bindings.size(listWord).multiply(41));
                searchList.setVisible(true);

                if (listWord.size() != 0 && !(listWord.get(0).equals(NO_WORD_NOTI))) {

                    // Add listener when chosen for each item in list
                    searchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String> ()
                    {
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (newValue != null && newValue != NO_WORD_NOTI) {
                                int selectedIndex = searchList.getSelectionModel().getSelectedIndex();
                                contentWebView = "<html><head><style>body {font-family: \"Calibri\", \"Helvetica\", sans-serif;}</style></head><body>" 
                                    + listDefinition.get(selectedIndex) + "</body></html>";
                                word = listWord.get(selectedIndex);
                                description = listDescription.get(selectedIndex);
                                dictionary = dictChoice;
                                System.out.println(word);
                                searchList.setVisible(false);
                                
                                if (dictChoice.equals(getDict("Anh - Việt"))) {
                                    speaker.setVisible(true);
                                }
                            }
                            webEngine.loadContent(contentWebView);
                            addToFav.setVisible(true);
                            
                            searchList.getSelectionModel().selectedItemProperty().removeListener(this);
                        }
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

    public void handleAddToFavorite() throws IOException {
        DialogPane pane = FXMLLoader.load(getClass().getResource("/views/addtofavdialog.fxml"));
        TextField wordField = (TextField) pane.lookup("#wordDialog");
        TextArea noteField = (TextArea) pane.lookup("#noteDialog");
        Dialog<Button> dialog = new Dialog<>();

        dialog.setDialogPane(pane);
        dialog.setTitle(DIALOG_TITLE);

        wordField.setText(word);
        noteField.setText(description);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String[] favWord = {wordField.getText(), noteField.getText()};
                if (dictionary.equals(getDict("Anh - Việt"))) {
                    DictionaryApplication.favoriteListEnVi.add(favWord);
                } else {
                    DictionaryApplication.favoriteListViEn.add(favWord);
                }
                Alerts successfulAlert = new Alerts();
                successfulAlert.showAlertInfo("Successful", "Added to favorites successfully!");
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void pronounce() {
        Sound.TextToSpeech.TextSpeech(word);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictChoiceBox.getItems().addAll(dicts);
        dictChoiceBox.setValue(dicts[0]);
        searchList.setVisible(false);
        speaker.setVisible(false);
        addToFav.setVisible(false);
    
        pronounceTooltip.setShowDelay(Duration.millis(250));
        pronounceTooltip.setHideDelay(Duration.millis(0));
        favoriteTooltip.setShowDelay(Duration.millis(250));
        favoriteTooltip.setHideDelay(Duration.millis(0));
    }
}
