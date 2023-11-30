package Game;

//import com.google.common.collect.SortedMultiset;
import javafx.scene.input.KeyEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Game {
    protected int score = 0;
    protected ArrayList<Integer> highscore;
    public abstract void init() throws SQLException;

    public abstract void setState(KeyEvent ke) throws SQLException;
}
