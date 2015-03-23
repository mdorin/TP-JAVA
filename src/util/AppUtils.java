package util;



public class AppUtils {

    public static <T> T checkNull(String message, T object) {
        if (object == null) {
            throw new NullPointerException(message);// shoud I throw a custom exception?
        }
        return object;
    }
    
    
}
