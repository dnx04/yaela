package GUIVersion.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import GUIVersion.DictionaryApplication;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.Duration;

public class FavoritesController implements Initializable {
    @FXML
    private ChoiceBox<String> dictChoiceBox;
    
    @FXML
    private TextField searchBox;

    @FXML
    private ListView<String> favoriteListView = new ListView<>();
    private ArrayList<String[]> list;

    @FXML
    private Button leftBtn;

    @FXML
    private Button rightBtn;

    @FXML
    private Label flashcard;

    private String frontContent = "";
    private String backContent = "";
    private static int index = -1;

    @FXML
    private Button modifyBtn;

    public void flipAction(MouseEvent event) {

        if (flashcard.getText().equals(frontContent)) {
            flip(flashcard, backContent);
        } else {
            flip(flashcard, frontContent);
        }
        
    }

    private void flip(Label front, String back) {
        ScaleTransition stHideFront = new ScaleTransition(Duration.millis(200), front);
        stHideFront.setFromY(1);
        stHideFront.setToY(0);

        ScaleTransition stShowBack = new ScaleTransition(Duration.millis(200), front);
        stShowBack.setFromY(0);
        stShowBack.setToY(1);

        stHideFront.setOnFinished(e -> {
            front.setText(back);
            stShowBack.play();
        });
        stHideFront.play();
    }

    private void moveLeft() {
        index--;
        String[] word = list.get(index);
        frontContent = word[0];
        backContent = word[1];
        if (index == 0) {
            leftBtn.setVisible(false);
        } else {
            leftBtn.setVisible(true);
        }
        if (index == list.size() - 1) {
            rightBtn.setVisible(false);
        } else {
            rightBtn.setVisible(true);
        }
        flashcard.setText(frontContent);
        rightBtn.setVisible(true);
    }

    private void moveRight() {
        index++;
        String[] word = list.get(index);
        frontContent = word[0];
        backContent = word[1];
        if (index == 0) {
            leftBtn.setVisible(false);
        } else {
            leftBtn.setVisible(true);
        }
        if (index == list.size() - 1) {
            rightBtn.setVisible(false);
        } else {
            rightBtn.setVisible(true);
        }
        flashcard.setText(frontContent);
        leftBtn.setVisible(true);
    }

    public void changeFlashcardContent(MouseEvent event) {
        frontContent = favoriteListView.getSelectionModel().getSelectedItem();
        index = binarySearch(frontContent, list);
        backContent = (list.get(index))[1];
        if (frontContent != null) {
            flashcard.setText(frontContent);
        }
    }

