package com.example.easyruledemo.service.impl;

import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.MailConfigEntity;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsFolderService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 14:39
 */
@Service
public class EwsEmailServiceImpl implements IEwsEmailService {
    @Autowired
    private IEwsFolderService ewsFolderService;

    @Override
    public Map<String, List<String>> createFolderByEmailConfigs(List<MailConfigEntity> mailConfigEntityList) {
        Map<String, List<String>> emailFolderIds = new HashMap<>();
        for (MailConfigEntity mailConfig : mailConfigEntityList) {
            try {
                List<String> folderIds = ewsFolderService.createFolder(
                        mailConfig.getMailFolders().getFolderNames(), WellKnownFolderName.Inbox, mailConfig);
                emailFolderIds.put(mailConfig.getEmail(), folderIds);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return emailFolderIds;
    }

    @Override
    public Integer downLoadAttachment(EmailMessage message) {
        return null;
    }

    @Override
    public List<MailConfigEntity> getMailConfigList(MailConfigEntity mailConfig) {
        //for test
        /**
         * LambdaQuery
         * .eq(MailConfigEntity::根据关联表查询符合这次要求的集合，
         * 如有的需要进行附件下载，有的需要进行邮件清理，会分为不同的task执行
         */
        MailConfigEntity mailConfigEntity = MailConfigEntity.builder()
                .configId("9id")
                .email("implementsteam@outlook.com")
                .password("zhangqi1112")
                .mailFolders(EwsFoldersEntity.builder()
                        .folderIds(Arrays.asList("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")).build())
                .build();
        List mailConfigList = new ArrayList<>();
        mailConfigList.add(mailConfigEntity);
        return mailConfigList;
    }
}
