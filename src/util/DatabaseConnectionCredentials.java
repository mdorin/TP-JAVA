package util;

public class DatabaseConnectionCredentials {

    private final String driverName;
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionCredentials(String p_driverName, String p_url, String p_user, String p_password) {

        driverName = AppUtils.checkNull("Driver name must not be null", p_driverName);
        url = AppUtils.checkNull("Url must not be null", p_url);
        user = AppUtils.checkNull("User name must not be null", p_user);
        password = AppUtils.checkNull("Password must not be null", p_password);

    }

    public String getDriverName() {
        return driverName;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

}
