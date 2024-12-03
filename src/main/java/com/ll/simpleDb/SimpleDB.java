package com.ll.simpleDb;


import com.ll.BindingValue;
import com.ll.Sql;
import com.ll.config.DBConfig;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;

@Getter
@Setter
public class SimpleDB implements Database{

    private Connection connection;
    private DBConfig dbConfig;
    private Boolean devFlag;

    public SimpleDB() {
        dbConfig=DBConfig.getInstance();
    }
    //나중에 전략 패턴 적용
    public void setDevMode(Boolean devFlag) {
        this.devFlag=devFlag;
    }


    public void connect() {
        String jdbcURL="jdbc:mysql://"+dbConfig.getHost()+":"
         +dbConfig.getPortNum()+"/" +dbConfig.getDbname()+"?useSSL=false&serverTimezone=UTC";
        String username=dbConfig.getUsername();
        String password=dbConfig.getPassword();
        try {
            if(connection==null  || connection.isClosed()) {
                connection= DriverManager.getConnection(jdbcURL,username,password);
                System.out.println("SimpleDB: MySQL DB에 연결되었습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void run(String query) {
        try (Statement stmt=connection.createStatement()) {
            stmt.execute(query);
            System.out.println("SimpleDB: 쿼리가 실행되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void run(String query,String title,String body,boolean isBlind) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, body);
            stmt.setBoolean(3,isBlind);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void disconnect(){
        try {
            if(connection !=null && !connection.isClosed()) {
                connection.close();
                System.out.println("SimpleDB: 연결이 해제되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeSql(Sql sql) {
        sql.executeUpdate();
    }


    public Sql genSql() {
        StringBuilder sb=new StringBuilder();
        BindingValue bindingValue=new BindingValue();
        Sql sql=new Sql(this,sb,bindingValue);

        return sql;
    }




}
