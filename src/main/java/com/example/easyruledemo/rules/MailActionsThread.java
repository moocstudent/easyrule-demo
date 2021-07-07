package com.example.easyruledemo.rules;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.sub.ItemActionsEntity;
import com.example.easyruledemo.enums.FolderNameEnum;
import com.example.easyruledemo.enums.RuleEnum;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.Attachment;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-07 15:15
 */
@Slf4j
@Data
public class MailActionsThread extends Thread{

    private String saveAttachPath = "";

    private String dateformat = "yyyy-MM-dd HH:mm:ss";
    protected EmailMessage emailMessage = null;
    protected EwsMailEntity mailConfig = null;
//    protected List<EmailMessage> emailMessageList = null;

    public MailActionsThread(){}

    public MailActionsThread(EmailMessage message){
        this.emailMessage = message;
    }

    public MailActionsThread(EmailMessage message, EwsMailEntity mailConfig){
        this.emailMessage = message;
        this.mailConfig = mailConfig;
    }

//    public MailActionsThread(List<EmailMessage> messagesList){
//        this.emailMessageList = messagesList;
//    }

    /**
     * 下载附件保存到指定目录
     * @param savePath
     */
    public void download(String savePath){
        this.saveAttachmentsFromEmail(savePath,null,null);
    }

    /**
     * 下载附件保存到指定目录 并拷贝到另一个目录
     * @param savePath
     * @param copyPath
     */
    public void download(String savePath,String copyPath){
        this.saveAttachmentsFromEmail(savePath,copyPath,null);
    }

//    public void saveAttachmentsFromEmailList(String savePath,String copyPath,String nameFilter){
//        this.saveAttachmentsFromEmail(savePath);
//    }

    /**
     * 保存附件
     * @param savePath
     * @param copyPath
     * @param nameFilter
     */
    public void saveAttachmentsFromEmail(String savePath,String copyPath,String nameFilter){
        if(StringUtils.isEmpty(savePath)){
            log.error("保存路径不能为空");
            return;
        }
        try {
            String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(emailMessage.getDateTimeReceived());
            if(emailMessage.getHasAttachments()){
                List<Attachment> attachs = emailMessage.getAttachments().getItems();
                if(savePath.contains("YYYYMMDD")){
                    savePath = savePath.replace("YYYYMMDD",yyyyMMdd);
                }
                File saveDir = new File(savePath);
                if (!saveDir.exists()){
                    saveDir.mkdir();
                }
                if(StringUtils.isEmpty(copyPath)){
                    if(copyPath.contains("YYYYMMDD")){
                        copyPath = copyPath.replace("YYYYMMDD",yyyyMMdd);
                        File copyDir = new File(copyPath);
                        if (!copyDir.exists()){
                            copyDir.mkdir();
                        }
                    }
                }
                //迭代获取附件
                for(Attachment attach:attachs){
                    if(attach instanceof FileAttachment){
                        //接收邮件到临时目录
                        String attachName = attach.getName();
                        //需要根据过滤条件判断是不是需要下载的附件
                        if(!StringUtils.isEmpty(nameFilter) && !attachName.contains(nameFilter)){
                            continue;
                        }
                        ((FileAttachment) attach).load(savePath+File.separator +attachName);
                        log.info("保存主题:"+emailMessage.getSubject()+"附件"+(savePath+File.separator+attachName));
                        //如果copyPath不为空,则进行文件拷贝,否则不然
                        if(!StringUtils.isEmpty(copyPath)){
                            copyFileByChannelTransfer(savePath+File.separator+attachName,copyPath+File.separator+attachName);
                            log.info("拷贝邮件:{}附件 从:{}到指定目录中:{}",emailMessage.getSubject(),
                                    savePath+File.separator+attachName,copyPath+File.separator+attachName);
                        }
                    }else{
                        log.error("邮件:{}没有附件,规则判断出错",emailMessage.getSubject());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                log.info("保存邮件:{}附件异常",emailMessage.getSubject());
            } catch (ServiceLocalException serviceLocalException) {
                serviceLocalException.printStackTrace();
            }
        }
    }

    /**
     * 文件拷贝
     * @param sourcePath 源目件
     * @param targetPath 拷贝文件
     */
    private void copyFileByChannelTransfer(String sourcePath,String targetPath){
        try(FileChannel inChannel = FileChannel.open(Paths.get(sourcePath), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE)
        ){
            inChannel.transferTo(0,inChannel.size(),outChannel);
        }catch(IOException e){
            e.printStackTrace();
            log.error("拷贝附件异常:{}",e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void start(){
        List<EwsRuleEntity> rulesThisTime = mailConfig.getMailRulesValidThisTime();
        for(EwsRuleEntity rule:rulesThisTime){
            ItemActionsEntity itemActions = JSONObject.parseObject(JSON.toJSONString(rule.getItemActions()), ItemActionsEntity.class);
            if(RuleEnum.D.getCode().equals(rule.getItemActionType())){
                this.download(itemActions.getDownloadPath());
            }else if (RuleEnum.DC.getCode().equals(rule.getItemActionType())){
                this.download(itemActions.getDownloadPath(),itemActions.getCopyPath());
            }else{
                log.error("错误的itemAction执行动作");
            }
        }
        //下载完成后将文件移动入已下载附件文件夹
        //todo 赋值过去下载完成后移入的文件夹 同上 获取list的时候set回去
        emailMessage.move(new FolderId(
                mailConfig.getMailFoldersMap()
                        .get(FolderNameEnum.ATTACH_ALREADY.getCode()).getFolderId())
        );
    }
}
