package com.example.easyruledemo.vo;

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
public class EwsMailVO {

//    @TableId(type = IdType.ASSIGN_UUID)
//    private String mailId;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("邮件配置表主键")
    private Long mailId;

    @ApiModelProperty("邮件地址")
    private String email;

    @ApiModelProperty("密码")
    private String password;

    private Integer recvHour;

    //每个邮箱对应一个收件主题
    @ApiModelProperty("该邮件关联的收件主题id")
    private String topicId;

}
