package com.example.easyruledemo.entity;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Frank
 * @Date: 2021-07-01 9:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
//@TableName("mail_config")
public class MailConfigEntity {

//    @TableId(type= IdType.AUTO)
    private String configId;

    private String email;

    private String password;

    private String host;

    private Integer port;

    private Integer recvHour;
}
