package com.example.easyruledemo.entity;

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
@ApiModel("ews规则配置实体")
public class EwsRuleEntity {

    //    @TableId(type = IdType.ASSIGN_UUID)
//    private String ruleId;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("规则表主键")
    private Long ruleId;

    @ApiModelProperty("规则名称")
    private String displayName;

    /**
     * {@link com.example.easyruledemo.enums.RuleType}
     */
    @ApiModelProperty("规则类型,指的是ews的规则类型")
    private String ruleType;

    //联查ews_topic_rule_relation获取
    /**
     * 权重1在规则的最下
     * 规则权重只在查回的时候使用
     * 在配置一个规则主题时,按照拖动顺序
     * 设定该规则的优先级,数值越小越靠上
     */
    @Transient
//    @ApiModelProperty("规则权重")
    private Integer priority;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否奏效,true奏效,false不奏效")
    private Boolean isEnabled;

    //简介
    @ApiModelProperty("规则简介")
    private String ruleDesc;

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
    @ApiModelProperty("规则动作")
    private String actions;

    /**
     * {@link ItemActionsEntity}
     */
    @ApiModelProperty("item动作集")
    private String itemActions;

    @ApiModelProperty("item动作集类型,如D下载,DC下载并拷贝")
    private String itemActionType;
    /**
     * The rule status of is supported or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    @ApiModelProperty("远端邮箱是否支持")
    private Boolean isNotSupported;

    /**
     * The rule status of in error or not.
     * 这个字段来自实际规则去update create操作后,在远端实际的状态
     */
    @ApiModelProperty("是否在异常中")
    private Boolean isInError;

    private Integer deleteFlag;

    //TODo 可以配置更新到邮件对应字段中,fireRule时不再单独传入
    private String itemMovedFolderIdStr;


}
