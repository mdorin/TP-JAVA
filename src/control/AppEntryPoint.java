package control;


import vue.AppMainUserWindow;


public class AppEntryPoint {
    public static void main(String[] args) {
        lookAndFeel();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppMainUserWindow mainUserWindow = new AppMainUserWindow();
                mainUserWindow.setVisible(true);
            }
        });
    }

    private static void lookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppEntryPoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppEntryPoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppEntryPoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppEntryPoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
