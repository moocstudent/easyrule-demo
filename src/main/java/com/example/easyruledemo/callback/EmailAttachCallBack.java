package com.example.easyruledemo.callback;

import com.example.easyruledemo.delegate.EmailNotifyDelegate;
import com.example.easyruledemo.delegate.EmailSubscriptionErrorDelegate;
import com.example.easyruledemo.serv.IRuleService;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.service.impl.EwsRuleServiceImpl;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import microsoft.exchange.webservices.data.misc.AsyncCallbackImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: Frank
 * @Date: 2021-07-01 10:01
 */

public class EmailAttachCallBack extends AsyncCallbackImplementation {


    @Override
    public Object processMe(Future<?> task) {
        try {
            Object o = task.get();
            System.out.println("o:"+o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //获取对应提醒文件夹内文件
        System.out.println("task:~!!!!!!!!!"+task.toString());
        System.out.println("task:~!!done!!!!!!!"+task.isDone());
        return null;
    }

}
