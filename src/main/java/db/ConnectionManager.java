package db;

public class ConnectionManager {

    private String host;
    private String username;
    private String password;

    public ConnectionManager(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }
}
