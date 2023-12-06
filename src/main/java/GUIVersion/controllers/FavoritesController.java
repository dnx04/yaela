package GUIVersion.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import GUIVersion.DictionaryApplication;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
        

        favoriteListView.setCellFactory(new Callback<ListView<String>,ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new CustomListCell();
            }
        });
    }

    private void updateDict(String text) {
        if (text.equals("Anh - Việt")) {
            list = DictionaryApplication.favoriteListEnVi;
        } else {
            list = DictionaryApplication.favoriteListViEn;
        }
        updateListView(searchBox.getText());
    }

    private void updateListView(String text) {
        favoriteListView.getItems().clear();
        
        // if (dictChoiceBox.getValue().equals("Anh - Việt")) {
        //     list = DictionaryApplication.favoriteListEnVi;
        // } else {
        //     list = DictionaryApplication.favoriteListViEn;
        // }

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
        private String lastItem;

        public CustomListCell() {
            super();
            button = new Button("🗑");
            
            button.setId("deleteBtn");
            button.setPrefSize(40, 40);
            button.setStyle("-fx-font-size: 18px;-fx-background-radius: 20;-fx-border-radius: 100;-fx-background-size: contain;-fx-background-repeat: no-repeat;-fx-background-position: center;");
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
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                button.setOnAction(event -> {
                    System.out.println("Button clicked for item: " + item);
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
}
