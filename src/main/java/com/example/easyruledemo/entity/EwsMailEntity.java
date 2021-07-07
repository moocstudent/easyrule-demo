package com.example.easyruledemo.entity;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("ews邮箱配置实体")
public class EwsMailEntity {

//    @TableId(type = IdType.ASSIGN_UUID)
//    private String mailId;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("邮件配置表主键")
    private Long mailId;

    @ApiModelProperty("邮件地址")
    private String email;

    @ApiModelProperty("密码")
    private String password;

    private String host;

    private Integer port;

    private Integer recvHour;

    //每个邮箱对应一个收件主题
    @ApiModelProperty("该邮件关联的收件主题id")
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
    //满足本次条件的mailRuleList集合
    @Transient
    @ApiModelProperty("满足本次条件的mailRuleList集合")
    private List<EwsRuleEntity> mailRulesValidThisTime;

    private Integer deleteFlag;

}
