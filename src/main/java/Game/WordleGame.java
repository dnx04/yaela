package Game;

import Database.QueryEngine;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WordleGame extends Game {
    public enum TileState {BLANK, NOT_CONTAIN, WRONG_POSITION, CORRECT};
    public enum GameState {IN_PROGRESS, LOSE, WIN}
    private char[][] word;
    private boolean valid;
    private String pick;
    private Boolean[] exists;
    private TileState[][] ts;
    private TileState[] keys;
    private int turn;
    private int cur;
    private GameState gs;
    private final QueryEngine qe = new QueryEngine("./wordlelist.db");
    private ArrayList<Integer> highscore;

    public WordleGame() throws SQLException {
        highscore = new ArrayList<>();
        score = 0;
        init();
    }

    @Override
    public void init() throws SQLException {
        word = new char[6][5];
        valid = true;
        keys = new TileState[26];
        ts = new TileState[6][5];
        exists = new Boolean[26];
        turn = 0;
        cur = 0;
        gs = GameState.IN_PROGRESS;
        ResultSet rs = qe.makeQuery("SELECT * FROM wordlist ORDER BY RANDOM() LIMIT 1");
        pick = rs.getString(1);
        for(int i = 0; i < 26; ++i){
            exists[i] = false;
        }
        for(int i = 0; i < 5; ++i){
            exists[pick.charAt(i) - 'a'] = true;
        }
        for(int i = 0; i < 26; ++i){
            keys[i] = TileState.BLANK;
        }
        for(int i = 0; i < 6; ++i){
            for(int j = 0; j < 5; ++j){
                ts[i][j] = TileState.BLANK;
            }
        }
    }

    public void setState(KeyEvent ke) throws SQLException {
        System.out.println("turn: " + turn);
        System.out.println("cur: " + cur);
        System.out.println(gs);

        if(ke.getCode() == KeyCode.ENTER){
            if(cur == 5){
                String now = new String(word[turn]).toLowerCase();
                ResultSet rs = qe.makeQuery(String.format("SELECT * from wordlist WHERE word = '%s' ", now));
                if(!rs.next()){
                    valid = false;
                    return;
                }
                valid = true;
                // check valid, do later
                boolean win = true;
                for (int i = 0; i < 5; ++i){

                    if(now.charAt(i) == pick.charAt(i)){
                        ts[turn][i] = TileState.CORRECT;
                    } else if(exists[now.charAt(i) - 'a'] && now.charAt(i) != pick.charAt(i)){
                        ts[turn][i] = TileState.WRONG_POSITION;
                    } else {
                        ts[turn][i] = TileState.NOT_CONTAIN;
                    }
                    keys[now.charAt(i) - 'a'] = ts[turn][i];
                    win &= (ts[turn][i] == TileState.CORRECT);
                }
                if(win){
                    gs = GameState.WIN;
                    score++;
                    return;
                }
                ++turn;
                cur = 0;

                if(turn == 6){
                    gs = GameState.LOSE;
                    return;
                }
            }

        } else if (ke.getCode() == KeyCode.BACK_SPACE) {
            if (cur > 0) {
                word[turn][cur - 1] = 0;
                --cur;
            }
        } else if(ke.getCode().isLetterKey()){
            if(cur < 5) {
                word[turn][cur] = ke.getCode().toString().charAt(0);
                ++cur;
            }
        }
    }

    public char[][] getWord() {
        return word;
    }

    public GameState getGs() {
        return gs;
    }

    public TileState[][] getTs() {
        return ts;
    }

    public int getCur() {
        return cur;
    }

    public int getTurn() {
        return turn;
    }

    public String getPick() {
        return pick;
    }

    public Boolean[] getExists() { return exists; }

    public TileState[] getKeys() {
        return keys;
    }

    public int getScore() {
        return score;
    }

    public boolean isValid() {
        return valid;
    }

    public void replay() throws SQLException {
        if (gs == GameState.LOSE) {
            highscore.add(score);
            score = 0;
        }
        init();
    }

    public static void main(String[] args) throws SQLException {
        WordleGame wg = new WordleGame();
        System.out.println(wg.getPick());
    }
}
