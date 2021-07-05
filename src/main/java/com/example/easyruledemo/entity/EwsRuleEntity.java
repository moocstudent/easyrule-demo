package com.example.easyruledemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.easyruledemo.entity.sub.EwsActionsEntity;
import com.example.easyruledemo.entity.sub.EwsConditionsEntity;
import com.example.easyruledemo.entity.sub.ItemActionsEntity;
import lombok.*;
import lombok.experimental.Accessors;

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
@TableName("ews_rule")
public class EwsRuleEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String ruleId;

    private String displayName;

    //联查ews_topic_rule_relation获取
    @Transient
    private Integer priority;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    //简介
    private String ruleDesc;

    //规则等级 是设定规则时预设的,rulelevel指定组合,priority指定顺序
    private Integer ruleLevel;

    /**
     * {见测试类 TransformTest}
     * {@link EwsConditionsEntity}
     */
    private String conditions;

    /**
     * {见测试类 TransformTest}
     * {@link EwsActionsEntity}
     */
    private String actions;

    /**
     * {@link ItemActionsEntity}
     */
    private String itemActions;

    private String itemActionType;
    /**
     * The rule status of is supported or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    private Boolean isNotSupported;

    /**
     * The rule status of in error or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    private Boolean isInError;

    private Integer deleteFlag;

    //TODo 可以配置更新到邮件对应字段中,fireRule时不再单独传入
    private String itemMovedFolderIdStr;


}
