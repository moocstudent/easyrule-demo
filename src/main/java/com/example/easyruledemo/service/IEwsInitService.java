package com.example.easyruledemo.service;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-13 14:58
 */
public interface IEwsInitService {

    /**
     * 初始化邮件文件夹&初始化规则
     * @param itemActionTypeList 邮件执行文件执行类型获取邮件list并开启文件夹和规则初始化
     * @return
     */
    Integer initMailFoldersAndFireRules(List<String> itemActionTypeList);

    /**
     * 初始化订阅
     * @param itemActionTypeList 邮件执行文件执行类型获取邮件list并开启订阅
     * @return
     */
    Integer initSubscription(List<String> itemActionTypeList);

    /**
     * 无法设定每30秒一次,暂时还依旧使用Scheduled
     * 初始化邮件附件的下载轮询服务
     * @param itemActionTypeList 邮件执行文件执行类型获取邮件list并开始轮询
     * @return
     */
    @Deprecated
    Integer initEmailAttachEventPoll(List<String> itemActionTypeList);

}
