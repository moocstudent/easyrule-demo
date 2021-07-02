package com.example.easyruledemo.container;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 14:08
 */
public class EwsContainer {

    //测试使用
    static ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
    static ExchangeCredentials credentials = new WebCredentials("implementsteam@outlook.com", "zhangqi1112");

    //TODO 当WebCredentials相同时,则设置为单例,使用List<ExchangeService> 进行 单例 ExchangeService 管理

    /**
     * TODO 这里加入参数 创建不同邮件的service交换机
     * @return
     */
    public static ExchangeService defaultExchangeService(){
//
//        List<ExchangeCredentials> credentialsList = new ArrayList<>();
//        credentialsList.add(credentials);

//        for(ExchangeCredentials ex:credentialsList){
//            if(ex.getClass().)
//        }
//
        service.setCredentials(credentials);
        //TODO 内网配置的htffund.com可以使用下面两个 if else下
        //TODO 判定email是否https, https设定下面
//        try {
//            service.autodiscoverUrl("implementsteam@163.com",redirectionUrl ->
//                     redirectionUrl.toLowerCase().startsWith("https://"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //TODO 否则使用下面
//        try {
//            service.autodiscoverUrl("implementsteam@163.com");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //测试使用了ews/Exchange.asmx
        try {
            service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("exchangeService:"+service);
        return service;
    }

    /**
     * 其它传入参数的exchangeService获取
     */
}
