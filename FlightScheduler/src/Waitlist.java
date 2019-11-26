
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class Waitlist {
    
    private Connection connection;
    private static PreparedStatement getWaitlistByDay;
    private static PreparedStatement addWaitlist;
    
    // constructor
    public Waitlist() {
        try
        {
            connection = DBConnection.getConnection();
            addWaitlist = connection.prepareStatement(
                    "INSERT INTO Waitlist (Passenger, Flight, Day, Timestamp)"
                    + "VALUES (?, ?, ?, ?)");
            getWaitlistByDay = connection.prepareStatement(
                    "SELECT * "
                    + "FROM Waitlist "
                    + "WHERE Day = ? ");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void addWaitlist(String name, java.sql.Date date, 
            String flight, java.sql.Timestamp timestamp) {
        int result = 0;
        try
        {
            addWaitlist.setString(1, name);
            addWaitlist.setString(2, flight);
            addWaitlist.setDate(3, date);
            addWaitlist.setTimestamp(4, timestamp);
            result = addWaitlist.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
    }
    
    public String[][] getWaitlistByDay(java.sql.Date date) {
        ResultSet resultSet = null;
        ArrayList<String> passenger = new ArrayList<String>();
        ArrayList<String> flightList = new ArrayList<String>();
        ArrayList<String> day = new ArrayList<String>();
        ArrayList<String> timestamp = new ArrayList<String>();
        
        try
        {
            getWaitlistByDay.setDate(1, date);
            
            try
            {
                resultSet = getWaitlistByDay.executeQuery();
                while (resultSet.next())
                {
                    passenger.add(resultSet.getString("Passenger"));
                    flightList.add(resultSet.getString("Flight"));
                    day.add(resultSet.getString("Day"));
                    timestamp.add(resultSet.getString("Timestamp"));
                }
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
            finally
            {
                try
                {
                    resultSet.close();
                }
                catch (SQLException sqlException)
                {
                    sqlException.printStackTrace();
                    DBConnection.close();
                }
            }
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
        String[][] tableArray = new String[passenger.size()][4];
        for (int i = 0; i < passenger.size(); i++) {
            tableArray[i][0] = passenger.get(i);
            tableArray[i][1] = flightList.get(i);
            tableArray[i][2] = day.get(i);
            tableArray[i][3] = timestamp.get(i);
        }
        return tableArray;
    }
    
}
