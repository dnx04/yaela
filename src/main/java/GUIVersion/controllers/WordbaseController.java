package GUIVersion.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static Database.QueryEngine.c;


public class WordbaseController {
    //public  Connection c = DriverManager.getConnection("jdbc:sqlite:avdict.db");
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
    private TextArea explain;
    @FXML
    private TextArea example;
    @FXML
    private TextField ipa;
    @FXML
    private TextField type;
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

    public WordbaseController() throws SQLException {
    }

//    public WordbaseController() throws SQLException {
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:avdict.db");
//            c.setAutoCommit(false);
//        } catch (ClassNotFoundException | SQLException e) {
//            // Handle exceptions appropriately, e.g., log or throw a runtime exception
//            throw new RuntimeException(e);
//        }
//    }

    public void insertWord(String newWord, String explain) {
        String insertQuery = "INSERT INTO av (word, html) " + " VALUES (?, ?)";
        try (PreparedStatement preparedStatement = c.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, newWord);
            preparedStatement.setString(2, explain);
            preparedStatement.executeUpdate();
            c.setAutoCommit(false);
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
    }

//    public void deleteWord(String deleteWord) {
//        String sanitizedDeleteWord = sanitizeAndValidate(deleteWord);
//
//        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:your_database.db");
//             Statement statement = connection.createStatement()) {
//
//            connection.setAutoCommit(false);
//
//            String deleteQuery = "DELETE FROM av WHERE word = '" + sanitizedDeleteWord + "'";
//            statement.executeUpdate(deleteQuery);
//
//            connection.commit();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String sanitizeAndValidate(String input) {
//        return input.replaceAll("[^a-zA-Z0-9]", "");
//    }


    public void deleteWord(String deleteWord) {
        String deleteQuery = "DELETE FROM av WHERE word = ?";
        try (PreparedStatement preparedStatement = c.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, deleteWord);
            preparedStatement.executeUpdate();
            c.setAutoCommit(false);
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            c.rollback();
        } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
        }
    }

    public boolean searchWord(String word) {
        String selectQuery = "SELECT * FROM av WHERE word = ?";
        int rowCount = 0;
        ArrayList<String> listWord = new ArrayList<>();
        try (PreparedStatement preparedStatement = c.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, word);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String wordResult = resultSet.getString(2);
                    listWord.add(wordResult);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listWord.size() == 0;
    }

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

    public void initialize() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:avdict.db");
            c.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions appropriately, e.g., log or throw a runtime exception
            throw new RuntimeException(e);
        }
        //if (c != null ) c.close();
        //WordbaseController w = new WordbaseController();
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
                explain.clear();
                ipa.clear();
                example.clear();
                type.clear();
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

    public ArrayList<String> tach(String s) {
        ArrayList<String> ans = new ArrayList<>();
        String[] lines = s.split("\n");
        for (String line : lines) {
            String text = line.trim();
            if (text.length() > 0) {
                ans.add(text);
            }
        }
        return ans;
    }

    private void handleAddButton() {
        String newWordText = newWord.getText().trim();
        String pronounce = ipa.getText().trim();
        String getType = type.getText().trim();
        String getExplain = explain.getText();
        String getExample = example.getText();
        ArrayList<String> explainList = tach(getExplain);
        ArrayList<String> exampleList = tach(getExample);

        StringBuilder htmlBuilder = new StringBuilder("<h1>" + newWordText + "</h1><h3><i>" + pronounce + "</i></h3><h2>" + getType
            + "</h2>"); //"<ul><li>" + getExplain+ "</li></ul>";
        if (explainList.size() > 0) {
            htmlBuilder = htmlBuilder.append("<h2>Explain</h2><ul>");
            for (String ex : explainList) {
                htmlBuilder = htmlBuilder.append("<li>").append(ex).append("</li>");
            }
            htmlBuilder = htmlBuilder.append("</ul>");
        }
        if (exampleList.size() > 0) {
            htmlBuilder = htmlBuilder.append("<h2>Example:</h2><ul>");
            for (String ex : exampleList) {
                htmlBuilder = htmlBuilder.append("<li>").append(ex).append("</li>");
            }
            htmlBuilder = htmlBuilder.append("</ul>");
        }
        String html = htmlBuilder.toString();
        if (!newWordText.isEmpty() && !html.isEmpty()) {
            if (searchWord(newWordText)) {
                Optional<ButtonType> option = alerts.showConfirmation("Wait", "Bạn có chắc chắc muốn thêm từ này?");
                if (option.get() == ButtonType.OK) {
                    insertWord(newWordText, html);
                    alerts.showAlertInfo("Add Word", "Bạn đã thêm từ thành công.");
                    newWord.clear();
                    explain.clear();
                    ipa.clear();
                    example.clear();
                    type.clear();
                }
            } else {
                alerts.showAlertInfo("Info", "Từ này đã tồn tại");
            }
        } else {
            alerts.showAlertWarning("Warning", "Vui lòng nhập đầy đủ từ và giải nghĩa");
        }
    }

    private void handleDeleteButton() {
        //searchListDelete.setVisible(false);
        Optional<ButtonType> option = alerts.showConfirmation("Wait", "Bạn có chắc chắc muốn xóa từ này?");
        if (option.get() == ButtonType.OK) {
            if (!wordWillDelete.isEmpty()) {
                if (!searchWord(wordWillDelete)) {
                    System.out.println(wordWillDelete);
                    deleteWord(wordWillDelete);
                    alerts.showAlertInfo("Delete Word", "Bạn đã xóa từ thành công.");
                    deleteWord.clear();
                    WebEngine webEngine = webView.getEngine();
                    webEngine.loadContent("");
                } else {
                    alerts.showAlertWarning("Warning", "Không tồn tại trong cơ sở dữ liệu.");
                }
            } else {
                alerts.showAlertWarning("Warning", "Vui lòng chọn từ để xóa");
            }
        }
    }
}