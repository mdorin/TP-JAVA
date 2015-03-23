package vue;



import util.AppGlobals;
import control.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author BRAVO
 */
public class AppMainUserWindow extends JFrame {

    private final String BACKGROUND_IMAGE_PATH = "resources/tulip.jpg";

    private JMenuBar mainMenu;
    private JMenu menuFile;
    private JMenuItem connect;
    private JMenuItem disconnect;
    private JMenuItem exit;

    private JMenu menuEdit;
    private JMenuItem toBeDefined;

    private JMenu menuHelp;
    private JMenuItem about;

    private JLabel jLabelBackgroundImage;

    public AppMainUserWindow() throws HeadlessException {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Red tulip club organisation");
        setSize(820, 600);
        createInitialContent();
        setListeners();
        AppGlobals.getInstance().redirectMessage(AppGlobals.STANDARD_OUTPUT);
        setLocationRelativeTo(null);

    }

    private void createInitialContent() {
        setBackgroundImage(BACKGROUND_IMAGE_PATH);

        mainMenu = new JMenuBar();
        setJMenuBar(mainMenu);

        menuFile = new JMenu("File");
        mainMenu.add(menuFile);
        connect = new JMenuItem("Connect");
        menuFile.add(connect);
        disconnect = new JMenuItem("Disconnect");
        menuFile.add(disconnect);
        exit = new JMenuItem("Exit");
        menuFile.add(exit);

        menuEdit = new JMenu("Edit");
        mainMenu.add(menuEdit);
        toBeDefined = new JMenuItem("To be defined");
        menuEdit.add(toBeDefined);

        menuHelp = new JMenu("Help");
        mainMenu.add(menuHelp);
        about = new JMenuItem("About");
        menuHelp.add(about);

        disconnect.setEnabled(false);

    }

    private void setListeners() {
        connect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseConnectionDialog dialog = new DatabaseConnectionDialog(new javax.swing.JFrame(), true);
                        dialog.setVisible(true);

                        if (dialog.getReturnStatus() == DatabaseConnectionDialog.USER_CANCEL) {
                            return;
                        }

                        if (dialog.getReturnStatus() == DatabaseConnectionDialog.CONNECTION_ERROR) {
                            //error code here
                            return;
                        }

                        getContentPane().removeAll();
                        createPostInitialContent();
                        disconnect.setEnabled(true);
                        connect.setEnabled(false);
                    }
                });
            }
        });

        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseConnection.getInstance().disconnect();
                setBackgroundImage(BACKGROUND_IMAGE_PATH);
                connect.setEnabled(true);
                disconnect.setEnabled(false);
            }
        });

        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null,
                        "Red tulip club organisation" + System.lineSeparator() + "Author: Dorin" + System.lineSeparator() + "version 1.0");
            }
        });

        // not working!
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(null, "Do you want to quit?");
                if (i == 0) {
                    DatabaseConnection.getInstance().disconnect(); // free resources
                    System.exit(0);
                }
                return;
            }
        });
        
        exit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                DatabaseConnection.getInstance().disconnect(); // free resources
                System.exit(0);
                    
            }
            
        });

    }

    private void createPostInitialContent() {

        JPanel panelNorth = new JPanel();
        JLabel labelTimeConnection = new JLabel("Last valid connection at : " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(Calendar.getInstance().getTime()));
        labelTimeConnection.setForeground(Color.RED);
        panelNorth.add(labelTimeConnection);

        getContentPane().add(panelNorth, BorderLayout.NORTH);
        getContentPane().add(tabbedPane(), BorderLayout.CENTER);
        tabbedPane();

        revalidate();
        repaint();
    }

    private void setBackgroundImage(String path) {

        getContentPane().removeAll();
        jLabelBackgroundImage = new JLabel(new ImageIcon(path));
        getContentPane().add(jLabelBackgroundImage);
        revalidate();
        repaint();
    }

    private JTabbedPane tabbedPane() {
        //Création de notre conteneur d'onglets
        JTabbedPane tab = new JTabbedPane();

        String[][] data = {{"", ""}};
        String[] columnNames = {""};

        TabPanelFirst tPanFirst = new TabPanelFirst(Color.RED, data, columnNames);
        TabPanelSecond tPanSecond = new TabPanelSecond(Color.BLUE);
        TabPanelThird tPanThird = new TabPanelThird(Color.GREEN);

        
        tab.add("Load script", tPanThird);
        tab.add("View results", tPanFirst);
        tab.add("Delete and update", tPanSecond);
        
        
        return tab;
    }

}
