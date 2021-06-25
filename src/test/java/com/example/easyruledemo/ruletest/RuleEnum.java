package com.example.easyruledemo.ruletest;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Frank
 * @Date: 2021-06-25 13:50
 */
@Getter
@AllArgsConstructor
public enum RuleEnum {

    D("D","下载"),
    C("C","下载bing 拷贝");

    private String code;
    private String description;
}
