package Database;

import java.sql.*;

// POC, not final
public class QueryEngine {
    private Connection c = null;
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