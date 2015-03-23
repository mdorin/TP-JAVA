package vue;

import model.DAO;
import util.*;
import util.AppSql;
import control.DatabaseConnection;
import model.TableData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TabPanelFirst extends JPanel {

    private final Color color;
    private final JScrollPane jScrollPaneJTable;
    private JTable jTable;
    private final JButton btnQueryAll;
    private final JPanel jPanelSouth;
    private final JComboBox comboBox;

    private final JButton btnQueryFilter;

    public TabPanelFirst(Color color, Object[][] data, String[] columnNames) {
        this.color = color;

        jScrollPaneJTable = new JScrollPane();
        jTable = new JTable(data, columnNames);

        String[] e = {"GOLD", "SILVER", "BRONZE"};
        comboBox = new JComboBox(e);

        jPanelSouth = new JPanel(new FlowLayout());
        jPanelSouth.add(new JLabel("Show all club menbers : "));
        btnQueryAll = new JButton("Show all");
        //reQueryAll.setPreferredSize(new Dimension(100, 40));
        jPanelSouth.add(btnQueryAll);

        btnQueryFilter = new JButton("Filter");

        jPanelSouth.add(Box.createHorizontalStrut(250));
        jPanelSouth.add(new JLabel("Show only : "));
        jPanelSouth.add(comboBox);
        jPanelSouth.add(new JLabel(" members"));
        jPanelSouth.add(btnQueryFilter);

        setLayout(new BorderLayout());

//        add(new JButton("North"), BorderLayout.NORTH);
        add(jPanelSouth, BorderLayout.SOUTH);
//        add(new JButton("East"), BorderLayout.EAST);
//        add(new JButton("West"), BorderLayout.WEST);
        jScrollPaneJTable.setViewportView(jTable);
        add(jScrollPaneJTable, BorderLayout.CENTER);
        setListeners();
    }

    private void setListeners() {
        btnQueryAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ResultSet rset = DAO.appExecuteQuery(AppSql.SQL_ALL_MEMBERS);
                    if (rset == null) {
                        // display this info
                        return;
                    }
                    TableData t = new TableData(rset); // tbd check for null here!
                    jTable = new JTable(t.getData(), t.getColumnNames());
                    jScrollPaneJTable.setViewportView(jTable);
                } catch (SQLException ex) {
                    AppGlobals.out.display(ex.getMessage());
                }
            }
        });

        btnQueryFilter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Connection connection = AppUtils.checkNull("Invalid connection to database", DatabaseConnection.getInstance().connect());
                ResultSet rset;
                PreparedStatement pstmt;
                String selectedItem = ((String) comboBox.getSelectedItem()).toLowerCase();
                int id;

                try {
                    pstmt = connection.prepareStatement(AppSql.SQL_ID_STATUT, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    pstmt.setString(1, selectedItem);
                    rset = pstmt.executeQuery();

                    if (!rset.first()) {
                        AppGlobals.out.display("Status does not exist!");
                        return;
                    }

                    id = rset.getInt(1);
                    pstmt = connection.prepareStatement(AppSql.SQL_ALL_STATUT_MEMBERS, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);
                    pstmt.setInt(1, id);
                    rset = pstmt.executeQuery();

                    if (rset == null) {
                        // display this info
                        return;
                    }
                    
                    TableData t = new TableData(rset);
                    jTable = new JTable(t.getData(), t.getColumnNames());
                    jScrollPaneJTable.setViewportView(jTable);
                    AppGlobals.out.display(AppSql.SQL_ALL_STATUT_MEMBERS + " - OK");
                } catch (SQLTimeoutException e) {
                    AppGlobals.out.display(e.getMessage());
                } catch (SQLException ex) {
                    AppGlobals.out.display(ex.getMessage());
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(this.color);

        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

}
