package DBHandler;

import java.sql.*;
import java.util.Properties;

public class DbHandler {
    private final String cleanTextTableName = "vi_cleanText";
    private PreparedStatement psCleanText;
    public Connection connection;


    public DbHandler( String usrname, String pwd, String dbName) {
//        connection = DbConnection.connect( usrname, pwd, dbName);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties properties = new Properties();
            properties.put("user", usrname );
            properties.put("password", pwd);
            properties.put("database", dbName);
            properties.put("useUnicode", "true");
            properties.put("characterEncoding", "utf8");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName, properties);

            psCleanText = connection.prepareStatement("INSERT INTO " + cleanTextTableName + " VALUES (null, ?, ?, ?, ?)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createCleanTextTable() throws SQLException {
        // wiki must be already created
        // String creteWiki = "CREATE DATABASE wiki;";
        ResultSet rs = null;
        Statement st = connection.createStatement();
        String createCleanTextTable = "CREATE TABLE " + cleanTextTableName + " (" + " id int UNSIGNED NOT NULL AUTO_INCREMENT,"
                + " cleanText MEDIUMBLOB NOT NULL," + " processed BOOLEAN, " + " page_id int UNSIGNED NOT NULL, "
                + " text_id int UNSIGNED NOT NULL, " + " PRIMARY KEY id (id)"
                + " ) MAX_ROWS=250000 AVG_ROW_LENGTH=10240 CHARACTER SET utf8;";

        // If database does not exist create it, if it exists delete it and create an empty one.
        System.out.println("Checking if the TABLE=" + cleanTextTableName + " already exist.");
        try {
            rs = st.executeQuery("SHOW TABLES;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean resText = false;
        try {

            while (rs.next()) {
                String str = rs.getString(1);
                if (str.contentEquals(cleanTextTableName))
                    resText = true;
            }
            if (resText) {
                System.out.println("TABLE = " + cleanTextTableName + " already exist deleting.");
                st.execute("DROP TABLE " + cleanTextTableName + ";");
            }


            // creating TABLE=cleanText
            // System.out.println("\nCreating table:" + createCleanTextTable);
            System.out.println("\nCreating table:" + cleanTextTableName);
            st.execute(createCleanTextTable);
            System.out.println("TABLE = " + cleanTextTableName + " successfully created.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCleanText(String text, String page_id, String text_id) {
        // System.out.println("inserting in cleanText: ");
        byte cleanText[] = null;

        try {
            cleanText = text.getBytes("UTF8");
        } catch (Exception e) { // UnsupportedEncodedException
            e.printStackTrace();
        }

        try {
            // ps = cn.prepareStatement("INSERT INTO cleanText VALUES (null, ?, ?, ?, ?)");
            if (cleanText != null) {
                psCleanText.setBytes(1, cleanText);
                psCleanText.setBoolean(2, false); // it will be true after processed by the FeatureMaker
                psCleanText.setInt(3, Integer.parseInt(page_id));
                psCleanText.setInt(4, Integer.parseInt(text_id));
                psCleanText.execute();
                psCleanText.clearParameters();
                System.out.println("Insert successful");
                System.out.println();
            } else
                System.out.println("WARNING: can not insert in " + cleanTextTableName + ": " + text);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
