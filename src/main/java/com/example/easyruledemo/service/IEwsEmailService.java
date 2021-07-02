package com.example.easyruledemo.service;

import microsoft.exchange.webservices.data.core.service.item.EmailMessage;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 22:55
 * 邮件处理
 */
public interface IEwsEmailService {
    //TODO 下载附件
    void downLoadAttachment(EmailMessage message);
}
