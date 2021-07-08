package com.example.easyruledemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Frank
 * @Date: 2021-06-25 13:50
 */
@Getter
@AllArgsConstructor
public enum ItemActionType {

    D("D","附件下载"),
    DC("DC","附件下载并拷贝");

    private String code;
    private String description;
}
