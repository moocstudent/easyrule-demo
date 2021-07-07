package com.example.easyruledemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 9:17
 * 一个ews_mail_config只能对应一个主题,
 * 不同的ews_mail_config可以对应同一个主题
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("ews_mail_topic")
@ApiModel("ews收件主题配置实体")
public class EwsTopicEntity {

//    @TableId(type = IdType.ASSIGN_UUID)
//    private String topicId;
//
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主题表主键")
    private Long topicId;

    @ApiModelProperty("主题名称")
    private String topicName;

    @ApiModelProperty("主题简介")
    private String topicDesc;

    @ApiModelProperty("主题配置串,根据子规则类型生成")
    private String topicConfig;

    private Integer deleteFlag;

}
