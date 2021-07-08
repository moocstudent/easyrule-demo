package com.example.easyruledemo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class EwsTopicVO {

//    @TableId(type = IdType.ASSIGN_UUID)
//    private String topicId;
//
    @TableId(type = IdType.AUTO)
    private Long topicId;

    private String topicName;

    private String topicDesc;

    private String topicConfig;

    private Integer deleteFlag;

}
