package model;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class TableData {

    private final int nombreOfColumns;
    private final String[] columnNames;
    private final Object[][] data;

    private final ResultSet rset;
    private final ResultSetMetaData rsmd;

    public TableData(ResultSet rset) throws SQLException {
        this.rset = rset;
        rsmd = rset.getMetaData();
        nombreOfColumns = rsmd.getColumnCount();
        columnNames = new String[nombreOfColumns];
        int nombreOfRows =  getRowCount();
        data = new Object[nombreOfRows][nombreOfColumns];
    }

    private int getRowCount() throws SQLException {
        int rows = 0;
        while (rset.next()) {
            rows++;
        }
        rset.beforeFirst();
        return rows;
    }

    public int getNombreOfColumns() throws SQLException {
        return nombreOfColumns;
    }

    public String[] getColumnNames() throws SQLException {
        for (int i = 1; i <= nombreOfColumns; i++) {
            columnNames[i - 1] = rsmd.getColumnName(i);
        }
        return columnNames;
    }

    public Object[][] getData() throws SQLException {
        int i = 0, j;
        while (rset.next()) {
            j = 0;
            while (j <= (columnNames.length - 1)) {
                data[i][j] = rset.getString(j + 1); // assuming there are all VARCHAR2 there
                j++;
            }
            i++;
        }
        return data;
    }
}
