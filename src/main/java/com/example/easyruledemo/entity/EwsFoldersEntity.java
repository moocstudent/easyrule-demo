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
//@TableName("mail_folders")
public class EwsFoldersEntity {

    private String belongEmailId;

    //创建folder后保存id到相关表 这里联查回来的
    private List<String> folderIds;

    private List<String> folderNames;

    private Integer deleteFlag;

}
