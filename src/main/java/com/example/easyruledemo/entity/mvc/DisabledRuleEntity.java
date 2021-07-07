package com.example.easyruledemo.entity.mvc;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-07 14:11
 * 当取消一条规则时,选定该邮箱的topic下展示的子规则
 * 点击相关取消按钮,获取该ruleId,以及传送该邮件地址
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel("取消一条规则传入参数entity")
public class DisabledRuleEntity {

    @ApiModelProperty("规则id")
    private String ruleId;
//
//    @ApiModelProperty("展示名称")
//    private String displayName;

    @ApiModelProperty("邮件地址")
    private String email;

}
