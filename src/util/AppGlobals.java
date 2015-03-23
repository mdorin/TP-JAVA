package util;



import util.CustomOutput;
import util.ShowMessage;
import util.StandardConsoleOuput;
import java.util.HashMap;
import java.util.Map;

public class AppGlobals {

    public static final String STANDARD_OUTPUT = "screen";
    public static final String CUSTOM_OUTPUT = "custom";

    public static ShowMessage out = null;
    private Map<String, ShowMessage> hm = null;
    private String key = null;

    private AppGlobals() {

        hm = new HashMap<>();

        hm.put("screen", new StandardConsoleOuput());
        hm.put("custom", CustomOutput.getInstance());

        key = STANDARD_OUTPUT;
        out = hm.get(key);
    }

    public String getKey() {
        return key;
    }

    public void redirectMessage(String key) {
        this.key = key;
        out = hm.get(key);
    }

    private static class ApplicationGlobalsHolder {

        private static final AppGlobals INSTANCE = new AppGlobals();
    }

    public static AppGlobals getInstance() {
        return ApplicationGlobalsHolder.INSTANCE;
    }

}
