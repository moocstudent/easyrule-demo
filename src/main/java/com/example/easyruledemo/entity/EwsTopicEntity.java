package com.example.easyruledemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

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

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主题表主键")
    private String topicId;
//
//    @TableId(type = IdType.AUTO)
//    @ApiModelProperty("主题表主键")
//    private Long topicId;

    @ApiModelProperty(value = "主题名称",required = true)
    private String topicName;

    @ApiModelProperty(value = "主题简介",required = true)
    private String topicDesc;

    /**
     * 形式大概类似:
     * {'D':'xxxkkkkkjdjj','DC':'kkkjjjddd'}
     */
    @ApiModelProperty(value = "主题配置串,根据子规则类型生成",required = true)
    private String topicConfig;

    @ApiModelProperty("激活标记,1已激活,0未激活")
    private Integer active;

    private Integer deleteFlag;

}
