package com.example.easyruledemo.condition;

import lombok.Builder;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;

/**
 * @Author: Frank
 * @Date: 2021-06-30 10:33
 */
@Builder
public class FootBallCondition implements Condition {


    @Override
    public boolean evaluate(Facts facts) {
        return false;
    }
}
