package com.ll.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {

    private HikariDataSource dataSource;


    public DataSourceProvider(DBConfig dbConfig) {
        HikariConfig config = new HikariConfig();
        dbConfig=DBConfig.getInstance();
        String jdbcURL="jdbc:mysql://"+dbConfig.getHost()+":"
                +dbConfig.getPortNum()+"/" +dbConfig.getDbname()+"?useSSL=false&serverTimezone=UTC";
        String username=dbConfig.getUsername();
        String password=dbConfig.getPassword();
        config.setJdbcUrl(jdbcURL); // MySQL URL
        config.setUsername(username); // 사용자명
        config.setPassword(password); // 비밀번호
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // MySQL JDBC 드라이버

        // 커넥션 풀 설정 (필요에 따라 수정)
        config.setMaximumPoolSize(10); // 최대 연결 수
        config.setMinimumIdle(5); // 최소 대기 커넥션 수
        config.setIdleTimeout(30000); // 유휴 커넥션 시간 (ms)
        config.setConnectionTimeout(20000); // 커넥션 획득 타임아웃 (ms)
        config.setMaxLifetime(1800000); // 커넥션 최대 수명 (ms)

        // 데이터 소스 생성
        dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
