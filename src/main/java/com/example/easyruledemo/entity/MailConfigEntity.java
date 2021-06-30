package com.example.easyruledemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Frank
 * @Date: 2021-06-30 17:47
 * 邮箱配置,对应多个收件邮箱
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("mail_config")
public class MailConfigEntity {

    @TableId(type= IdType.ASSIGN_UUID)
    private String configId;

    private String email;

    private String password;

    private String host;

    private Integer port;

    private Integer recvHour;
}
