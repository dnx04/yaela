package Sound;

import java.sql.*;

public class Sound {

    public static void wordSearch(String target) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:D:/avdict.db");
            connection.setAutoCommit(true);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(target);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2)
                        + " - " + resultSet.getString(5));
                TextToSpeech.TextSpeech(resultSet.getString(2));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
