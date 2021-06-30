package com.example.easyruledemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: Frank
 * @Date: 2021-06-30 8:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleConditionEntity {

    private String subject;

    private String sender;

    private String senderAddress;

    private LocalDateTime sentDate;

    private String bodyText;

    private String replySign;

    private Boolean seen;

    private String handler;

}
