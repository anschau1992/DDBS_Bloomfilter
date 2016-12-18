package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;


public class NormalJoin {

    private static ArrayList<String> join(ArrayList<String> joinColumn) {
        // joinColumn is column from other db with joinColumn[0] number of columns, joinColumn[1] column name and joinColumn[i] i>0 tuples
        // we are expecting only one column and natural join
        String columnName = joinColumn.get(1);

        // TODO: update table_name
        String query = "SELECT * FROM t1 WHERE";

        // this okay? if we focus on communication cost, yes, if we also want to check performance cost, no
        for (int i=2;i < joinColumn.size(); ++i) {
            if(i == joinColumn.size()-1) {
                query += " t1." + columnName + " = '" + joinColumn.get(i) + "';";
            } else {
                query += " t1." + columnName + " = '" + joinColumn.get(i) + "' OR";
            }
        }

        return executeQuery(query);

    }

    private static ArrayList<String> executeQuery(String query) {

        //TODO: update database name and username/password
        //TODO: error handling
        ArrayList <String> queryResult = new ArrayList <String> ();

        Connection connection = null;
        ResultSet resultSet = null;
        ResultSetMetaData rsmd = null;
        Statement statement = null;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error 1\n");
        }

        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test1", "root", "wertBNM");
        }
        catch (SQLException e)
        {
            System.out.println("Error 2\n");
        }

        if (connection != null)
        {
            try
            {
                statement = connection.createStatement();
            }
            catch (SQLException e)
            {
                System.out.println("Error 3\n");
            }
        }

        if (statement != null)
        {
            try
            {
                resultSet = statement.executeQuery(query);
            }
            catch (SQLException e)
            {
                System.out.println("Error 4\n");
            }
        }

        try
        {
            rsmd = resultSet.getMetaData();
        }
        catch (SQLException e)
        {
            System.out.println("Error 5\n");
        }

        try
        {
            queryResult.add(rsmd.getColumnCount() + "");
        }
        catch (SQLException e)
        {
            System.out.println("Error 6\n");
        }

        try
        {
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                queryResult.add(rsmd.getColumnName(i));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error 7\n");
        }

        try
        {
            while (resultSet.next())
            {
                for (int i=1; i<=rsmd.getColumnCount(); i++)
                {
                    queryResult.add(resultSet.getString(i));
                }
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
        }

        return queryResult;
    }
    
}