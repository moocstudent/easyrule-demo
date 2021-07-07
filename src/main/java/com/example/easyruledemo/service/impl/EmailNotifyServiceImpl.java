package com.example.easyruledemo.service.impl;

import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.service.IEmailNotifyService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 16:44
 */
@Service
@Slf4j
@Deprecated
public class EmailNotifyServiceImpl implements IEmailNotifyService {
    @Override
    public PullSubscription getPullSubscription(List<FolderId> foldersBeWatching, int timeout) {
        try {
            return EwsExContainer.defaultExchangeService().subscribeToPullNotifications(foldersBeWatching, timeout
                    /* timeOut: the subscription will end if the server is not polled within 5 minutes. */, null /* watermark: null to start a new subscription. */,
                    EventType.NewMail, EventType.Created, EventType.Deleted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("get subscription error:" + e);
        }
    }


}
