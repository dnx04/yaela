package Database;

import java.sql.*;
import java.util.ArrayList;

// POC, not final
public class QueryEngine {
    public static Connection c= null;
    public QueryEngine(String database) {
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + database);
            c.setAutoCommit(false);
        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public ResultSet makeQuery(String query){
        Statement st = null;
        ResultSet rs = null;
        try {
            st = c.createStatement();
            // SQLiLite?
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    // custom vocabulary, implement later, after done all basic GUI functionalities.
    public void addWord(){

    }
    public void modifyWord(){

    }
    public void deleteWord(){

    }
    public static void insertWord(String newWord, String meanWord) {
        String insertQuery = "INSERT INTO av (word, html) " + " VALUES (?, ?)";

        try (PreparedStatement preparedStatement = c.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, newWord);
            preparedStatement.setString(2, meanWord);

            preparedStatement.executeUpdate();
            System.out.println("INSERT operation executed successfully.");
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                c.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
    }

    // Hàm thực hiện DELETE
    public static void deleteWord(String newWord) {
        String deleteQuery = "DELETE FROM av WHERE word = ?";
        try (PreparedStatement preparedStatement = c.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, newWord);

            preparedStatement.executeUpdate();
            System.out.println("DELETE operation executed successfully.");
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            c.rollback();
        } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
        }
    }

    public static boolean searchWord(String word) {
        String selectQuery = "SELECT * FROM av WHERE word = ?";
        int rowCount = 0;
        ArrayList<String> listWord = new ArrayList<>();

        try (PreparedStatement preparedStatement = c.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, word);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    String wordResult = resultSet.getString(2);
                    listWord.add(wordResult);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listWord.size() == 0;
    }





//    public static void main( String args[] ) {
//
//        Connection c = null;
//        Statement stmt = null;
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:./avdict.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM 'av' LIMIT 0,30;" );
//
//            while ( rs.next() ) {
//                System.out.println(rs.getString(4));
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//    }
}