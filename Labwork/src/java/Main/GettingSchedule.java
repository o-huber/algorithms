package Main;

import DatabaseManager.DatabaseManager;
import DatabaseManager.JDBCDatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GettingSchedule {

    private static DatabaseManager databaseManager = new JDBCDatabaseManager();

    static {
        try {
            databaseManager.connect("postgres", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<Schedule> getSchedule() {
        List<Schedule> buffer = new ArrayList<>();

        ResultSet resultSet = databaseManager.getSchedule();
        try {
            while (resultSet.next()) {
                String key = resultSet.getString("day");
                String[] value = resultSet.getString("lessons")
                        .replaceAll("\\[", "")
                        .replaceAll("]", "")
                        .split(", ");

                Schedule schedule = new Schedule(key);
                schedule.setLessons(Arrays.asList(value));

                buffer.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buffer;
    }
}