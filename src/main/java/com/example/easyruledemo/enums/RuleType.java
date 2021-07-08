package com.example.easyruledemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zhangQi
 * @Date: 2021-07-08 9:52
 * ews中规则类型,如移入文件夹,拷贝到哪个文件夹,删除还是抄送
 */
@Getter
@AllArgsConstructor
public enum RuleType {
    MOVE("moveToFolder","移动到远端的文件夹"),
    COPY("copyToFolder","拷贝到远端的文件夹"),
    DEL("delete","删除邮件");

    private String code;
    private String description;
}
