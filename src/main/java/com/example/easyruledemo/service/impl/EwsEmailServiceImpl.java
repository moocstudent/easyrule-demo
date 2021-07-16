package com.example.easyruledemo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.example.easyruledemo.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
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
    public List<EwsMailEntity> getMailConfigList(EwsMailEntity mailConfig) {

        //查包含指定ruleType的ewsMail集合
        List<EwsMailEntity> mailEntityList = new LambdaQueryChainWrapper<EwsMailEntity>(baseMapper)
                .eq(EwsMailEntity::getDeleteFlag, 0)
                .eq(!StringUtils.isEmpty(mailConfig.getEmail()), EwsMailEntity::getEmail, mailConfig.getEmail())
                .eq(!StringUtils.isEmpty(mailConfig.getTopicId()), EwsMailEntity::getTopicId, mailConfig.getTopicId())
//                .eq(StringUtils.isEmpty(mailConfig.getHost()), EwsMailEntity::getHost, mailConfig.getHost())
                .orderByDesc(EwsMailEntity::getMailId)
                .list();
        //FIXME 先只设定一个邮箱
        if(mailEntityList.size()>0){
            mailConfig = mailEntityList.get(0);
        }
        Long mailId = mailConfig.getMailId();
        EwsTopicEntity topic = ewsTopicService.getTopicByMailId(mailId);
        log.info("topic:{}",topic);
        String topicConfig = topic.getTopicConfig();
//        JSONArray jsonArray = JSONObject.parseArray(topicConfig);
//        List<EwsRuleEntity> ewsRuleEntityList = jsonArray.toJavaList(EwsRuleEntity.class);

        Map<String, EwsFoldersEntity> nameFolderMap = new HashMap<>();
        //根据topicConfig以及给定需要的
        log.info("topicConfig:{}", topicConfig);
        List<EwsRuleEntity> ewsRuleEntityList = ewsRuleService.listRulesByTopicConfig(topicConfig);
        List<Long> ruleIdList = ewsRuleEntityList.stream()
                .map(rule -> {
                    return rule.getRuleId();
                }).collect(Collectors.toList());
        log.info("email 获取mailConfig:");
        mailConfig.setMailRulesValidThisTime(ewsRuleEntityList);
        log.info("query ruleIdList:{}", ruleIdList);
        if (ruleIdList.size() == 0) {
            throw new RuntimeException("获取到ruleIdList为空,请检查重试.");
        }
        //下载后不再转移文件夹,则注释,否则需要多配置,并根据不同规则设定的下载后文件转移文件夹code来获取
//        EwsFoldersEntity foldersEntity = ewsFolderService.findInRuleRelation(ruleIdList, FolderNameEnum.ATTACH_ALREADY.getCode());
//        mailConfig.setMailFolders(foldersEntity);
//        nameFolderMap.put(FolderNameEnum.ATTACH_ALREADY.getCode(), foldersEntity);
//        mailConfig.setMailFoldersMap(nameFolderMap);
        return Arrays.asList(mailConfig);

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
            List<Long> ruleIdList = ewsRuleEntityList.stream()
                    .map(rule -> {
                        return rule.getRuleId();
                    }).collect(Collectors.toList());
            log.info("email 获取mailConfig:");
            mail.setMailRulesValidThisTime(ewsRuleEntityList);
            log.info("query ruleIdList:{}",ruleIdList);
            if(ruleIdList.size()==0){
                throw new RuntimeException("获取到ruleIdList为空,请检查重试.");
            }
//            EwsFoldersEntity foldersEntity = ewsFolderService.findInRuleRelation(ruleIdList, FolderNameEnum.ATTACH_ALREADY.getCode());
//            mail.setMailFolders(foldersEntity);
//            nameFolderMap.put(FolderNameEnum.ATTACH_ALREADY.getCode(),foldersEntity);
//            mail.setMailFoldersMap(nameFolderMap);
        }
        log.info("满足itemActionType条件的mail:"+ewsMailList);
        return ewsMailList;
    }

    @Override
    public List<EwsMailEntity> getMailConfigList(EwsMailEntity mailConfig, List<ItemActionType> itemActionTypeList) {
        //查包含指定ruleType的ewsMail集合
        List<EwsMailEntity> mailEntityList = new LambdaQueryChainWrapper<EwsMailEntity>(baseMapper)
                .eq(EwsMailEntity::getDeleteFlag, 0)
                .eq(!StringUtils.isEmpty(mailConfig.getEmail()), EwsMailEntity::getEmail, mailConfig.getEmail())
                .eq(!StringUtils.isEmpty(mailConfig.getTopicId()), EwsMailEntity::getTopicId, mailConfig.getTopicId())
//                .eq(StringUtils.isEmpty(mailConfig.getHost()), EwsMailEntity::getHost, mailConfig.getHost())
                .orderByDesc(EwsMailEntity::getMailId)
                .list();
        //过滤符合当前传入ruleTypes的mailEntity
        List<EwsMailEntity> ewsMailList = itemActionTypeList.stream()
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
            List<EwsRuleEntity> ewsRuleEntityList = ewsRuleService.filterRuleByConfigEnum(topicConfig, itemActionTypeList);
            List<Long> ruleIdList = ewsRuleEntityList.stream()
                    .map(rule -> {
                        return rule.getRuleId();
                    }).collect(Collectors.toList());
            log.info("email 获取mailConfig:");
            mail.setMailRulesValidThisTime(ewsRuleEntityList);
            log.info("query ruleIdList:{}",ruleIdList);
            if(ruleIdList.size()==0){
                throw new RuntimeException("获取到ruleIdList为空,请检查重试.");
            }
//            EwsFoldersEntity foldersEntity = ewsFolderService.findInRuleRelation(ruleIdList, FolderNameEnum.ATTACH_ALREADY.getCode());
//            mail.setMailFolders(foldersEntity);
////            nameFolderMap.put(FolderNameEnum.ATTACH_ALREADY.getCode(),foldersEntity);
//            mail.setMailFoldersMap(nameFolderMap);
        }
        log.info("满足itemActionType条件的mail:"+ewsMailList);
        return ewsMailList;
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
    public Integer delOne(Long mailId) {
        return baseMapper.deleteById(mailId);
    }

}
