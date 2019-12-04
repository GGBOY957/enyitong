package com.example.demo.service;

import org.springframework.stereotype.Component;
import java.util.Properties;

import com.taosdata.jdbc.TSDBDriver;

import java.sql.*;

@Component
public class TSDBDate {
    private static final String JDBC_PROTOCAL = "jdbc:TAOS://";
    private static final String TSDB_DRIVER = "com.taosdata.jdbc.TSDBDriver";

    private String host = "127.0.0.1";
    private String user = "root";
    private String password = "taosdata";
    private int port = 0;
    private String jdbcUrl = "";

    private String databaseName = "db";
//    private String metricsName = "mt";
//    private String tablePrefix = "t";
//
//    private int tablesCount = 1;
//    private int loopCount = 2;
//    private int batchSize = 10;
//    private long beginTimestamp = 1519833600001L;
//
//    private long rowsInserted = 0;

    static {
        try {
            Class.forName(TSDB_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doMakeJdbcUrl() {
        // jdbc:TSDB://127.0.0.1:0/dbname?user=root&password=taosdata
//        System.out.println("\nJDBC URL to use:");
        this.jdbcUrl = String.format("%s%s:%d/%s?user=%s&password=%s", JDBC_PROTOCAL, this.host, this.port, this.databaseName,
                this.user, this.password);
//        System.out.println(this.jdbcUrl);
    }


    public void doInsert(long TimeMillis, String message) {
        System.out.println("\n---------------------------------------------------------------");
        System.out.println("Start inserting data:"+message);
        int start = (int) System.currentTimeMillis();
        StringBuilder sql = new StringBuilder("");
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("use " + databaseName);
            for (int i = 110; i < 115; i++) {
                sql = new StringBuilder("insert into t3 values");
                sql.append("(").append(TimeMillis).append(",\"").append(message).append("\")");
                stmt.executeUpdate(sql.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.printf("Failed to execute SQL: %s\n", sql.toString());
            System.exit(4);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(4);
        }
    }

    public void doQuery() {
        System.out.println("\n---------------------------------------------------------------");
        System.out.println("Starting querying data...");
        StringBuilder sql = new StringBuilder("");
        StringBuilder resRow = new StringBuilder("");
        ResultSet resSet = null;
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("use " + databaseName);
            sql = new StringBuilder("select * from t3");
            resSet = stmt.executeQuery(sql.toString());
            if (resSet == null) {
                System.out.println(sql + " failed");
                System.exit(4);
            }
            ResultSetMetaData metaData = resSet.getMetaData();
            int rows = 0;
            while (resSet.next()) {
                resRow = new StringBuilder();
                for (int col = 1; col <= metaData.getColumnCount(); col++) {
                    resRow.append(metaData.getColumnName(col)).append("=").append(resSet.getObject(col))
                            .append(" ");
                }
                System.out.println(resRow.toString());
                rows++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
