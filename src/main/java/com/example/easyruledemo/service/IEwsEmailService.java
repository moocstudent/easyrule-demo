package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.MailConfigEntity;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 22:55
 * 邮件处理
 */
public interface IEwsEmailService {

    //根据邮件配置创建各自的初始化文件夹用于筛选分拆邮件,返回folderId集合,以邮件为key
    Map<String,List<String>> createFolderByEmailConfigs(List<MailConfigEntity> mailConfigEntityList);
    //TODO 下载附件
    Integer downLoadAttachment(EmailMessage message);
    //动态获取邮件配置list
    List<MailConfigEntity> getMailConfigList(MailConfigEntity mailConfig);
}
