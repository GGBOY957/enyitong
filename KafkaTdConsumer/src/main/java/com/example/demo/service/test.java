package com.example.demo.service;

import org.springframework.stereotype.Component;
import java.util.Properties;

import com.taosdata.jdbc.TSDBDriver;

import java.sql.*;


@Component
public class test {
//    private TSDBDate tsdbDate;

    public static void main(String[] args){
        TSDBDate tsdbDate = new TSDBDate();
        tsdbDate.doMakeJdbcUrl();
//        for (int i=0;i<10;i++)
        tsdbDate.doInsert(1519833600001L,"this is a test");
        tsdbDate.doQuery();
    }


}
