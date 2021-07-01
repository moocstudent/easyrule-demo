package com.example.easyruledemo.serv;

import com.example.easyruledemo.entity.MailConfigEntity;

import java.util.List;

/**
 * @Author: Frank
 * @Date: 2021-07-01 9:06
 */
public interface IMailConfigService {
    //获取全部list
    List<MailConfigEntity> listAll();
}
