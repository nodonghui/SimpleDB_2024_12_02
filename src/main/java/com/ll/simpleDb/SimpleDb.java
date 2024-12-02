package com.ll.simpleDb;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleDb {

    private Boolean devFlag;

    final private String host;
    final private String userName;
    final private String password;
    final private String databaseName;

    //나중에 전략 패턴 적용
    public void setDevMode(Boolean devFlag) {
        this.devFlag=devFlag;
    }

    public void run(String query) {

    }
}
