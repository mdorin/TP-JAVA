package util;


import util.ShowMessage;



/**
 *
 * @author BRAVO
 */
public class CustomOutput implements ShowMessage {

    private CustomOutput() {
    }

    public static CustomOutput getInstance() {
        return CustomOutputHolder.INSTANCE;
    }

    @Override
    public void display(String messsage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class CustomOutputHolder {

        private static final CustomOutput INSTANCE = new CustomOutput();
    }

}
