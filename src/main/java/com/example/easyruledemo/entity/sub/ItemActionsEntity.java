package com.example.easyruledemo.entity.sub;

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
public class ItemActionsEntity {

    private String downloadPath;

    private String copyPath;

//    private String otherActions;

}
