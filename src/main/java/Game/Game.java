package Game;

//import com.google.common.collect.SortedMultiset;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public abstract class Game {
    protected int score = 0;
    protected ArrayList<Integer> highscore = new ArrayList<>();
    public abstract void init() throws SQLException;

    public abstract void setState(KeyEvent ke) throws SQLException;

    public ArrayList<Integer> getHighscore() {
        Collections.sort(highscore, Collections.reverseOrder());
        return highscore;
    }
}
