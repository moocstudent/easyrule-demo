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
 * @Date: 2021-07-03 14:53
 * 指明该邮件有哪些folder
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("ews_mail_folders")
@ApiModel("ews文件夹实体")
public class EwsFoldersEntity {
    //使用单个文件id存一条形式

    //主键
//    @TableId(type = IdType.ASSIGN_UUID)
//    private String ewsFolderId;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("文件夹表主键")
    private Long ewsFolderId;

    //floderId字符串
    @ApiModelProperty("文件夹id字符串,规则生成到邮箱后产生,不可被修改")
    private String folderId;

    //attach-un-download
    @ApiModelProperty("文件夹枚举值")
    private String folderCode;

    //待下载附件邮件
    @ApiModelProperty("文件夹名称")
    private String folderName;

//
//    //创建folder后保存id到相关表 这里联查回来的
    //todo 这里将不再使用这个字段,将从邮箱对应topic对应的rule对应rule需要的文件夹来获取
    //todo 现在只是测试用
    private List<String> folderIds;
//
    //todo 这里将不再使用这个字段,将从邮箱对应topic对应的rule对应rule需要的文件夹来获取
    //todo 现在只是测试用
    private List<String> folderNames;

    @ApiModelProperty("删除标记,1已删除,0未删除")
    private Integer deleteFlag;

}
