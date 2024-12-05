package com.ll;

import com.ll.simpleDb.SimpleDB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
        //where in 경우
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

    public Sql append(String query,int id1,int id2) {
        sb.append(query);
        sb.append("\n");
        bindingValue.setValue(query,id1,id2);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public Sql appendIn(String query,long id1,long id2,long id3) {
        String placeholders = String.join(", ", java.util.Collections.nCopies(3, "?"));
        String modifyQuery= query.replace("(?)", "(" + placeholders + ")");
        sb.append(modifyQuery);
        sb.append("\n");
        bindingValue.setValue(query,id1,id2,id3);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public Sql appendIn(String query,String value1,String value2) {
        String placeholders = String.join(", ", java.util.Collections.nCopies(2, "?"));
        String modifyQuery= query.replace("?", placeholders);
        sb.append(modifyQuery);
        sb.append("\n");
        bindingValue.setValue(query,value1,value2);
        return new Sql(simpleDB,sb,bindingValue);
    }

    public Sql appendIn(String query,Long[] ids) {

        String placeholders = String.join(", ", java.util.Collections.nCopies(ids.length, "?"));
        String modifyQuery="";
        if(query.startsWith("ORDER BY FIELD")) {
            modifyQuery= query.replace("(id, ?)", "(id," + placeholders + ")");
        }
        if(query.startsWith("WHERE")) {
            modifyQuery= query.replace("(?)", "(" + placeholders + ")");
        }

        sb.append(modifyQuery);
        sb.append("\n");
        bindingValue.setValue(query,ids);
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
        int start=0;
        //파라미터가 입력 되지 않은 경우 null로 초기화 되어있음
        String title= bindingValue.getTitle();
        String body= bindingValue.getBody();
        Boolean isBlind=bindingValue.getIsBlind();
        List<Long> updateId=bindingValue.getUpdateId();
        for(String order : bindingValue.getBindingOrder()) {
            if(order.equals("title")) { stmt.setString(index,title); index+=1;}
            if(order.equals("body")) { stmt.setString(index,body); index+=1;}
            if(order.equals("isBlind")) { stmt.setBoolean(index,isBlind); index+=1;}
            if(order.startsWith("updateId")) {
                //updateId&4 이런식으로 이번 순서에 추가될 개수 기록되어있다.
                int offset=Integer.parseInt(order.split("&")[1]);
                for(int i=start;i<start+offset;i++) {
                    long id=updateId.get(i);
                    stmt.setLong(index,id);
                    index+=1;
                }
                start+=offset;
            }

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

    public <T> List<T> selectRows(Class<T> clazz) {
        List<T> rows=new ArrayList<>();
        List<Class<?>> parameterTypes=new ArrayList<>();
        List<Object> parameterValues=new ArrayList<>();

        int index=1;
        try{

            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();
            Field[] fields = clazz.getDeclaredFields();
            while (resultSet.next()) {

                createInstanceParameter(parameterTypes,parameterValues,fields,resultSet);

                Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0]));
                T instance = constructor.newInstance(parameterValues.toArray(new Object[0]));
                rows.add(instance);

                parameterTypes.clear();
                parameterValues.clear();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch ( Exception e) {
            throw new RuntimeException("Error creating instance for: " + clazz.getName(), e);
        }


        return rows;

    }

    public <T> T selectRow(Class<T> clazz) {

        T row=null;
        List<Class<?>> parameterTypes=new ArrayList<>();
        List<Object> parameterValues=new ArrayList<>();


        try{

            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            ResultSet resultSet=stmt.executeQuery();

            Field[] fields = clazz.getDeclaredFields();
            while (resultSet.next()) {

                createInstanceParameter(parameterTypes,parameterValues,fields,resultSet);

                Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0]));
                row = constructor.newInstance(parameterValues.toArray(new Object[0]));


                parameterTypes.clear();
                parameterValues.clear();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch ( Exception e) {
            throw new RuntimeException("Error creating instance for: " + clazz.getName(), e);
        }


        return row;

    }

    void createInstanceParameter(List<Class<?>> parameterTypes,List<Object> parameterValues,Field[] fields,ResultSet resultSet) throws SQLException {
        int index=1;
        for (Field field : fields) {
            Class<?> clazz=field.getType();
            if(clazz==Long.class) {
                parameterTypes.add(clazz);
                parameterValues.add(resultSet.getLong(index));
                index+=1;
            }
            if(clazz==String.class) {
                parameterTypes.add(clazz);
                parameterValues.add(resultSet.getString(index));
                index+=1;
            }
            if(clazz==LocalDateTime.class) {
                parameterTypes.add(clazz);
                parameterValues.add(resultSet.getObject(index,LocalDateTime.class));
                index+=1;
            }
            if(clazz==Boolean.class) {
                parameterTypes.add(clazz);
                parameterValues.add(resultSet.getBoolean(index));
                index+=1;
            }

        }


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
        long id=0;

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            addParameter(stmt);
            ResultSet resultSet=stmt.executeQuery();
            while(resultSet.next()) {
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

    public List<Long> selectLongs() {
        List<Long> ids=new ArrayList<>();

        try{
            stmt = simpleDB.getConnection().prepareStatement(sb.toString());
            addParameter(stmt);
            ResultSet resultSet=stmt.executeQuery();
            while(resultSet.next()) {
                ids.add(resultSet.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }
}
