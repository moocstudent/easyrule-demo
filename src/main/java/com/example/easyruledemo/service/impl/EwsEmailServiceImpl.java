package com.example.easyruledemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.enums.FolderNameEnum;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.mapper.EwsMailMapper;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.service.IEwsTopicService;
import com.example.easyruledemo.util.BeanUtil;
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

//    @Override
//    public Map<String, List<String>> createFolderByEmailConfigs(List<EwsMailEntity> mailConfigEntityList) {
//        Map<String, List<String>> emailFolderIds = new HashMap<>();
//        for (EwsMailEntity mailConfig : mailConfigEntityList) {
//            try {
//                //todo 改为从mail对应主题对应规则对应的文件夹中获取
//                List<String> folderIds = ewsFolderService.createFolder(
//                        mailConfig.getMailFolders().getFolderNames(), WellKnownFolderName.Inbox, mailConfig);
//                emailFolderIds.put(mailConfig.getEmail(), folderIds);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
//        return emailFolderIds;
//    }

    @Override
    public Integer downLoadAttachment(EmailMessage message) {
        return null;
    }

    @Override
    @Deprecated
    public List<EwsMailEntity> getMailConfigList(EwsMailEntity mailConfig) {


        //for test
        /**
         * LambdaQuery
         * .eq(MailConfigEntity::根据关联表查询符合这次要求的集合，
         * 如有的需要进行附件下载，有的需要进行邮件清理，会分为不同的task执行
         */
        EwsMailEntity mailConfigEntity = EwsMailEntity.builder()
                .mailId("9id")
//                .mailId(1L)
                .email("implementsteam@outlook.com")
                .password("zhangqi1112")
                .mailFolders(EwsFoldersEntity.builder()
                        .folderIds(Arrays.asList("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==")).build())
                .build();
        EwsMailEntity mailConfigEntity2 = EwsMailEntity.builder()
                .mailId("9id")
//                .mailId(1L)
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
                .eq(!StringUtils.isEmpty(mailConfig.getEmail()), EwsMailEntity::getEmail, mailConfig.getEmail())
                .eq(!StringUtils.isEmpty(mailConfig.getTopicId()), EwsMailEntity::getTopicId, mailConfig.getTopicId())
//                .eq(StringUtils.isEmpty(mailConfig.getHost()), EwsMailEntity::getHost, mailConfig.getHost())
                .orderByDesc(EwsMailEntity::getMailId)
                .list();
        List<ItemActionType> ruleEnums = Arrays.asList(ruleTypes);
        //过滤符合当前传入ruleTypes的mailEntity
        List<EwsMailEntity> ewsMailList = ruleEnums.stream()
                .map(ruleType -> {
                    return mailEntityList.stream()
                            .filter(mail -> {
                                EwsTopicEntity topic = ewsTopicService.getTopicByMailId(mail.getMailId());
                                JSONObject ruleConfigJsonObject = JSONObject.parseObject(topic.getTopicConfig());
                                return ruleConfigJsonObject.containsKey(ruleType.getCode());
                            })
                            .collect(Collectors.toList());
                })
                .reduce((validRuleEml1, validRuleEml2) -> {
                    validRuleEml1.addAll(validRuleEml2);
                    return validRuleEml1;
                }).orElse(Collections.emptyList());
        log.info("valid ruleTypes mail list:{}",ewsMailList);
        if (ewsMailList.size()==0){
            log.warn("并没有符合这次要求的email");
            return Collections.emptyList();
        }
        //根据topicConfig以及给定需要的
        Map<String,EwsFoldersEntity> nameFolderMap = new HashMap<>();
        for(EwsMailEntity mail:ewsMailList){
            EwsTopicEntity topic = ewsTopicService.getTopicByMailId(mail.getMailId());
            String topicConfig = topic.getTopicConfig();
            log.info("topicConfig:{}",topicConfig);
            List<EwsRuleEntity> ewsRuleEntityList = ewsRuleService.filterRuleByConfigEnum(topicConfig, ruleEnums);
            List<String> ruleIdList = ewsRuleEntityList.stream()
                    .map(rule -> {
                        return rule.getRuleId();
                    }).collect(Collectors.toList());
            log.info("email 获取mailConfig:");
            mail.setMailRulesValidThisTime(ewsRuleEntityList);
            log.info("query ruleIdList:{}",ruleIdList);
            if(ruleIdList.size()==0){
                throw new RuntimeException("获取到ruleIdList为空,请检查重试.");
            }
            EwsFoldersEntity foldersEntity = ewsFolderService.findInRuleRelation(ruleIdList, FolderNameEnum.ATTACH_ALREADY.getCode());
            mail.setMailFolders(foldersEntity);
            nameFolderMap.put(FolderNameEnum.ATTACH_ALREADY.getCode(),foldersEntity);
            mail.setMailFoldersMap(nameFolderMap);
        }
        log.info("满足itemActionType条件的mail:"+ewsMailList);
        return ewsMailList;
    }

    @Override
    public List<EwsMailEntity> getMailConfigList(EwsMailEntity mailConfig, List<String> ruleTypeList) {
        return null;
    }

    @Override
    public EwsMailEntity findOne(String mailId) {
        return baseMapper.selectById(mailId);
    }

    @Override
    public EwsMailEntity findOne(Long mailId) {
        return baseMapper.selectById(mailId);
    }

    @Override
    public Boolean saveOrUpdateEmail(EwsMailEntity ewsMail) {
        return super.saveOrUpdate(BeanUtil.getTrimClass(ewsMail).get());
    }

    @Override
    public List<EwsMailEntity> listSelective(EwsMailEntity ewsMail) {
        return new LambdaQueryChainWrapper<EwsMailEntity>(baseMapper)
                .eq(EwsMailEntity::getDeleteFlag, 0)
                .like(!StringUtils.isEmpty(ewsMail.getEmail()), EwsMailEntity::getEmail, ewsMail.getEmail())
                .eq(!StringUtils.isEmpty(ewsMail.getTopicId()), EwsMailEntity::getTopicId, ewsMail.getTopicId())
                .orderByDesc(EwsMailEntity::getMailId)
                .list();
    }

    @Override
    public Integer delOne(String mailId) {
        return baseMapper.deleteById(mailId);
    }

    @Override
    public Integer inactive(String mailId) {
        LambdaUpdateWrapper<EwsMailEntity> updateWrapper =
                new LambdaUpdateWrapper<EwsMailEntity>()
                        .eq(EwsMailEntity::getMailId,mailId)
                        .eq(EwsMailEntity::getDeleteFlag,0);
        EwsMailEntity ewsMailEntity
                = EwsMailEntity.builder().active(0).mailId(mailId).build();
        return baseMapper.update(ewsMailEntity,updateWrapper);
    }

}
