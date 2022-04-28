package ru.vnipe;

public class Configuration {
    private int paesQNT;
    private String dbName;
    private String server;
    private int port;
    private String user;
    private String password;

    public Configuration() {}

    public Configuration(int paesQNT, String dbName, String server, int port, String user, String password) {
        this.paesQNT = paesQNT;
        this.dbName = dbName;
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public int getPaesQNT() {
        return paesQNT;
    }

    public void setPaesQNT(int paesQNT) {
        this.paesQNT = paesQNT;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
