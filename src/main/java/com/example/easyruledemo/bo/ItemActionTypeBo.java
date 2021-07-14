package com.example.easyruledemo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-13 16:06
 */
@ApiModel(value = "文件执行类型请求bo")
@Data
@NoArgsConstructor
public class ItemActionTypeBo {

    @ApiModelProperty(value = "需要发起初始化的itemActionType的集合")
    private List<String> itemActionType;

}
