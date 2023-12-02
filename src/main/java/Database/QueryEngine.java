package Database;

import java.sql.*;

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
        Statement st;
        ResultSet rs;
        try {
            st = c.createStatement();
            // SQLiLite?
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public static void main(String[] args) {
        QueryEngine queryEngine = new QueryEngine("avdict.db");
        String q = "CREATE INDEX idx ON av (word)";
        ResultSet rs = queryEngine.makeQuery(q);
        System.out.println("Successful!");
    }
}