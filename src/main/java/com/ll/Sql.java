package com.ll;

import com.ll.simpleDb.SimpleDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Sql append(String query,int id1,int id2,int id3,int id4) {
        sb.append(query);
        sb.append("\n");
        bindingValue.setValue(query,id1,id2,id3,id4);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public Sql append(String query,int id1,int id2,int id3) {
        sb.append(query);
        sb.append("\n");
        bindingValue.setValue(query,id1,id2,id3);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public long executeInsert() {
        long primaryKeyId=0;
        int affectedRows;

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            addParameter(stmt);
            affectedRows=stmt.executeUpdate();
            if(affectedRows >0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    primaryKeyId = generatedKeys.getLong(1); // 첫 번째 열에서 기본키 값 가져오기
                    System.out.println("Inserted record's ID: " + primaryKeyId);
                } else {
                    System.out.println("No ID was generated.");
                }
            } else {
                System.out.println("기본키를 찾지 못했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return primaryKeyId;
    }

    void addParameter(PreparedStatement stmt) throws SQLException {
        int index=1;
        //파라미터가 입력 되지 않은 경우 null로 초기화 되어있음
        String title= bindingValue.getTitle();
        String body= bindingValue.getBody();
        Boolean isBlind=bindingValue.getIsBlind();
        List<Integer> updateId=bindingValue.getUpdateId();
        if(title !=null) { stmt.setString(index,title); index+=1;}
        if(body !=null) { stmt.setString(index,body); index+=1;}
        if(isBlind !=null) { stmt.setBoolean(index,isBlind); index+=1;}
        for(int id : updateId) {
            stmt.setInt(index,id);
            index+=1;
        }

    }

    public int executeUpdate() {
        int affectedRows=0;

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            addParameter(stmt);
            affectedRows=stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows;
    }

    public int executeDelete() {
        int affectedRows=0;

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            addParameter(stmt);
            affectedRows=stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows;
    }

    public List<Map<String, Object>> selectRows() {

        List<Map<String,Object>> rows=new ArrayList<>();

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            while (resultSet.next()) {
                Map<String,Object> row=new HashMap<>();
                row.put("id",resultSet.getLong(1));
                row.put("createdDate",resultSet.getObject(2, LocalDateTime.class));
                row.put("modifiedDate",resultSet.getObject(3,LocalDateTime.class));
                row.put("title",resultSet.getString(4));
                row.put("body",resultSet.getString(5));
                row.put("isBlind",resultSet.getBoolean(6));
                rows.add(row);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;

    }

    public Map<String, Object> selectRow() {
        Map<String, Object> row=new HashMap<>();

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            while (resultSet.next()) {

                row.put("id",resultSet.getLong(1));
                row.put("createdDate",resultSet.getObject(2, LocalDateTime.class));
                row.put("modifiedDate",resultSet.getObject(3,LocalDateTime.class));
                row.put("title",resultSet.getString(4));
                row.put("body",resultSet.getString(5));
                row.put("isBlind",resultSet.getBoolean(6));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return row;

    }

    public LocalDateTime selectDatetime() {
        LocalDateTime time=LocalDateTime.now();

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            while (resultSet.next()) {
                time=resultSet.getObject(2, LocalDateTime.class);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return time;
    }

    public Long selectLong() {
        Long id=0l;

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            while (resultSet.next()) {
                id=resultSet.getLong(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public String selectString() {
        String title="";

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            while (resultSet.next()) {
                title=resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return title;
    }

    public Boolean selectBoolean() {
        Boolean isblind=false;

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            while (resultSet.next()) {
                isblind=resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isblind;
    }
}
