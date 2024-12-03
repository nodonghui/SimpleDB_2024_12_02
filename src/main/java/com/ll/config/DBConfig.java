package com.ll.config;


import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Setter
public class DBConfig {

    private static DBConfig instance;

    private String host;
    private String username;
    private String password;
    private String dbname;
    private String portNum;

    private  DBConfig() {
        try (InputStream input=new FileInputStream("config.properties")) {
            Properties prop=new Properties();
            prop.load(input);
            this.host=prop.getProperty("jdbc.host");
            this.username=prop.getProperty("jdbc.username");
            this.password=prop.getProperty("jdbc.password");
            this.dbname=prop.getProperty("jdbc.dbname");
            this.portNum=prop.getProperty("jdbc.portNum");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static DBConfig getInstance() {
        if(instance==null) {
            synchronized (DBConfig.class) {
                if(instance==null) {
                    instance=new DBConfig();
                }
            }
        }

        return instance;
    }
}
