/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author lizhewen
 */
public class DBConnection {
    
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBEricLiZXL163";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "java";
    private static Connection connection;
    
    public static Connection getConnection() {
        try
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
        return connection;
    }
    
    public static void close() {
        try
        {
            connection.close();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
}
