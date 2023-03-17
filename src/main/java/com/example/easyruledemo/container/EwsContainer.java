package com.example.easyruledemo.container;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 14:08
 */
public class EwsContainer {

    //测试使用
    private static ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
    private static ExchangeCredentials credentials = new WebCredentials("implementsteam@outlook.com", "************");
    final private static String EX_URI = "https://outlook.office365.com/EWS/Exchange.asmx";
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
            service.setUrl(new URI(EX_URI));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("exchangeService:"+service);
        return service;
    }

    //singleton map
    private static Map<String,ExchangeService> exchangeServiceMap = new HashMap<>();

    /**
     * 其它传入参数的exchangeService获取,支持多个邮箱
     */
    public static ExchangeService getExchangeService(String email,String password){
        String key = email+password;
        if(exchangeServiceMap.get(key)!=null){
            return exchangeServiceMap.get(key);
        }
        ExchangeCredentials credentials = new WebCredentials(email, password);
        service.setCredentials(credentials);
        try {
            service.setUrl(new URI(EX_URI));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("exchangeService:"+service);
        exchangeServiceMap.put(key,service);
        return service;
    }
}
