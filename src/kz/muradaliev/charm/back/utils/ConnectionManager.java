package kz.muradaliev.charm.back.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



@UtilityClass
public class ConnectionManager {
    private static final String URL = ConfigFileUtils.get("app.datasource.url");
    private static final String USER = ConfigFileUtils.get("app.datasource.username");
    private static final String PASSWORD = ConfigFileUtils.get("app.datasource.password");
    private static final String DRIVER = ConfigFileUtils.get("app.datasource.driver");
    private static final String FETCH_SIZE_STR = ConfigFileUtils.get("app.datasource.fetch-size");
    public static final int FETCH_SIZE = Integer.parseInt(FETCH_SIZE_STR != null ? FETCH_SIZE_STR : "100");
    private static final String MAX_ROWS_STR = ConfigFileUtils.get("app.datasource.max-rows");
    public static final int MAX_ROWS = Integer.parseInt(MAX_ROWS_STR != null ? MAX_ROWS_STR : "1000");
    private static final String QUERY_TIMEOUT_STR = ConfigFileUtils.get("app.datasource.query-timeout");
    public static final int QUERY_TIMEOUT = Integer.parseInt(QUERY_TIMEOUT_STR != null ? QUERY_TIMEOUT_STR : "10");

    static {
        if (DRIVER != null) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
