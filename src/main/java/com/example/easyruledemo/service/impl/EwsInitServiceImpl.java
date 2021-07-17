package com.example.easyruledemo.service.impl;

import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.container.SubscriptionContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.relation.EwsMailFolderRelation;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.rules.MailActionsThread;
import com.example.easyruledemo.rules.MailEventsThread;
import com.example.easyruledemo.service.*;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.notification.GetEventsResults;
import microsoft.exchange.webservices.data.notification.ItemEvent;
import microsoft.exchange.webservices.data.notification.PullSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: zhangQi
 * @Date: 2021-07-13 14:58
 */
@Service
@Slf4j
public class EwsInitServiceImpl
        implements IEwsInitService {

    @Autowired
    private IEwsEmailService ewsEmailService;
    @Autowired
    private IEwsRuleService ewsRuleService;
    @Autowired
    private IMailFolderRelationService mailFolderRelationService;
    @Autowired
    private IEwsFolderService ewsFolderService;
//
//    @Transactional
//    @Override
//    public Integer initMailFoldersAndFireRules(List<String> itemActionTypeList) {
////        ItemActionType.
//        //the all mailConfig valid ItemActionTypes
//        List<EwsMailEntity> mailConfigList
//                = ewsEmailService.getMailConfigList(EwsMailEntity.builder().build(), ItemActionType.getEnumList(itemActionTypeList));
//        log.info("邮箱文件夹初始化:{}", mailConfigList);
//        Integer createFolderSizeByMailList = mailConfigList.stream()
//                .filter(mail -> mail.getTopicId() != null )
//                .map(mail -> {
//                    /**
//                     * 一则主题对应多个规则,规则中创建的接收文件夹,可通过list放入pullSubscription来监听
//                     */
//                    List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mail.getTopicId());
//                    Integer createFolderSizeByTopicId = rules.stream()
//                            .map(rule -> {
//                                List<EwsMailFolderRelation> ruleFolderRelations = mail.listByRuleId(rule.getRuleId());
//                                return ruleFolderRelations;
//                            })
//                            .map(ruleFolderRelation -> {
//                                //根据ruleId查回来文件夹list,创建文件夹,返回folderId unionId,放入关联表
//                                Integer ruleNeedsFoldersSize = this.createRuleNeedsFolders(ruleFolderRelation, mail);
//                                log.info("创建了规则需要的文件夹共{}个.",ruleNeedsFoldersSize);
//                                //初始化规则
////                                initialRules(mail);
//                                return ruleNeedsFoldersSize;
//                            })
//                            .reduce((c1, c2) -> {
//                                c1 += c2;
//                                return c1;
//                            })
//                            .get();
//                    return createFolderSizeByTopicId;
//                })
//                .reduce((c1, c2) -> {
//                    c1 += c2;
//                    return c1;
//                })
//                .get();
//
//        log.info("创建该邮件list下初始化的文件夹共:{}个,根据类型:{},该初始化只执行一次", createFolderSizeByMailList, ItemActionType.D.getDescription());
//        return createFolderSizeByMailList;
//    }


    @Override
    public Integer initMailFoldersAndFireRules(Integer resetRuleCode) {
        //the all mailConfig valid ItemActionTypes
        List<EwsMailEntity> mailConfigList
                = ewsEmailService.getMailConfigList(EwsMailEntity.builder().build());
        log.info("邮箱文件夹初始化:{}", mailConfigList);
        Integer createFolderSizeByMailList = mailConfigList.stream()
                .filter(mail -> mail.getTopicId() != null)
                .map(mail -> {
                    /**
                     * 一则主题对应多个规则,规则中创建的接收文件夹,可通过list放入pullSubscription来监听
                     */
                    List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mail.getTopicId());

                    List<EwsMailFolderRelation> folderRelation = mailFolderRelationService.findByMailId(mail.getMailId());
                    Integer ruleNeedsFoldersSize = this.createRuleNeedsFolders(folderRelation, mail);
                    return ruleNeedsFoldersSize;
                })
                .reduce((c1, c2) -> {
                    c1 += c2;
                    return c1;
                })
                .get();

        log.info("创建该邮件list下初始化的文件夹共:{}个,根据类型:{},该初始化只执行一次", createFolderSizeByMailList, ItemActionType.D.getDescription());
        return createFolderSizeByMailList;
    }

    @Override
    public Integer initMailFoldersAndFireRules(Long mailId, Integer resetRuleCode) {

        EwsMailEntity mailEntity = ewsEmailService.findOne(mailId);
        log.info("邮箱文件夹初始化:{}", mailEntity);

        List<EwsMailFolderRelation> ruleFolderRelations = mailFolderRelationService.findByMailId(mailEntity.getMailId());
        List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mailEntity.getTopicId());
        //根据ruleId查回来文件夹list,创建文件夹,返回folderId unionId,放入关联表
        Integer ruleNeedsFoldersSize = this.createRuleNeedsFolders(ruleFolderRelations, mailEntity);
        log.info("创建了规则需要的文件夹共{}个.", ruleNeedsFoldersSize);
        log.info("邮箱规则初始化:{}", mailEntity);
        if (resetRuleCode == 1) {
            Integer disabledRuleByEmAddr = ewsRuleService.disabledRuleByEmAddr(mailEntity);
        } else if (resetRuleCode == 2) {
            Integer deleteRuleByEmAddr = ewsRuleService.deleteRuleByEmAddr(mailEntity);
        }
        Integer initialRules = initialRules(mailEntity, rules);

        return initialRules;
    }

    @Override
    public Integer initMailFolder(Long mailId) {
        EwsMailEntity mailEntity = ewsEmailService.findOne(mailId);
        log.info("邮箱文件夹初始化:{}", mailEntity);

        List<EwsMailFolderRelation> ruleFolderRelations = mailFolderRelationService.findByMailId(mailEntity.getMailId());
        //根据ruleId查回来文件夹list,创建文件夹,返回folderId unionId,放入关联表
        Integer ruleNeedsFoldersSize = this.createRuleNeedsFolders(ruleFolderRelations, mailEntity);
        log.info("创建了规则需要的文件夹共{}个.", ruleNeedsFoldersSize);
        return ruleNeedsFoldersSize;
    }

    @Override
    public Integer initMailRules(Long mailId, Integer resetRuleCode) {
        EwsMailEntity mailEntity = ewsEmailService.findOne(mailId);
        log.info("邮箱文件夹初始化:{}", mailEntity);

        List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mailEntity.getTopicId());
        //根据ruleId查回来文件夹list,创建文件夹,返回folderId unionId,放入关联表
        log.info("邮箱规则初始化:{}", mailEntity);
        if (resetRuleCode == 1) {
            Integer disabledRuleByEmAddr = ewsRuleService.disabledRuleByEmAddr(mailEntity);
        } else if (resetRuleCode == 2) {
            Integer deleteRuleByEmAddr = ewsRuleService.deleteRuleByEmAddr(mailEntity);
        }
        Integer initialRules = initialRules(mailEntity, rules);

        return initialRules;
    }


    @Override
    public Integer initMailFoldersAndFireRules(List<String> itemActionTypeList) {
        return null;
    }


    @Override
    public Integer initSubscriptionAndEventPoll(Long mailId) {
        EwsMailEntity mailConfig = ewsEmailService.findOne(mailId);
        PullSubscription pullSubscription = SubscriptionContainer.getSubscriptionToday(mailConfig);
        //todo thread获取mailentity和subscription一致的
        log.info("subscriptionId in events:{}", pullSubscription.getId());
        try {
            GetEventsResults events = pullSubscription.getEvents();
            System.out.println("----");
            // Loop through all item-related events.
            for (ItemEvent itemEvent : events.getItemEvents()) {
                if (itemEvent.getEventType() == EventType.NewMail) {
                    log.info("新的邮件已到达 itemId:{}" + itemEvent.getItemId());
                    EmailMessage message = EmailMessage.bind(EwsExContainer.getExchangeService(
                            mailConfig.getEmail(), mailConfig.getPassword()),
                            itemEvent.getItemId()
                    );
                    MailActionsThread actionsThread = new MailActionsThread(message, mailConfig);
                    actionsThread.start();
                    //                    if (message.getHasAttachments()) {
                    //                        AttachmentCollection attachments = message.getAttachments();
                    //                        System.out.println("attachments:" + attachments);
                    //                        log.info("进行附件下载");
                    //
                    //                        attachments.save();
                    //
                    //                        log.info("下载完成后将邮件移入" + FolderNameEnum.ATTACH_ALREADY.getUsename());
                    //                        //下载完成后将文件移动入已下载附件文件夹
                    //                        message.move(new FolderId(
                    //                                mailConfig.getMailFoldersMap()
                    //                                        .get(FolderNameEnum.ATTACH_ALREADY.getCode()).getFolderId())
                    //                        );
                    //                    }

                } else if (itemEvent.getEventType() == EventType.Created) {
                    Item item = Item.bind(EwsExContainer.getExchangeService(
                            mailConfig.getEmail(), mailConfig.getPassword()),
                            itemEvent.getItemId());
                } else if (itemEvent.getEventType() == EventType.Deleted) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;

//    MailEventsThread mailEventsThread = new MailEventsThread(mailEntity,subscriptionToday);
//        mailEventsThread.start();
//        return 1;
    }


    @Override
    public Integer initEmailAttachEventPoll(List<String> itemActionTypeList) {
        if (!(SubscriptionContainer.getInitialCount() > 0)) {
            log.info("如果subscription监听未初始化,则不进行事件处理");
            return 0;
        }
        int mailEventCount = 0;
        try {
            List<EwsMailEntity> mailConfigList = ewsEmailService
                    .getMailConfigList(EwsMailEntity.builder().build(),
                            /*邮件监听类型为下载以及下载并拷贝*/ItemActionType.getEnumList(itemActionTypeList));
            for (EwsMailEntity mailConfig : mailConfigList) {
//                MailEventsThread mailEventsThread = new MailEventsThread(mailConfig);
//                mailEventsThread.start();
//                mailEventCount++;
            }
//            executeThisDay++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mailEventCount;
    }


    /**
     * 创建规则需要的文件夹
     *
     * @param relation
     * @param mailConfig
     * @return
     */
    @Transactional
    protected Integer createRuleNeedsFolders(List<EwsMailFolderRelation> relation, EwsMailEntity mailConfig) {
        int createSize = 0;
        //TODO 现在改为了一个邮箱对应一个创建文件夹
        try {
            Boolean foldersSingleCreate = this.createRuleNeedsFoldersSingle(relation.get(0), mailConfig);
            if (foldersSingleCreate) {
                createSize++;
            }
        } catch (Exception e) {
            //判定如果是已经存在文件夹的异常
            if (e.getMessage().indexOf("A folder with the specified name already exists.") > -1) {
                log.error("该邮箱:{},该文件夹:{}已经创建.", mailConfig.getEmail(), relation.get(0).getFolderCode());
            }
            e.printStackTrace();
        }
        return createSize;
    }

    private Boolean createRuleNeedsFoldersSingle(EwsMailFolderRelation relation, EwsMailEntity mailConfig) {
        log.info("邮箱:{}新生成文件夹:{}", mailConfig.getEmail(), relation.getFolderName());
        String folderUnionId = ewsFolderService.createFolder(relation.getFolderName(), WellKnownFolderName.Inbox, mailConfig);
        log.info("folderUnionId 新生成:{}", folderUnionId);
        if (folderUnionId != null) {
            relation.setFolderId(folderUnionId);
            //fixme 这里在设定主题的时候其实已经设定了mailId, 在保存主题->规则->文件夹 (规则文件夹关联关系时可以传入当前)
            relation.setMailId(mailConfig.getMailId());
            //设定规则中的moveToFolder actions进行更改
//            ewsRuleService.

            return mailFolderRelationService.saveOrUpdateRelation(relation);
        } else {
            log.warn("创建folder已经存在");
            return false;
        }
    }

    /**
     * 初始化规则
     *
     * @param mailConfig
     */
    public Integer initialRules(EwsMailEntity mailConfig, List<EwsRuleEntity> rules) {

        log.info("本次执行了email:{}的规则.", mailConfig.getEmail());
        log.info("rules in initial:{}", rules);
        Integer ewsRuleFire = ewsRuleService.ewsRuleFire(rules, mailConfig);
        log.info("邮箱:{}执行规则条数:{}", mailConfig.getEmail(), ewsRuleFire);
        return ewsRuleFire;
//        for (EwsRuleEntity rule : rules) {
//            ewsRuleService.ewsRuleFire(rule,mailConfig);
//        }
//        return 1;
    }

}
