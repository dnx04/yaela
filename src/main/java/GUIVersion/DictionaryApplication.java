package GUIVersion;



import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import CommandVersion.Word;
import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DictionaryApplication extends Application {

    public static ArrayList<String[]> favoriteListEnVi = new ArrayList<>();
    public static ArrayList<String[]> favoriteListViEn = new ArrayList<>();

    private String enViPath = System.getProperty("user.dir") + "/src/main/java/GUIVersion/resources/data/favoriteEnVi.csv";
    private String viEnPath = System.getProperty("user.dir") + "/src/main/java/GUIVersion/resources/data/favoriteViEn.csv";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
            CSVReader reader1 = new CSVReaderBuilder(new FileReader(enViPath)).withCSVParser(parser).build();
            List<String[]> rows1 = reader1.readAll();
            for (String[] row: rows1) {
                favoriteListEnVi.add(row);
            }
            reader1.close();
            CSVReader reader2 = new CSVReaderBuilder(new FileReader(viEnPath)).withCSVParser(parser).build();
            List<String[]> rows2 = reader2.readAll();
            for (String[] row: rows2) {
                favoriteListViEn.add(row);
            }
            reader2.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("yaela");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            saveDataToFile();
            System.exit(0);
        });

        stage.show();
    }

    private void saveDataToFile() {
        try {
            CSVWriter writer1 = new CSVWriter(new FileWriter(enViPath), '|', 
                CSVWriter.NO_QUOTE_CHARACTER, 
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                CSVWriter.DEFAULT_LINE_END);
            writer1.writeAll(favoriteListEnVi);
            writer1.close();
            CSVWriter writer2 = new CSVWriter(new FileWriter(viEnPath), '|', 
                CSVWriter.NO_QUOTE_CHARACTER, 
                CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                CSVWriter.DEFAULT_LINE_END);
            writer2.writeAll(favoriteListViEn);
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
