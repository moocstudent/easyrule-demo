package com.example.easyruledemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zhangQi
 * @Date: 2021-07-06 15:24
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    UNKOWN(-1,"未知异常"),
    SUCCESS(0,""),
    E(7001,"接口调用异常7001"),
    F(7002,"接口调用异常7002");

    private Integer code;

    private String message;

}
