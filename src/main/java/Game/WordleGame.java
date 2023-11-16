package Game;

import Database.QueryEngine;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.management.Query;
import java.io.File;
import java.io.RandomAccessFile;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordleGame extends Game {
    protected enum TileState {BLANK, NOT_CONTAIN, WRONG_POSITION, CORRECT};
    protected enum GameState {IN_PROGRESS, LOSE, WIN}
    private char[][] word;
    private String pick;
    private Boolean[] exists;
    private TileState[][] ts;
    private int turn;
    private int cur;
    private GameState gs;
    private final QueryEngine qe = new QueryEngine("./wordlelist.db");

    WordleGame() throws SQLException {
        word = new char[6][5];
        ts = new TileState[6][5];
        exists = new Boolean[26];
        turn = 0;
        cur = 0;
        gs = GameState.IN_PROGRESS;
        ResultSet rs = qe.makeQuery("SELECT * FROM wordlist ORDER BY RANDOM() LIMIT 1");
        pick = rs.getString(1);
        for(int i = 0; i < 5; ++i){
            exists[pick.charAt(i) - 'a'] = true;
        }
        for(int i = 0; i < 6; ++i){
            for(int j = 0; j < 5; ++j){
                ts[i][j] = TileState.BLANK;
            }
        }
    }
    public void setState(KeyEvent ke){
        if(ke.getCode() == KeyCode.ENTER){
            if(cur == 5){
                String now = new String(word[turn]).toLowerCase();
                // check valid, do later
                boolean win = true;
                for (int i = 0; i < 5; ++i){
                    if(now.charAt(i) == pick.charAt(i)){
                        ts[turn][i] = TileState.CORRECT;
                    } else if(exists[now.charAt(i) - 'a']){
                        ts[turn][i] = TileState.WRONG_POSITION;
                    } else {
                        ts[turn][i] = TileState.NOT_CONTAIN;
                    }
                    win &= (ts[turn][i] == TileState.CORRECT);
                }
                if(win){
                    gs = GameState.WIN;
                    return;
                }
                ++turn;
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
                System.out.println(word[turn][cur]);
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

    public static void main(String[] args) throws SQLException {
        WordleGame wg = new WordleGame();
        System.out.println(wg.getPick());
    }
}
