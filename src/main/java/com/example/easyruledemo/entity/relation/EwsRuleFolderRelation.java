package com.example.easyruledemo.entity.relation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 10:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("ews_rule_folder_relation")
@ApiModel("ews规则与文件夹关联表")
public class EwsRuleFolderRelation {

    //    @TableId(type = IdType.ASSIGN_UUID)
//    private String relationId;
//    @TableId(type = IdType.AUTO)
//    @ApiModelProperty("关联表主键")
//    private Long relationId;
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("关联表主键")
    private String relationId;


    @ApiModelProperty(value = "规则表主键",required = true)
    private String ruleId;

    //pk of folder table
    @ApiModelProperty(value = "文件表主键",required = true)
    private String ewsFolderId;

    @TableField(exist = false)
    private String folderCode;
    @TableField(exist = false)
    private String folderName;

    @ApiModelProperty(value = "email表主键",required = true)
    private String mailId;

    //unionId
    @ApiModelProperty("用于生成文件夹的folderId unionId,不回显")
    private String folderId;

    private Integer deleteFlag;

}
