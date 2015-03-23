package vue;

import control.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TabPanelThird extends JPanel {

    private final Color color;
    private final JButton btnViewScript;
    private final JButton btnExecScript;
    private final JPanel jPanelNorth;
    private final JScrollPane jPanelCenter;
    private final JTextArea textArea;
    private List<String> lines;
    private java.io.File file = null;

    public TabPanelThird(Color color) {
        this.color = color;

        setLayout(new BorderLayout());

        jPanelNorth = new JPanel();
        btnViewScript = new JButton("Load dml.sql script");
        btnViewScript.setPreferredSize(new Dimension(150, 30));
        btnExecScript = new JButton("Execute script");
        btnExecScript.setPreferredSize(new Dimension(130, 30));
        jPanelNorth.add(btnViewScript);
        jPanelNorth.add(btnExecScript);
        jPanelNorth.add(Box.createHorizontalStrut(420));
        add(jPanelNorth, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);
//        textArea.setForeground(Color.GREEN);
        jPanelCenter = new JScrollPane();
        jPanelCenter.setViewportView(textArea);
        add(jPanelCenter, BorderLayout.CENTER);

        lines = new ArrayList<>();
        setListeners();
        textArea.append("dml.sql script exixts on project root directory...");
    }

    private void setListeners() {
        btnViewScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                textArea.setText(null);
                lines.clear();


                final JFileChooser fileDialog = new JFileChooser();
                int returnVal = fileDialog.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fileDialog.getSelectedFile();
                } else {
                    return;
                }


                try {
                    lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
                    for (String line : lines) {
                        textArea.append(line + System.lineSeparator());
                    }

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        });

        btnExecScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (file == null) {
                    return;
                } 
                scriptEngine();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(this.color);

        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    private void scriptEngine() {

        if (lines.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            sb.append(line);
        }

        String[] parts = sb.toString().split(";");

        textArea.setText(null);
        
        boolean flag = true;
        
        for (String str : parts) {
            try {
                textArea.setForeground(Color.BLUE);
                if(flag && str.split(" ")[0].equalsIgnoreCase("INSERT") ){ // really bad implementation but a real parsing is hard to code
                    flag = false;
                    createTriggersAndProcedures();
                }
                DatabaseConnection.getInstance().connect().createStatement().executeUpdate(str);
                textArea.append(str + ";" +  System.lineSeparator() + " ... DONE" + System.lineSeparator());
            } catch (SQLException ex) {
                textArea.setForeground(Color.RED);
                textArea.append(ex.getMessage() + System.lineSeparator());
            }
        }
        textArea.setForeground(Color.BLACK);
        lines.clear();
    }

    // not used, just for test ; found on internet
    private void scriptEngine_old(File file) {

        StringBuilder sqlBuf = new StringBuilder();
        String line;
        boolean statementReady;
        boolean declaringFunction = false;
        textArea.setText(null);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.contains("$$") || line.contains("$_$") || line.contains("$BODY$")) {
                    declaringFunction = !declaringFunction;
                }

                if (line.equals("--/exe/--")) // scriptEngine finished statement for postgres
                {
                    sqlBuf.append(' ');
                    statementReady = true;
                } else if (line.equals("/")) // scriptEngine finished statement for oracle
                {
                    sqlBuf.append(' ');
                    statementReady = true;
                } else if (line.startsWith("--") || line.length() == 0) // comment or empty
                {
                    continue;
                } else if (!declaringFunction && line.endsWith(";")) {
                    sqlBuf.append(' ');
                    statementReady = true;
                    sqlBuf.append(line.substring(0, line.length() - 1));
                } else {
                    sqlBuf.append(' ');
                    sqlBuf.append(line);
                    statementReady = false;
                }
                if (statementReady) {
                    if (sqlBuf.length() == 0) {
                        continue;
                    }
                    try {
                        String q = sqlBuf.toString();
                        DatabaseConnection.getInstance().connect().createStatement().executeUpdate(q);
                    } catch (SQLException ex) {
                        textArea.append(ex.getMessage());
                    }
                    sqlBuf.setLength(0);
                }
            }
        } catch (FileNotFoundException ex) {
            textArea.append(ex.getMessage());
        } catch (IOException ex) {
            textArea.append(ex.getMessage());
        }
    }

    private void createTriggersAndProcedures() {
        String p = " CREATE OR REPLACE PROCEDURE TULIP_UPDATE "
                + " ( "
                + "  IDMEMBRE IN MEMBRE.ID_MEMBRE%TYPE "
                + " ) AS "
                + " stat STATUT.STATUT_NAME%TYPE; "
                + " BEGIN "
                + "  SELECT S.STATUT_NAME INTO stat "
                + "  FROM STATUT S JOIN MEMBRE MB ON (S.ID = MB.ID_STATUT) WHERE MB.ID_MEMBRE = IDMEMBRE ; "
                + "  IF(stat <> 'bronze') THEN "
                + "    UPDATE membre "
                + "    SET ID_STATUT = (SELECT ID FROM STATUT WHERE STATUT_NAME = 'bronze')"
                + "    WHERE ID_MEMBRE = IDMEMBRE;"
                + "  END IF; "
                + " EXCEPTION "
                + " WHEN NO_DATA_FOUND THEN "
                + "  RAISE_APPLICATION_ERROR (-20222, 'WRONG MEMBER ID'); "
                + " COMMIT; "
                + " END TULIP_UPDATE; ";
        
        String t = " CREATE OR REPLACE TRIGGER trigger_membre_id "
                + " BEFORE INSERT ON membre "
                + " FOR EACH ROW "
                + " BEGIN "
                + " IF :NEW.id_membre IS NULL THEN "
                + " SELECT  membre_id_seq.NEXTVAL INTO :NEW.id_membre FROM dual; "
                + " END IF; "
                + " END; ";
        
        
        try {
            DatabaseConnection.getInstance().connect().createStatement().execute(p);
            textArea.append(p + System.lineSeparator() + " ... done" + System.lineSeparator());
            DatabaseConnection.getInstance().connect().createStatement().execute(t);
            textArea.append(t + System.lineSeparator() + " ... done" + System.lineSeparator());
        } catch (SQLException ex) {
            textArea.append(ex.getMessage());
        }
    }
}
