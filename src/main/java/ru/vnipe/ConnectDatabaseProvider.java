package ru.vnipe;

public interface ConnectDatabaseProvider {
    void useDatabase(String name);
    void createNewDatabase(String name);
    void connect(String server, int host, String userName, String password, String database);
    void disconnect();
}
