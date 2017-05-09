package net.lolcat.workflow.sink;

import java.io.Serializable;

public class MongoConfig  implements Serializable{

    private String host;

    private int port;

    private String db;

    private String password;

    private String login;

    public MongoConfig() {
        // Default constructor
    }

    public MongoConfig(String host, int port, String db, String password, String login) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.password = password;
        this.login = login;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
