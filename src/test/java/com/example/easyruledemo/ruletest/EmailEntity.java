package com.example.easyruledemo.ruletest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: Frank
 * @Date: 2021-06-25 11:29
 */
@Data
@AllArgsConstructor
@Builder
public class EmailEntity {

    private String subject;

    private String senderAddress;

}
