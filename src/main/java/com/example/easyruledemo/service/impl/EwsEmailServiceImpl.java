package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.mapper.EwsMailMapper;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.service.IEwsTopicService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-03 14:39
 */
@Service
@Slf4j
public class EwsEmailServiceImpl extends ServiceImpl<EwsMailMapper, EwsMailEntity>
        implements IEwsEmailService {
    @Autowired
    private IEwsFolderService ewsFolderService;
    @Autowired
    private IEwsTopicService ewsTopicService;
    @Autowired
    private IEwsRuleService ewsRuleService;

    @Override
    public Map<String, List<String>> createFolderByEmailConfigs(List<EwsMailEntity> mailConfigEntityList) {
        Map<String, List<String>> emailFolderIds = new HashMap<>();
        for (EwsMailEntity mailConfig : mailConfigEntityList) {
            try {
                //todo 改为从mail对应主题对应规则对应的文件夹中获取
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
    public List<EwsMailEntity> getMailConfigList(EwsMailEntity mailConfig) {


        //for test
        /**
         * LambdaQuery
         * .eq(MailConfigEntity::根据关联表查询符合这次要求的集合，
         * 如有的需要进行附件下载，有的需要进行邮件清理，会分为不同的task执行
         */
        EwsMailEntity mailConfigEntity = EwsMailEntity.builder()
//                .mailId("9id")
                .mailId(1L)
                .email("implementsteam@outlook.com")
                .password("zhangqi1112")
                .mailFolders(EwsFoldersEntity.builder()
                        .folderIds(Arrays.asList("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")).build())
                .build();
        EwsMailEntity mailConfigEntity2 = EwsMailEntity.builder()
//                .mailId("9id")
                .mailId(1L)
                .email("frankimplements@outlook.com")
                .password("zhangqi1112")
                .mailFolders(EwsFoldersEntity.builder()
                        .folderIds(Arrays.asList("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")).build())
                .build();
        List mailConfigList = new ArrayList<>();
        mailConfigList.add(mailConfigEntity);
        mailConfigList.add(mailConfigEntity2);
        return mailConfigList;
    }

    @Override
    public List<EwsMailEntity> getMailConfigList(EwsMailEntity mailConfig, ItemActionType... ruleTypes) {
        //查包含指定ruleType的ewsMail集合
        List<EwsMailEntity> mailEntityList = new LambdaQueryChainWrapper<EwsMailEntity>(baseMapper)
                .eq(EwsMailEntity::getDeleteFlag, 0)
                .eq(StringUtils.isEmpty(mailConfig.getEmail()), EwsMailEntity::getEmail, mailConfig.getEmail())
                .eq(StringUtils.isEmpty(mailConfig.getTopicId()), EwsMailEntity::getTopicId, mailConfig.getTopicId())
                .eq(StringUtils.isEmpty(mailConfig.getHost()), EwsMailEntity::getHost, mailConfig.getHost())
                .orderByDesc(EwsMailEntity::getMailId)
                .list();
        List<ItemActionType> ruleEnums = Arrays.asList(ruleTypes);
        long validRuleEnumCount = mailEntityList.stream()
                .filter(mail -> ruleEnums.stream()
                        .filter(ruleType -> mail.getTopicId().indexOf(ruleType.getCode()) > 1)
                        .count() > 0)
                .count();
        if (validRuleEnumCount == 0) {
            return Collections.emptyList();
        }
        //过滤符合当前传入ruleTypes的mailEntity
        List<EwsMailEntity> ewsMailList = ruleEnums.stream()
                .map(ruleType -> {
                    return mailEntityList.stream()
                            .filter(mail -> {
                                return mail.getTopicId().indexOf(ruleType.getCode()) > 1;
                            })
                            .collect(Collectors.toList());
                })
                .reduce((validRuleEml1, validRuleEml2) -> {
                    validRuleEml1.addAll(validRuleEml2);
                    return validRuleEml1;
                }).orElse(Collections.emptyList());
        log.info("valid ruleTypes mail list:{}",ewsMailList);
        //根据topicConfig以及给定需要的
        for(EwsMailEntity mail:ewsMailList){
            EwsTopicEntity topic = ewsTopicService.getTopicByMailId(mail.getMailId());
            String topicConfig = topic.getTopicConfig();
            List<EwsRuleEntity> ewsRuleEntityList = ewsRuleService.filterRuleByConfigEnum(topicConfig, ruleEnums);
            mail.setMailRulesValidThisTime(ewsRuleEntityList);
//            ewsRuleService.filterRuleByConfigEnum(mail.)
//            List<EwsRuleEntity> rulesByTopicId = ewsRuleService.getRulesByTopicId(mail.getTopicId());
        }
        return ewsMailList;
    }

    @Override
    public EwsMailEntity findOne(String mailId) {
        return baseMapper.selectById(mailId);
    }

    @Override
    public Boolean saveOrUpdateEmail(EwsMailEntity ewsMail) {
        return super.saveOrUpdate(ewsMail);
    }

    @Override
    public List<EwsMailEntity> listSelective(EwsMailEntity ewsMail) {
        return new LambdaQueryChainWrapper<EwsMailEntity>(baseMapper)
                .eq(EwsMailEntity::getDeleteFlag, 0)
                .like(StringUtils.isEmpty(ewsMail.getEmail()), EwsMailEntity::getEmail, ewsMail.getEmail())
                .eq(StringUtils.isEmpty(ewsMail.getTopicId()), EwsMailEntity::getTopicId, ewsMail.getTopicId())
                .orderByDesc(EwsMailEntity::getMailId)
                .list();
    }

    @Override
    public Integer delOne(String mailId) {
        return baseMapper.deleteById(mailId);
    }

}
