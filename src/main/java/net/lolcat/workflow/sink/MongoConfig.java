package net.lolcat.workflow.sink;


import lombok.Data;

import java.io.Serializable;

@Data
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
}
