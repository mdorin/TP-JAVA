package vue;


import model.DAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TabPanelSecond extends JPanel {

    private final Color color;

    private final JPanel panelNorth;
    private final JPanel panelSouth;
    private final JLabel label1;
    private final JLabel label2;
    private final JTextField jTextCodeNumberUpdateStatus;
    private final JButton btnUpdate;

    private final JTextField jTextCodeNumberDeleteMember;
    private final JButton btnDeleteMember;

    private final JScrollPane scrollPane;
    private final JTextArea textArea;

    public TabPanelSecond(Color color) {
        this.color = color;

        setLayout(new BorderLayout());

        panelNorth = new JPanel();
        panelSouth = new JPanel();

        label1 = new JLabel("Member to be updated");
        label1.setPreferredSize(new Dimension(150, 30));
        panelNorth.add(label1);

        jTextCodeNumberUpdateStatus = new JTextField();
        jTextCodeNumberUpdateStatus.setPreferredSize(new Dimension(150, 30));
        panelNorth.add(jTextCodeNumberUpdateStatus);

        btnUpdate = new JButton("Update status");
        btnUpdate.setPreferredSize(new Dimension(130, 30));
        panelNorth.add(btnUpdate);
        panelNorth.add(new JLabel("(using stored procedure)"));
        panelNorth.add(Box.createHorizontalStrut(200));

        //----------------------------------------------------------------------
        label2 = new JLabel("Member to be deleted");
        label2.setPreferredSize(new Dimension(150, 30));
        panelSouth.add(label2);

        jTextCodeNumberDeleteMember = new JTextField();
        jTextCodeNumberDeleteMember.setPreferredSize(new Dimension(150, 30));
        panelSouth.add(jTextCodeNumberDeleteMember);

        btnDeleteMember = new JButton("Delete member");
        btnDeleteMember.setPreferredSize(new Dimension(130, 30));
        panelSouth.add(btnDeleteMember);
        panelSouth.add(Box.createHorizontalStrut(340));

        add(panelNorth, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        display("Notification messsages will appear here...");
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.CENTER);

        add(panelSouth, BorderLayout.SOUTH);

        setListeners();
    }

    private void setListeners() {
        btnDeleteMember.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String arg = jTextCodeNumberDeleteMember.getText();
                jTextCodeNumberDeleteMember.setText("");

                if (!checkInteger(arg)) {
                    display("Enter an integer!");
                    return;
                }

                try {
                    int rows = DAO.deleteMember(Integer.parseInt(arg));
                    display(rows + " rows updated");
                } catch (SQLException ex) {
                    display(ex.getMessage());
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String arg = jTextCodeNumberUpdateStatus.getText();
                jTextCodeNumberUpdateStatus.setText("");

                if (!checkInteger(arg)) {
                    display("Enter an integer!");
                    return;
                }
                
                int id = Integer.parseInt(arg);
                try {
                    DAO.callProcedure(id);
                    display("Member with id = " + id + " has now status 'bronze'");
                } catch (SQLException ex) {
                    display(ex.getMessage());
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(this.color);

        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    private boolean checkInteger(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            //display(e.getMessage());
        }
        return false;
    }

    private void display(String msg) {
        textArea.append(msg + System.lineSeparator());
    }
}
