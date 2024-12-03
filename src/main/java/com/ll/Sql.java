package com.ll;

import com.ll.simpleDb.SimpleDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Sql {


    SimpleDB simpleDB;
    StringBuilder sb;
    PreparedStatement stmt;
    BindingValue bindingValue;

    public Sql(SimpleDB simpleDB,StringBuilder sb,BindingValue bindingValue) {
        this.sb=sb;
        this.simpleDB=simpleDB;
        this.bindingValue=bindingValue;
    }

    public Sql append(String query) {
        sb.append(query);
        sb.append("\n");


        return new Sql(simpleDB,sb,bindingValue);
    }

    public Sql append(String query, String data) {
        sb.append(query);
        sb.append("\n");
        bindingValue.setValue(query,data);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public Sql append(String query, Boolean data) {
        sb.append(query);
        sb.append("\n");
        bindingValue.setValue(query,data);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public long executeUpdate() {
        long primaryKeyId=0;
        int affectedRows;
        try {
            stmt = simpleDB.getConnection().prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, bindingValue.title);
            stmt.setString(2, bindingValue.body);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            affectedRows=stmt.executeUpdate();
            if(affectedRows >0) {
                try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        primaryKeyId = generatedKeys.getLong(1); // 첫 번째 열에서 기본키 값 가져오기
                        System.out.println("Inserted record's ID: " + primaryKeyId);
                    } else {
                        System.out.println("No ID was generated.");
                    }
                } catch (Exception e) {
                    System.out.println("excute실패");
                }

            } else {
                System.out.println("기본키를 찾지 못했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeyId;
    }
}
