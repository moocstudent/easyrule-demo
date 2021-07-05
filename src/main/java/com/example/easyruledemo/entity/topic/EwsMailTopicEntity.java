package com.example.easyruledemo.entity.topic;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 9:17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
//@TableName("ews_mail_topic")
public class EwsMailTopicEntity {

    private String topicId;

    private String topicName;

    private String topicDesc;

    private String topicConfig;

    private Integer deleteFlag;

}
