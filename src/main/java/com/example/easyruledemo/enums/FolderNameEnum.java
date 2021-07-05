package com.example.easyruledemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 15:10
 * 文件夹名称枚举类,用于直接放入folder表
 */
@Getter
@AllArgsConstructor
public enum FolderNameEnum {

    ATTACH_UN("attach-un-download","待下载附件邮件"),
    ATTACH_ALREADY("attach-already-download","已下载附件邮件"),
    PREVIEW_NOW("preview-now","请及时查看");

    private String code;
    private String usename;

}
