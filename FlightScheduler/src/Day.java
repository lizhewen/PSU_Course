
import java.sql.Connection;
import java.sql.Date;
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
public class Day {
    private Connection connection;
    private static PreparedStatement selectAllDate, addDate;
    
    // constructor
    public Day() {
        try
        {
            connection = DBConnection.getConnection();
            selectAllDate = connection.prepareStatement("SELECT * FROM Day");
            addDate = connection.prepareStatement("INSERT INTO Day (Date) VALUES (?)");
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            System.exit(1);
        }
    }
    
    public static String[] getDates() {
        ArrayList<String> dates = new ArrayList<String>();
        ResultSet resultSet = null;
        try
        {
            resultSet = selectAllDate.executeQuery();
            while (resultSet.next())
            {
                dates.add(resultSet.getString("Date"));
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
        String[] datesArray = dates.toArray(new String[0]);
        return datesArray;
    }
    
    public int addDate(Date day) {
        int result = 0;
        
        try
        {
            addDate.setDate(1, day);
            
            result = addDate.executeUpdate();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
            DBConnection.close();
        }
        
        return result;
    }
    
}
