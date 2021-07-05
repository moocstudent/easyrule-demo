package com.example.easyruledemo.entity;

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
//@TableName("ews_mail_folders")
public class EwsFoldersEntity {
    //使用单个文件id存一条形式

    //主键
    private String ewsFolderId;

    private String folderId;

    private String folderCode;

    private String folderName;

    private String belongRuleId;

//    private String belongEmailId;
//
//    //创建folder后保存id到相关表 这里联查回来的
    //todo 这里将不再使用这个字段,将从邮箱对应topic对应的rule对应rule需要的文件夹来获取
    private List<String> folderIds;
//
    //todo 这里将不再使用这个字段,将从邮箱对应topic对应的rule对应rule需要的文件夹来获取
    private List<String> folderNames;

    private Integer deleteFlag;

}
