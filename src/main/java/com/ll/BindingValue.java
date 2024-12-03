package com.ll;


public class BindingValue {

    String title;
    String body;
    Boolean isBlind;


    public BindingValue() {
        this.title="none";
        this.body="none";
        this.isBlind=false;
    }

    public void setValue(String query,String value) {
        //paring ", title = ?"
        String key=parseQuery(query);
        if(key.equals("title")) {this.title=value;}
        if(key.equals("body")) {this.body=value;}
    }

    public void setValue(String query,Boolean value) {
        //paring ", title = ?"
        String key=parseQuery(query);
        if(key.equals("isBlind")) {this.isBlind=value;}

    }

    String parseQuery(String query) {
        String splitData1=query.split("=")[0];

        return splitData1.substring(1,splitData1.length()-1).trim();
    }

}
