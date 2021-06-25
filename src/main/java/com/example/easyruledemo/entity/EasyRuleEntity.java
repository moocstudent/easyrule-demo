package com.example.easyruledemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author: Frank
 * @Date: 2021-06-25 9:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("easy_rule")
public class EasyRuleEntity {

    @TableId(type= IdType.AUTO)
    private Long ruleId;

    private Long priority;

    private String ruleName;

    private String ruleDesc;

    private String ruleConditions;

    private String actionType;

    private String actions;

    private Integer deleteFlag;

}
