package com.example.easyruledemo.service;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-13 14:58
 */
public interface IEwsInitService {

    //初始化邮件文件夹&初始化规则
    Integer initMailFoldersAndFireRules(List<String> itemActionTypeList);

}
