package Game;

import Database.QueryEngine;
import javafx.scene.input.KeyEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MCQGame extends Game {
    private List<String> words;
    private String currentQuestion;
    private String correctAnswer;
    private int score;
    private int errorCount;
    private boolean gameEnded;

    private static QueryEngine queryEngine = new QueryEngine("avdict.db");

    public MCQGame() {
        score = 0;
        init();
    }

    private void initializeWordsFromDatabase() {
        ResultSet resultSet = queryEngine.makeQuery("SELECT word FROM av ORDER BY RANDOM() LIMIT 1000;");
        try {
            while (resultSet.next()) {
                words.add(resultSet.getString("word"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getVietnameseWordMeaningFromDatabase(String englishWord) {
        String query = "SELECT description FROM av WHERE word = '" + englishWord + "'";
        ResultSet resultSet = queryEngine.makeQuery(query);

        try {
            if (resultSet.next()) {
                return resultSet.getString("description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setState(String choice) {
        switch (choice) {
            case "A":
                checkUserAnswer(0);
                break;
            case "B":
                checkUserAnswer(1);
                break;
            case "C":
                checkUserAnswer(2);
                break;
            case "D":
                checkUserAnswer(3);
                break;
            default:
                break;
        }
        selectNewQuestion();
    }

    private void checkUserAnswer(int selectedIndex) {
        if (selectedIndex >= 0 && selectedIndex < 4) {
            String selectedAnswer = getChoices().get(selectedIndex);
            if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
            } else {
                errorCount++;
            }
            words.remove(correctAnswer);
        }

        if (errorCount >= 3) {
            gameEnded = true;
            highscore.add(score);
        } else {
            Collections.shuffle(words);
            selectNewQuestion();
        }
    }

    public void selectNewQuestion(){
        Random random = new Random();
        List<String> questionChoices = getChoices();

        correctAnswer = questionChoices.get(random.nextInt(questionChoices.size()));

        currentQuestion = getVietnameseWordMeaningFromDatabase(correctAnswer);
    }

    public String getCurrentQuestion() {
        return currentQuestion;
    }
    public List<String> getChoices(){
        return words.subList(0, 4);
    }

    public int getScore() {
        return score;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    @Override
    public void init() {
        words = new ArrayList<>();
        initializeWordsFromDatabase();
        errorCount = 0;
        gameEnded = false;
        score = 0;
        selectNewQuestion();
    }


    @Override
    public void setState(KeyEvent ke) throws SQLException {

    }
}