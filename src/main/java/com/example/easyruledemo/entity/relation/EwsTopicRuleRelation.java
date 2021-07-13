package com.example.easyruledemo.entity.relation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("ews_topic_rule_relation")
@ApiModel("ews主题与规则关联表")
public class EwsTopicRuleRelation {

    //    @TableId(type = IdType.ASSIGN_UUID)
//    private String relationId;
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("关联表主键")
    private Long relationId;
//    @TableId(type = IdType.AUTO)
//    @ApiModelProperty("关联表主键")
//    private Long relationId;

    @ApiModelProperty(value = "主题表主键",required = true)
    private Long topicId;
    @ApiModelProperty(value = "规则表主键",required = true)
    private Long ruleId;

    //关联时,ruleLevel在同一个topic中不能重复
    @ApiModelProperty(value = "规则等级,一个主题下不能重复",required = true)
    private Integer ruleLevel;
    @ApiModelProperty("权重等级,一个主题下不能重复")
    private Integer priority;

    private Integer deleteFlag;

}
