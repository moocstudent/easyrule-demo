package com.example.easyruledemo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: zhangQi
 * @Date: 2021-07-06 15:18
 */
@Data
@ToString
@ApiModel("ews返回实体")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private String consoleMsg;

    private T data;

    public Result(){
        super();
    }

    public Result(Integer code,String message){
        super();
        this.code = code;
        this.message = message;
    }

}
