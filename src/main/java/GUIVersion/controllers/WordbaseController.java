package GUIVersion.controllers;

import Database.QueryEngine;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.QueryEngine.c;
import static GUIVersion.controllers.DictionaryController.contentWebView;

public class WordbaseController {
    private static final int WORD_SEARCH_LIMIT = 10;
    private static final String NO_WORD_NOTI = "No words found!";
    private final String[] choiceBox = {"Thêm từ", "Xóa từ"};
    public ListView searchListDelete;
    public WebView webView;
    public AnchorPane searchPane;
    @FXML
    private ChoiceBox<String> insertChoiceBox;
    @FXML
    private ChoiceBox<String> deleteChoiceBox;
    @FXML
    private TextField newWord;
    @FXML
    private TextField deleteWord;
    @FXML
    private TextArea meanWord;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private AnchorPane sceneInsert;
    @FXML
    private AnchorPane sceneDelete;
    private Alerts alerts = new Alerts();
    private static String contentWebView = "";
    private static String wordWillDelete = "";

    public void search(javafx.scene.input.KeyEvent e) throws SQLException {
        String inputText = deleteWord.getText().trim();
        searchListDelete.getItems().clear();
        ObservableList<String> listWord = searchListDelete.getItems();
        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.clear();
        WebEngine webEngine = webView.getEngine();
        if (inputText.isEmpty() || inputText == null) {
            searchListDelete.setVisible(false);
        } else {
            try {
                String query = "SELECT * FROM av WHERE word LIKE ? LIMIT " + WORD_SEARCH_LIMIT;
                try (PreparedStatement preparedStatement = c.prepareStatement(query)) {
                    preparedStatement.setString(1, inputText + "%");
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            String wordResult = resultSet.getString(2);
                            String htmlResult = resultSet.getString(3);
                            listWord.add(wordResult);
                            stringArrayList.add(htmlResult);
                        }
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                if (listWord.size() == 0) {
                    listWord.add(NO_WORD_NOTI);
                }
                // Set height of search list appropriate to the size of list
                searchListDelete.prefHeightProperty().bind(Bindings.size(listWord).multiply(41));
                searchListDelete.setVisible(true);
                if (listWord.size() != 0 && !(listWord.get(0).equals(NO_WORD_NOTI))) {
                    searchListDelete.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && !newValue.equals(NO_WORD_NOTI)) {
                            wordWillDelete = (String) newValue;
                            int selectedIndex = searchListDelete.getSelectionModel().getSelectedIndex();
                            if (selectedIndex >= 0 && selectedIndex < stringArrayList.size()) {
                                contentWebView = "<html><head><style>body {font-family: \"Calibri\", \"Helvetica\", sans-serif;}</style></head><body>"
                                        + stringArrayList.get(selectedIndex) + "</body></html>";
                                searchListDelete.setVisible(false);
                            }
                            deleteWord.setText(wordWillDelete);
                        } else {
                            wordWillDelete = "";
                            contentWebView = "";
                        }
                        webEngine.loadContent(contentWebView);
                    });
                }
                webView.setVisible(true);
                // Make search list disappear when clicking outside search list region
                searchPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (!searchListDelete.getBoundsInParent().contains(event.getX(), event.getY())) {
                        searchListDelete.setVisible(false);
                    }
                });
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void initialize() {
        insertChoiceBox.getItems().addAll(choiceBox);
        deleteChoiceBox.getItems().addAll(choiceBox);
        // Chọn mặc định là "Thêm từ"
        insertChoiceBox.setValue(("Thêm từ"));
        showScene(sceneInsert);
        deleteWord.setOnKeyReleased(event -> {
            try {
                search(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        insertChoiceBox.setOnAction(event -> {
            String selectedChoice = insertChoiceBox.getValue();
            if ("Thêm từ".equals(selectedChoice)) {
                showScene(sceneInsert);
                deleteChoiceBox.setValue(null);
            } else if ("Xóa từ".equals(selectedChoice)) {
                showScene(sceneDelete);
                deleteChoiceBox.setValue("Xóa từ");
                newWord.clear();
                meanWord.setText("<p>                                      </p>");
            }
        });

        deleteChoiceBox.setOnAction(event -> {
            String selectedChoice = deleteChoiceBox.getValue();
            if ("Thêm từ".equals(selectedChoice)) {
                showScene(sceneInsert);
                insertChoiceBox.setValue("Thêm từ");
                deleteWord.clear();
                WebEngine webEngine = webView.getEngine();
                webEngine.loadContent("");
            } else if ("Xóa từ".equals(selectedChoice)) {
                showScene(sceneDelete);
                insertChoiceBox.setValue(null);
            }
        });
        addButton.setOnAction(event -> handleAddButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }
    private void showScene(AnchorPane scene) {
        sceneInsert.setVisible(scene == sceneInsert);
        sceneDelete.setVisible(scene == sceneDelete);
    }
    private void handleAddButton() {
        String newWordText = newWord.getText().trim();
        String meanWordText = meanWord.getText().trim();
        if (!newWordText.isEmpty() && !meanWordText.isEmpty()) {
            if (QueryEngine.searchWord(newWordText)) {
                Optional<ButtonType> option = alerts.showConfirmation("Wait", "Bạn có chắc chắc muốn thêm từ này?");
                if (option.get() == ButtonType.OK) {
                    QueryEngine.insertWord(newWordText, meanWordText);
                    alerts.showAlertInfo("Add Word", "Bạn đã thêm từ thành công.");
                    newWord.clear();
                    meanWord.setText("<p>                                      </p>");
                }
            } else {
                alerts.showAlertInfo("Info", "Từ này đã tồn tại");
            }
        } else {
            alerts.showAlertWarning("Warning", "Vui lòng nhập từ và giải nghĩa");
            alerts.showAlertWarning("Warning", "Vui lòng nhập đầy đủ từ và giải nghĩa");
        }
    }

    private void handleDeleteButton() {
        Optional<ButtonType> option = alerts.showConfirmation("Wait", "Bạn có chắc chắc muốn xóa từ này?");
        if (option.get() == ButtonType.OK) {
            if (!wordWillDelete.isEmpty()) {
                if (!QueryEngine.searchWord(wordWillDelete)) {
                    QueryEngine.deleteWord(wordWillDelete);
                    alerts.showAlertInfo("Delete Word", "Bạn đã xóa từ thành công.");
                    deleteWord.clear();
                    WebEngine webEngine = webView.getEngine();
                    webEngine.loadContent("");
                } else {
                    alerts.showAlertWarning("Warning", "Không tồn tại trong cơ sở dữ liệu.");
                }
            } else {
                alerts.showAlertWarning("Warning", "Vui lòng nhập từ để xóa");
                alerts.showAlertWarning("Warning", "Vui lòng chọn từ để xóa");
            }
        }
    }
}