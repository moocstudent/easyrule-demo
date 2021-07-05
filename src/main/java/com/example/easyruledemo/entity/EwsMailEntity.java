package com.example.easyruledemo.entity;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

/**
 * @Author: Frank
 * @Date: 2021-07-01 9:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("ews_mail_config")
public class EwsMailEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String mailId;

    private String email;

    private String password;

    private String host;

    private Integer port;

    private Integer recvHour;

    //每个邮箱对应一个收件主题
    private String topicId;

    /**
     * 这里联查回来,做为该邮件的创建文件夹初始化依据
     * 这里的key为folderCode,value为对应该folderCode的实体类
     * 比如获取 mailFolders.get("moveToFolder")获取要移动入的文件夹
     */
//    @Transient
//    private Map<String,EwsFoldersEntity> mailFolders;
    @Transient
    private Map<String, EwsFoldersEntity> mailFoldersMap;
    @Transient
    private EwsFoldersEntity mailFolders;

}
