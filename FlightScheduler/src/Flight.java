
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
public class Flight {
    
    private Connection connection;
    private static PreparedStatement selectAllFlightName, getSeats, getFlightSeats, addFlight, dropFlight;
    
    // constructor
    public Flight() {
        try
        {
            connection = DBConnection.getConnection();
            selectAllFlightName = connection.prepareStatement("SELECT * FROM Flight");
            getSeats = connection.prepareStatement(
                    "SELECT Seats FROM Flight WHERE Name = ?");
            getFlightSeats = connection.prepareStatement(""
                    + "SELECT COUNT(passenger) "
                    + "FROM Bookings "
                    + "WHERE flight = ? AND day = ?"); 
            addFlight = connection.prepareStatement("INSERT INTO Flight "
                    + "(Name, Seats) VALUES (?, ?)");
            dropFlight = connection.prepareStatement(
                    "DELETE FROM Flight "
                    + "WHERE Name = ?");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public int addFlight(String name, int num) {
        int result = 0;
        
        try
        {
            addFlight.setString(1, name);
            addFlight.setInt(2, num);
            
            result = addFlight.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
        return result;
    }
    
    public static String[] getFlights() {
        ArrayList<String> flights = new ArrayList<String>();
        ResultSet resultSet = null;
        try
        {
            resultSet = selectAllFlightName.executeQuery();
            while (resultSet.next())
            {
                flights.add(resultSet.getString("Name"));
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
        String[] flightsArray = flights.toArray(new String[0]);
        return flightsArray;
    }
    
    public static int getTotalSeats(String flight) {
        int result = 0;
        ResultSet resultSet = null;
        try
        {
            getSeats.setString(1, flight);
            resultSet = getSeats.executeQuery();
            while (resultSet.next()) {
                result = Integer.parseInt(resultSet.getString("Seats"));
            }
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return result;
    }
    
    public static int getAvailableSeats(String flight, java.sql.Date date) {
        int totalSeats = getTotalSeats(flight);
        int seatsBooked = 0;
        ResultSet resultSet = null;
        try
        {
            getFlightSeats.setString(1, flight);
            getFlightSeats.setDate(2, date);
            resultSet = getFlightSeats.executeQuery(); 
            resultSet.next(); 
            seatsBooked = resultSet.getInt(1);
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        // return number of available seats
        return (totalSeats - seatsBooked);
    }
    
    public static int dropFlight(String flight) {
        int result = 0;
        
        try
        {
            dropFlight.setString(1, flight);
            result = dropFlight.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        return result;
    }
    
}
