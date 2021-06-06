package DatabaseManager;

import java.sql.*;

public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    @Override
    public void connect(String user, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/University", user, password);
        if (connection != null) {
            System.out.println("Connected to database!");
        } else {
            System.out.println("Connecting failure");
        }
    }

    @Override
    public void addDayToSchedule(String day, String lessons) {
        final String table = "schedule";
        final String dayColumn = "day";
        final String lessonsColumn = "lessons";

        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO " + table + " " +
                    "(" + dayColumn + ", " + lessonsColumn +
                    ") VALUES ('" + day + "', '" + lessons + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet getSchedule() {
        String table = "schedule";
        ResultSet resultSet = null;

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + table);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}