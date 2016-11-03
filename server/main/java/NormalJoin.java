import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;


public class NormalJoin {

    private static ArrayList<String> join(ArrayList<String> db1) {
        // db1 is column from other db with db1[0] number of columns, db1[1] column name and db1[i] i>0 tuples
        // we are expecting only one column and natural join
        String cName = db1.get(1);

        // TODO: update table_name
        String query = "SELECT * FROM t1 WHERE";

        // this okay? if we focus on communication cost, yes, if we also want to check performance cost, no
        for (int i=2;i < db1.size(); ++i) {
            if(i == db1.size()-1) {
                query += " t1." + cName + " = '" + db1.get(i) + "';";
            } else {
                query += " t1." + cName + " = '" + db1.get(i) + "' OR";
            }
        }

        System.out.println(query);

        return executeQuery(query);

    }

    private static ArrayList<String> executeQuery(String query) {

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

    public static void main(String[] args) {

        // socket codem

        // get column from db1

        ArrayList<String> db1 = new ArrayList<>();
        for(int i=0; i<args.length; ++i) {
            db1.add(args[i]);
        }

        ArrayList<String> result = join(db1);

        int numOfCol = Integer.parseInt(result.get(0));

        for (int i = 1; i < result.size(); i++) {
            if(i % numOfCol == 0) {
                System.out.print(result.get(i) + "\n");
            } else {
                System.out.print(result.get(i) + "\t");
            }
        }

        return;

    }
}