package com.ll.simpleDb;

import com.ll.Sql;

public class SimpleDBProxy implements Database{

    private  SimpleDB simpleDB;
    private  boolean isConnected=false;

    @Override
    public void connect() {
        if(simpleDB==null) {
            simpleDB=new SimpleDB();
        }
        if(!isConnected) {
            simpleDB.connect();
            isConnected=true;
        }
    }


    public void run(String query) {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }
        simpleDB.run(query);
    }

    public void run(String query,String title,String body,Boolean isBlind) {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }
        simpleDB.run(query,title,body,isBlind);
    }

    public void executeSql(Sql sql) {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }

        simpleDB.executeSql(sql);
    }

    public Sql genSql() {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }

        return simpleDB.genSql();
    }

    public void startTransaction() {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }

        simpleDB.startTransaction();
    }

    public void rollback() {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }

        simpleDB.rollback();
    }

    public void commit() {
        if (!isConnected) {
            System.out.println("DatabaseProxy: 연결이 필요합니다. 자동으로 연결합니다.");
            connect();
        }

        simpleDB.commit();
    }


    public void disconnect() {
        if (isConnected && simpleDB != null) {
            simpleDB.disconnect();
            isConnected = false;
        }
    }
}
