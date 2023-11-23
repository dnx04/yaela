package Game;

import com.google.common.collect.SortedMultiset;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;

public abstract class Game {
    protected int score = 0;
    protected SortedMultiset<Integer> high_score;
    public abstract void init() throws SQLException;

    public abstract void setState(KeyEvent ke) throws SQLException;
}
