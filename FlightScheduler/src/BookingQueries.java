
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lizhewen
 */
public class BookingQueries {
    
    private Connection connection;
    private static PreparedStatement addBooking, selectByFlightDay, 
            getFlightName, cancelBooking, getFromWaitlist, updateBooking,
            removeWaitlist, dropFlightWaitlist, rebookPassenger, selectByFlight;
    
    // constructor
    public BookingQueries() {
        try
        {
            connection = DBConnection.getConnection();
            addBooking = connection.prepareStatement(
                    "INSERT INTO Bookings (Passenger, Flight, Day, Timestamp)"
                    + "VALUES (?, ?, ?, ?)");
            selectByFlightDay = connection.prepareStatement(
                    "SELECT * "
                    + "FROM Bookings "
                    + "WHERE Flight = ? "
                    + "AND Day = ?");
            selectByFlight = connection.prepareStatement(
                    "SELECT * FROM Bookings WHERE Flight = ? ORDER BY Timestamp");
            getFlightName = connection.prepareStatement(
                    "SELECT * FROM Bookings "
                    + "WHERE Passenger = ? "
                    + "AND Day = ?");
            cancelBooking = connection.prepareStatement(
                    "DELETE FROM Bookings "
                    + "WHERE Passenger = ? "
                    + "AND Day = ?");
            getFromWaitlist = connection.prepareStatement(
                    "SELECT * "
                    + "FROM Waitlist "
                    + "WHERE Flight = ? "
                    + "AND Day = ? "
                    + "ORDER BY Timestamp "
                    + "FETCH FIRST 1 ROWS ONLY");
            removeWaitlist = connection.prepareStatement(
                    "DELETE FROM Waitlist "
                    + "WHERE Passenger = ? "
                    + "AND Day = ?");
            dropFlightWaitlist = connection.prepareStatement(
                    "DELETE FROM Waitlist "
                    + "WHERE Flight = ?");
            updateBooking = connection.prepareStatement(
                    "UPDATE Bookings "
                    + "SET Flight = ?, Timestamp = ? "
                    + "WHERE Passenger = ?");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public static int addBooking(String name, java.sql.Date date, 
            String flight, java.sql.Timestamp timestamp) {
        int result = 0;
        try
        {
            addBooking.setString(1, name);
            addBooking.setString(2, flight);
            addBooking.setDate(3, date);
            addBooking.setTimestamp(4, timestamp);
            result = addBooking.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        return result;
    }
    
    public static String[][] statusByFlightDay(java.sql.Date date, String flight) {
        ResultSet resultSet = null;
        ArrayList<String> passenger = new ArrayList<String>();
        ArrayList<String> flightList = new ArrayList<String>();
        ArrayList<String> day = new ArrayList<String>();
        ArrayList<String> timestamp = new ArrayList<String>();
        
        try
        {
            selectByFlightDay.setString(1, flight);
            selectByFlightDay.setDate(2, date);
            
            try
            {
                resultSet = selectByFlightDay.executeQuery();
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
    
    public static String cancelBooking(String name, java.sql.Date day) {
        String flightName = null;
        try
        {
            cancelBooking.setString(1, name);
            cancelBooking.setDate(2, day);
            getFlightName.setString(1, name);
            getFlightName.setDate(2, day);
            ResultSet resultSet = getFlightName.executeQuery();
            if (resultSet.next()) {
                flightName = resultSet.getString("Flight");
            }
            cancelBooking.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        return flightName;
    }
    
    public static int bookPassenger(String flight, java.sql.Date day) {
        int result = 0;
        String passengerName = null;
        try
        {
            getFromWaitlist.setString(1, flight);
            getFromWaitlist.setDate(2, day);
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            ResultSet resultSet = getFromWaitlist.executeQuery();
            if (resultSet.next()) {
                passengerName = resultSet.getString("Passenger");
            }
            result = addBooking(passengerName, day, flight, currentTimestamp);
            removeWaitlist.setString(1, passengerName);
            removeWaitlist.setDate(2, day);
            removeWaitlist.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
        return result;
    }
    
    public static String[] getAvailableFlight(java.sql.Date day) {
        String[] result = new String[2];
        result[0] = "false";
        String[] flightList = Flight.getFlights();
        for (String i : flightList) {
            if (Flight.getAvailableSeats(i, day) > 0) {
                result[0] = "true";
                result[1] = i;
                return result;
            }
        }
        return result;
    }
    
    public static String rebookPassengers(String flight) {
        int result = 0, dropCount = 0, rebookCount = 0;
        String resultString = "Error.";
        
        try
        {
            dropFlightWaitlist.setString(1, flight);
            result = dropFlightWaitlist.executeUpdate();
            selectByFlight.setString(1, flight);
            ResultSet resultSet = selectByFlight.executeQuery();
            while(resultSet.next()) { // for each passenger that was booked
                String pas = resultSet.getString("Passenger");
                java.sql.Date day = resultSet.getDate("Day");
                String[] nextAvailableFlight = getAvailableFlight(day);
                if (nextAvailableFlight[0].equals("true")) { // if there's an available seat
                    java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                    try
                    {
                        // rebook the passenger
                        updateBooking.setString(1, nextAvailableFlight[1]);
                        updateBooking.setTimestamp(2, currentTimestamp);
                        updateBooking.setString(3, pas);
                        if (updateBooking.executeUpdate() == 1) {
                            rebookCount++;
                        }
                    }
                    catch (SQLException sqlException)
                    {
                        sqlException.printStackTrace();
                    }
                }
                else { // if there's no seat, drop booking
                    cancelBooking(pas, day);
                    dropCount++;
                }
            }
            
            resultString = result + " person(s) have been removed from waitlist.\n"
                    + rebookCount + " person(s) have been rebooked.\n" 
                    + dropCount + " person(s) have been dropped from the bookings.\n" ;
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
        return resultString;
    }
    
}
