package DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseManager {

    void connect(String user, String password) throws SQLException;

    void addDayToSchedule(String day, String lessons);

    ResultSet getSchedule();

    boolean isConnected();
}