package com.ll;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class BindingValue {

    private String title;
    private String body;
    private Boolean isBlind;
    private List<Long> updateId;
    private List<String> bindingOrder;



    public BindingValue() {
        this.title=null;
        this.body=null;
        this.isBlind=null;
        updateId=new ArrayList<>();
        bindingOrder=new ArrayList<>();

    }

    public void setValue(String query,String value) {
        //paring ", title = ?"
        String key=parseQuery(query);
        if(key.equals("title")) {
            this.title=value;
            bindingOrder.add("title");
        }
        if(key.equals("body")) {
            this.body=value;
            bindingOrder.add("body");
        }
    }

    public void setValue(String query,String value1,String value2) {
        //paring ", title = ?"
        this.title=value1;
        bindingOrder.add("title");

        this.body=value2;
        bindingOrder.add("body");

    }

    public void setValue(String query,Boolean value) {
        //paring ", title = ?"
        String key=parseQuery(query);
        if(key.equals("isBlind")) {
            this.isBlind=value;
            bindingOrder.add("isBlind");
        }

    }

    public void setValue(String query,long id1,long id2,long id3,long id4) {

        updateId.add(id1);
        updateId.add(id2);
        updateId.add(id3);
        updateId.add(id4);
        bindingOrder.add("updateId&4");

    }

    public void setValue(String query,long id1,long id2,long id3) {
        updateId.add(id1);
        updateId.add(id2);
        updateId.add(id3);
        bindingOrder.add("updateId&3");
    }

    public void setValue(String query,long id1,long id2) {
        updateId.add(id1);
        updateId.add(id2);
        bindingOrder.add("updateId&2");
    }

    public void setValue(String query,Long [] ids) {
        updateId.addAll(Arrays.asList(ids));
        bindingOrder.add("updateId&"+ids.length);
    }

    String parseQuery(String query) {
        //너무 복잡해지면 state 그래프 그리자
        if(query.contains("=")) {
            String splitData1 = query.split("=")[0];
            if (splitData1.startsWith(",")) {
                return splitData1.substring(1, splitData1.length() - 1).trim();
            }

            if (splitData1.startsWith("SET")) {
                return splitData1.substring(3, splitData1.length() - 1).trim();
            }
        }

        if(query.contains("LIKE")) {
            String splitData1 = query.split("LIKE")[0];
            if(splitData1.contains("title")) {
                return "title";
            }
        }



        return query;
    }



}
