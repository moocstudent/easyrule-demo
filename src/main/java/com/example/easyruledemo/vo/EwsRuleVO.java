package com.example.easyruledemo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.easyruledemo.entity.sub.EwsActionsEntity;
import com.example.easyruledemo.entity.sub.EwsConditionsEntity;
import com.example.easyruledemo.entity.sub.ItemActionsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("ews规则实体")
public class EwsRuleVO {

//    @TableId(type = IdType.ASSIGN_UUID)
//    private String ruleId;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("规则表主键")
    private Long ruleId;

    @ApiModelProperty("规则展示名")
    private String displayName;

    //简介
    @ApiModelProperty("规则介绍")
    private String ruleDesc;

    //联查ews_topic_rule_relation获取
    /**
     * 权重1在规则的最下
     */
    @Transient
    @ApiModelProperty("规则权重")
    private Integer priority;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用该规则")
    private Boolean isEnabled;


    //规则等级 是设定规则时预设的,rulelevel指定组合,priority指定顺序
    @ApiModelProperty("规则等级")
    private Integer ruleLevel;

    /**
     * {见测试类 TransformTest}
     * {@link EwsConditionsEntity}
     */
    @ApiModelProperty("规则条件")
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
