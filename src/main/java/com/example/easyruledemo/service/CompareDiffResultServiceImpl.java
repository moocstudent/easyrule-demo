package com.example.easyruledemo.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author: Frank
 * @Date: 2021-06-28 16:36
 */
@Service
public class CompareDiffResultServiceImpl {



    public static ResultSet getDiffByTwoData(Connection connection,String targetSql,String tableName){
        String minusUnionSql = "select * from (("+targetSql+") minus select * from "+tableName+") union" +
                "select * from (select * from "+tableName+" minus select * from ("+targetSql+"))";
        try(PreparedStatement preparedStatement = connection.prepareStatement(minusUnionSql)){
            long total = 0;
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                String result = resultSet.toString();
                if(resultSet.next()){
                    total = resultSet.getLong(1);
                }
                System.out.println("差异数据量为:"+total);
                if(total>0){
                    return resultSet;
                }
                return null;
            }
        }catch(Exception e){
            System.out.println("查询差异条数出现异常:"+e);
            throw new RuntimeException("查询差异条数异常");
        }
    }



}
