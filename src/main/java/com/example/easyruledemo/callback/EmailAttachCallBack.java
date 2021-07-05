package com.example.easyruledemo.callback;

import microsoft.exchange.webservices.data.misc.AsyncCallbackImplementation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: Frank
 * @Date: 2021-07-01 10:01
 * 这个异步执行实现类,会在创建subscription时指定
 *
 */
@Deprecated
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
