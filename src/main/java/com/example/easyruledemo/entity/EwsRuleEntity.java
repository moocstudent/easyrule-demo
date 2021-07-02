package com.example.easyruledemo.entity;

import lombok.*;
import lombok.experimental.Accessors;
import microsoft.exchange.webservices.data.property.complex.Rule;

import javax.persistence.Transient;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 10:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
//@TableName("ews_rule")
public class EwsRuleEntity {

    private String ruleId;

    private String displayName;

    private Integer priority;

    private Boolean isEnabled;

    private String conditions;

    private String actionType;

    private String actions;

    /**
     * The rule status of is supported or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    @Transient
    private boolean isNotSupported;

    /**
     * The rule status of in error or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    @Transient
    private boolean isInError;

    //TODo 可以配置更新到邮件对应字段中,fireRule时不再单独传入
    private String itemMovedFolderIdStr;


}
