package com.ll;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BindingValue {

    private String title;
    private String body;
    private Boolean isBlind;
    private List<Integer> updateId;



    public BindingValue() {
        this.title=null;
        this.body=null;
        this.isBlind=null;
        updateId=new ArrayList<>();

    }

    public void setValue(String query,String value) {
        //paring ", title = ?"
        String key=parseQuery(query);
        if(key.equals("title")) {
            this.title=value;
        }
        if(key.equals("body")) {
            this.body=value;
        }
    }

    public void setValue(String query,Boolean value) {
        //paring ", title = ?"
        String key=parseQuery(query);
        if(key.equals("isBlind")) {
            this.isBlind=value;
        }

    }

    public void setValue(String query,int id1,int id2,int id3,int id4) {

        updateId.add(id1);
        updateId.add(id2);
        updateId.add(id3);
        updateId.add(id4);

    }

    public void setValue(String query,int id1,int id2,int id3) {

        updateId.add(id1);
        updateId.add(id2);
        updateId.add(id3);


    }

    String parseQuery(String query) {
        String splitData1=query.split("=")[0];
        if(splitData1.startsWith(",")) {
            return splitData1.substring(1,splitData1.length()-1).trim();
        }

        if(splitData1.startsWith("SET")) {
            return splitData1.substring(3,splitData1.length()-1).trim();
        }

        return query;
    }



}
