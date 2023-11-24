package GUIVersion.controllers;

import Database.QueryEngine;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WordbaseController {
    private final String[] choiceBox = {"Thêm từ", "Xóa từ"};
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
    @FXML
    private Label insertLabel;

    @FXML
    private Label deleteLabel;

    public void initialize() {
        insertChoiceBox.getItems().addAll(choiceBox);
        deleteChoiceBox.getItems().addAll(choiceBox);

        // Chọn mặc định là "Thêm từ"
        insertChoiceBox.setValue(("Thêm từ"));
        showScene(sceneInsert);


        insertChoiceBox.setOnAction(event -> {

            String selectedChoice = insertChoiceBox.getValue();
            if ("Thêm từ".equals(selectedChoice)) {
                showScene(sceneInsert);
                deleteChoiceBox.setValue(null);
            } else if ("Xóa từ".equals(selectedChoice)) {
                showScene(sceneDelete);
                deleteChoiceBox.setValue("Xóa từ");
            }
        });

        deleteChoiceBox.setOnAction(event -> {
            String selectedChoice = deleteChoiceBox.getValue();
            if ("Thêm từ".equals(selectedChoice)) {
                showScene(sceneInsert);
                insertChoiceBox.setValue("Thêm từ");
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
        //insertLabel.setVisible(scene == sceneInsert);
        //deleteLabel.setVisible(scene == sceneDelete);
    }

    private void handleAddButton() {
        String newWordText = newWord.getText().trim();
        String meanWordText = meanWord.getText().trim();

        if (!newWordText.isEmpty() && !meanWordText.isEmpty()) {
            if (QueryEngine.searchWord(newWordText)) {
                QueryEngine.insertWord(newWordText, meanWordText);
                System.out.println("Từ mới: " + newWordText);
                System.out.println("Giải nghĩa: " + meanWordText);
            } else {
                System.out.println("Từ đã tồn tại trong cơ sở dữ liệu.");
                // Xử lý thông báo hoặc logic khác tùy thuộc vào yêu cầu của bạn
            }
        } else {
            System.out.println("Vui lòng nhập từ và giải nghĩa.");
            // Xử lý thông báo hoặc logic khác tùy thuộc vào yêu cầu của bạn
        }
    }

    private void handleDeleteButton() {
        String newDeleteWord = deleteWord.getText().trim();

        if (!newDeleteWord.isEmpty()) {
            if (!QueryEngine.searchWord(newDeleteWord)) {
                QueryEngine.deleteWord(newDeleteWord);
                System.out.println("Từ xóa: " + newDeleteWord);
            } else {
                System.out.println("Không tồn tại trong cơ sở dữ liệu.");
                // Xử lý thông báo hoặc logic khác tùy thuộc vào yêu cầu của bạn
            }
        } else {
            System.out.println("Vui lòng nhập từ khác.");
            // Xử lý thông báo hoặc logic khác tùy thuộc vào yêu cầu của bạn
        }
    }
}
