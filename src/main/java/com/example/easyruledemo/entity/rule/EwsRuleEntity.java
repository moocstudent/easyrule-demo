package com.example.easyruledemo.entity.rule;

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

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    //简介
    private String ruleDesc;

    //规则等级
    private Integer ruleLevel;

    /**
     * {见测试类 TransformTest}
     * {@link com.example.easyruledemo.entity.EwsConditionsEntity}
     */
    private String conditions;

    /**
     * {见测试类 TransformTest}
     * {@link com.example.easyruledemo.entity.EwsActionsEntity}
     */
    private String actions;

    /**
     * {@link com.example.easyruledemo.entity.ItemActionsEntity}
     */
    private String itemActions;

    private String actionType;
    /**
     * The rule status of is supported or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    private boolean isNotSupported;

    /**
     * The rule status of in error or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    private boolean isInError;

    //TODo 可以配置更新到邮件对应字段中,fireRule时不再单独传入
    private String itemMovedFolderIdStr;


}
