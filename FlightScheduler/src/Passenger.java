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
public class Passenger {
    private Connection connection;
    private static PreparedStatement selectAllPeople, insertNewPerson, 
            statusBookedCustomer, statusWtstCustomer;
    
    // constructor
    public Passenger() {
        try
        {
            connection = DBConnection.getConnection();
            
            // create query that selects all enties in Passenger table
            selectAllPeople = connection.prepareStatement("SELECT * FROM Passenger");
            
            // create insert that adds a new entry into the database
            insertNewPerson = connection.prepareStatement("INSERT INTO Passenger (Name) VALUES (?)");
            
            statusBookedCustomer = connection.prepareStatement("SELECT * "
                    + "FROM Bookings "
                    + "WHERE Passenger = ?");
            
            statusWtstCustomer = connection.prepareStatement("SELECT * "
                    + "FROM Waitlist "
                    + "WHERE Passenger = ?");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    // addPerson class
    public int addPerson(String name) {
        int result = 0;
        
        try
        {
            insertNewPerson.setString(1, name);
            
            result = insertNewPerson.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
        return result;
    }
    
    public static String[] getAllPassenger() {
        ArrayList<String> people = new ArrayList<String>();
        ResultSet resultSet = null;
        try
        {
            resultSet = selectAllPeople.executeQuery();
            while (resultSet.next())
            {
                people.add(resultSet.getString("NAME"));
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
        String[] peopleArray = people.toArray(new String[0]);
        return peopleArray;
    }
    
    public static String[][] statusCustomer(String name) {
        ResultSet resultSet1 = null;
        ResultSet resultSet2 = null;
        ArrayList<String> passenger = new ArrayList<String>();
        ArrayList<String> flightList = new ArrayList<String>();
        ArrayList<String> day = new ArrayList<String>();
        ArrayList<String> timestamp = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        
        try
        {
            statusBookedCustomer.setString(1, name);
            statusWtstCustomer.setString(1, name);
            
            try
            {
                resultSet1 = statusBookedCustomer.executeQuery();
                resultSet2 = statusWtstCustomer.executeQuery();
                while (resultSet1.next())
                {
                    passenger.add(resultSet1.getString("Passenger"));
                    flightList.add(resultSet1.getString("Flight"));
                    day.add(resultSet1.getString("Day"));
                    timestamp.add(resultSet1.getString("Timestamp"));
                    status.add("Confirmed");
                }
                while (resultSet2.next())
                {
                    passenger.add(resultSet2.getString("Passenger"));
                    flightList.add(resultSet2.getString("Flight"));
                    day.add(resultSet2.getString("Day"));
                    timestamp.add(resultSet2.getString("Timestamp"));
                    status.add("Waiting");
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
                    resultSet1.close();
                    resultSet2.close();
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
        
        String[][] tableArray = new String[passenger.size()][5];
        for (int i = 0; i < passenger.size(); i++) {
            tableArray[i][0] = passenger.get(i);
            tableArray[i][1] = flightList.get(i);
            tableArray[i][2] = day.get(i);
            tableArray[i][3] = timestamp.get(i);
            tableArray[i][4] = status.get(i);
        }
        return tableArray;
    }
    
}
