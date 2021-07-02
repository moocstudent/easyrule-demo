package com.example.easyruledemo.action;

import com.example.easyruledemo.rules.RuleStuff;
import lombok.Builder;
import microsoft.exchange.webservices.data.notification.StreamingSubscription;
import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;

/**
 * @Author: Frank
 * @Date: 2021-06-30 10:27
 */
@Builder
public class EmailAction implements Action {

    private String actionType;

    private String actions;

    @Override
    public void execute(Facts facts) throws Exception {
        RuleStuff.fire(actionType,actions);
    }
}
