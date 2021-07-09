package com.example.easyruledemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: zhangQi
 * @Date: 2021-07-08 11:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("ews_subscription")
@ApiModel("ews订阅实体")
public class EwsSubscriptionEntity {


    //    @TableId(type = IdType.ASSIGN_UUID)
//    private String topicId;
//
//    @TableId(type = IdType.AUTO)
//    @ApiModelProperty("主题表主键")
//    private Long ewsSubscriptionId;
    //
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主题表主键")
    private String ewsSubscriptionId;

    @ApiModelProperty(value = "主题id",required = true)
    private String subscriptionId;

    @ApiModelProperty(value = "订阅mapKey",required = true)
    private String subscriptionKey;

    @ApiModelProperty(value = "订阅时长,分钟",required = true)
    private Integer subscriptionMinutes;

    @ApiModelProperty("订阅时间")
    private LocalDateTime subscriptionDate;

    private Integer deleteFlag;
}
