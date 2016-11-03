package BloomFilterJoin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;


public class BloomFilterJoin {

    private static ArrayList<String> join(BloomFilter bf, String columnName) {

        String query1 = "SELECT " + columnName + " FROM t1;";

        ArrayList<String> joinColumn = executeQuery(query1);

        // TODO: update table_name
        String queryResult = "SELECT * FROM t1 WHERE";

        int toCheck;
        String content;
        for (int i = 2; i<joinColumn.size(); ++i) {
            content = joinColumn.get(i);
            toCheck = 0;// TODO: convert database content in preferred type (integer?)
            if(bf.check(toCheck)) {
                queryResult += " t1." + columnName + " = '" + content + "' OR";
            }
        }
        queryResult = queryResult.substring(0,queryResult.length() - 3).concat(";");

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