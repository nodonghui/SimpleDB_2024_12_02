package com.ll.simpleDb;

import com.ll.Sql;

public interface Database {

    void connect();
    void run(String query);
    void disconnect();
    void executeSql(Sql sql);
    Sql genSql();

}