    public void modifyFlashcardContent(MouseEvent e) throws IOException {
        DialogPane pane = FXMLLoader.load(getClass().getResource("/views/addtofavdialog.fxml"));
        TextField wordField = (TextField) pane.lookup("#wordDialog");
        TextArea noteField = (TextArea) pane.lookup("#noteDialog");
        Dialog<Button> dialog = new Dialog<>();

        dialog.setDialogPane(pane);
        dialog.setTitle("Modify favorite");

        wordField.setText(frontContent);
        noteField.setText(backContent);

        Button btOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        // ArrayList<String[]> listFav;
        // if (dictionary.equals(getDict("Anh - Việt"))) {
        //     listFav = DictionaryApplication.favoriteListEnVi;
        // } else {
        //     listFav = DictionaryApplication.favoriteListViEn;
        // }

        Alerts alert = new Alerts();

        btOK.addEventFilter(ActionEvent.ACTION, event -> {
            String[] favWord = {wordField.getText(), noteField.getText()};
            if (favWord[0] == "" || favWord[0] == null || favWord[1] == "" || favWord[1] == null) {
                alert.showAlertWarning("Warning", "No fields are allowed to be left blank!");
                event.consume();
            } else if (binarySearch(favWord[0], list) != index && binarySearch(favWord[0], list) != -1) {
                alert.showAlertWarning("Warning", "This word has a duplicate in favorite list!");
                event.consume();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String[] favWord = {wordField.getText(), noteField.getText()};
                list.remove(index);
                index = findInsertIndex(favWord[0], list);
                if (favWord[0] != "" && favWord[0] != null && favWord[1] != "" && favWord[1] != null && index != -1) {
                    list.add(index, favWord);
                    Alerts successfulAlert = new Alerts();
                    successfulAlert.showAlertInfo("Successful", "Modified successfully!");
                    if (flashcard.getText().equals(frontContent)) {
                        flashcard.setText(favWord[0]);
                    } else if (flashcard.getText().equals(backContent)) {
                        flashcard.setText(favWord[1]);
                    }
                    frontContent = favWord[0];
                    backContent = favWord[1];
                    if (index == 0) {
                        leftBtn.setVisible(false);
                    } else {
                        leftBtn.setVisible(true);
                    }
                    if (index == list.size() - 1) {
                        rightBtn.setVisible(false);
                    } else {
                        rightBtn.setVisible(true);
                    }
                    updateListView(searchBox.getText());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dictChoiceBox.getItems().addAll("Anh - Việt", "Việt - Anh");
        dictChoiceBox.setValue("Anh - Việt");

        list = DictionaryApplication.favoriteListEnVi;
        updateListView("");

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            updateListView(newValue);
        });

        dictChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateDict(newValue);
        });

        leftBtn.setOnMouseClicked(e -> moveLeft());
        rightBtn.setOnMouseClicked(e -> moveRight());
        

        favoriteListView.setCellFactory(new Callback<ListView<String>,ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new CustomListCell();
            }
        });
        flashcard.setWrapText(true);
        setDefaultFlashcard();
        
    }

    private void updateDict(String text) {
        if (text.equals("Anh - Việt")) {
            list = DictionaryApplication.favoriteListEnVi;
        } else {
            list = DictionaryApplication.favoriteListViEn;
        }
        updateListView(searchBox.getText());
        setDefaultFlashcard();
    }

    private void setDefaultFlashcard() {
        if (list.size() != 0) {
            String[] firstWord = list.get(0);
            frontContent = firstWord[0];
            backContent = firstWord[1];
            index = 0;
            if (list.size() == 1) {
                rightBtn.setVisible(false);
            }
        } else {
            frontContent = "";
            backContent = "";
            index = -1;
            rightBtn.setVisible(false);
        }
        flashcard.setText(frontContent);
        leftBtn.setVisible(false);
    }

    private void updateListView(String text) {
        favoriteListView.getItems().clear();

        if (text.equals("") || text == null) {
            for (String[] word: list) {
                favoriteListView.getItems().add(word[0]);
            }
        } else {
            for (String[] word: list) {
                String wordTarget = word[0];
                if (text.length() <= wordTarget.length()) {
                    if (wordTarget.substring(0, text.length()).equals(text)) {
                        favoriteListView.getItems().add(wordTarget);
                    }
                }
            }
        }
    }

    private class CustomListCell extends ListCell<String> {
        private Button button;
        private Label label;
        private HBox hbox;
        private Pane pane;

        public CustomListCell() {
            button = new Button("🗑");
            button.setPrefSize(40, 40);
            button.setStyle("-fx-font-size: 18px;" 
                + "-fx-background-radius: 20;" 
                + "-fx-border-radius: 100;" 
                + "-fx-background-size: contain;" 
                + "-fx-background-repeat: no-repeat;" 
                + "-fx-background-position: center;");
            label = new Label();
            pane = new Pane();
            hbox = new HBox();

            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            hbox.setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (empty) {
                setGraphic(null);
            } else {
                button.setOnAction(event -> {
                    Alerts alert = new Alerts();
                    Optional<ButtonType> result = alert.showConfirmation("Confirm delete", "Are you sure to delete this word from the favorite list?");
                    if (result.get() == ButtonType.OK) {
                        favoriteListView.getItems().removeIf(word -> word.equals(item));
                        int deleteIndex = binarySearch(item, list);
                        if (deleteIndex == index) {
                            if (index != list.size() - 1) {
                                moveRight();
                                --index;
                                if (index == 0) {
                                    leftBtn.setVisible(false);
                                }
                            } else {
                                moveLeft();
                                rightBtn.setVisible(false);
                            }
                        } else if (deleteIndex < index) {
                            --index;
                        }
                        list.remove(deleteIndex);
                        
                    }
                });
                label.setText(item);

                Tooltip tooltip = new Tooltip("Delete");
                tooltip.setShowDelay(Duration.millis(250));
                tooltip.setHideDelay(Duration.millis(0));
                button.setTooltip(tooltip);

                setGraphic(hbox);
            }
        }
    }

    private int binarySearch(String word, ArrayList<String[]> list) {
        int start = 0;
        int end = list.size() - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            int cmp = (list.get(mid))[0].compareTo(word);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return -1;
    }

    private int findInsertIndex(String word, ArrayList<String[]> list) {
        int start = 0;
        int end = list.size() - 1;
        while (start <= end) {
            int mid = start + (end - start) / 2;
            int cmp = (list.get(mid))[0].compareTo(word);
            if (cmp == 0) {
                return -1;
            } else if (cmp < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return end + 1;
    }
}
