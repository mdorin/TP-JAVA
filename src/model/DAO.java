package model;


import util.AppUtils;
import util.AppSql;
import util.AppGlobals;
import control.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

public class DAO {

    private DAO() {
    }

    public static ResultSet appExecuteQuery(String query) {

        Connection connection = AppUtils.checkNull("Invalid connection to database", DatabaseConnection.getInstance().connect());
        Statement stmt;
        ResultSet rset = null;
        try {
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            AppGlobals.out.display("Statement object creation succeeded!");
        } catch (SQLException e) {
            AppGlobals.out.display("Statement object creation NOT succeeded! " + e.getMessage());
            return null;
        }

        try {
            rset = stmt.executeQuery(query);
            AppGlobals.out.display(query + " - OK");
        } catch (SQLTimeoutException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("ResultSet object creation not succeeded! " + e.getMessage());
        }

        return rset; // in my case return value may be null
        // a ResultSet object that contains the data produced by the given query; never null (JavaDoc)

    }

    private static PreparedStatement appExecutePreparedQuery(String preparedQuery) {
        Connection connection = AppUtils.checkNull("Invalid connection to database", DatabaseConnection.getInstance().connect());
        PreparedStatement pstmt;

        try {
            pstmt = connection.prepareStatement(preparedQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            AppGlobals.out.display("Prepared statement object creation succeeded!");
        } catch (SQLException e) {
            AppGlobals.out.display("Prepared statement object creation NOT succeeded! " + e.getMessage());
            return null;
        }

        return pstmt;
    }
    
   public static int deleteMember(int id) throws SQLException{
       PreparedStatement pstmt = appExecutePreparedQuery(AppSql.DELETE_MEMBER);
       pstmt.setInt(1, id);
       return pstmt.executeUpdate();
   }
   
   public static void callProcedure(int id) throws SQLException{
       CallableStatement cs = AppUtils.checkNull("Invalid connection to database", DatabaseConnection.getInstance().connect()).prepareCall("{ call TULIP_UPDATE( ? ) }");
       cs.setInt(1,id);
       cs.execute();
   }
}
