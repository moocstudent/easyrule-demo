package com.example.easyruledemo.entity.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 12:35
 * transform from com.example.easyruledemo.entity.EwsRuleEntity.itemActions
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ApiModel("item执行动作实体")
public class ItemActionsEntity {

    @ApiModelProperty("下载路径")
    private String downloadPath;

    @ApiModelProperty("拷贝路径")
    private String copyPath;

//    private String otherActions;

}
