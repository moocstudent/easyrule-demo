package com.example.easyruledemo.service;

import microsoft.exchange.webservices.data.misc.IAsyncResult;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.property.complex.FolderId;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 16:12
 */
//@FunctionalInterface
public interface IEmailNotifyService {

    /**
     * Subscribe to pull notifications in the Inbox folder, and get notified when a new mail is received,
     * when an item or folder is created, or when an item or folder is deleted.
     * @param foldersBeWatching
     * @param timeout
     * @return
     */
    PullSubscription getPullSubscription(List<FolderId> foldersBeWatching,int timeout);
//
//    public Integer notificationStart();
//
//    public Integer notificationEnd();


//    IEmailNotifyService START = ()
//
//    IAsyncResult asyncResult();
//    IAsyncResult asyncResult();


}
