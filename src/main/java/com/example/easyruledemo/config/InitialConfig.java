package com.example.easyruledemo.config;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.relation.EwsMailFolderRelation;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.service.*;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 17:45
 */
@Component
@Slf4j
public class InitialConfig {

    @Autowired
    private IEwsEmailService ewsEmailService;
    @Autowired
    private IEwsRuleService ewsRuleService;
    @Autowired
    private IEwsFolderService ewsFolderService;
    @Autowired
    private IEwsTopicService ewsTopicService;
    @Autowired
    private IMailFolderRelationService ruleFolderRelationService;

    /**
     * 初始化邮件文件夹,全部并根据itemActionType.D
     * 也可以根据传入邮件来进行单条生成
     * 这里是默认获取全部的进行生成
     * todo 做成接口单独调用产生文件夹,根据email主体
     * testok
     */
//    @PostConstruct
    public void initMailFoldersAndFireRules() {
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
                    Integer createFolderSizeByTopicId = rules.stream()
                            .map(rule -> {
                                List<EwsMailFolderRelation> ruleFolderRelations = ruleFolderRelationService.listByRuleId(rule.getRuleId());
                                return ruleFolderRelations;
                            })
                            .map(ruleFolderRelation -> {
                                //根据ruleId查回来文件夹list,创建文件夹,返回folderId unionId,放入关联表
                                Integer ruleNeedsFoldersSize = this.createRuleNeedsFolders(ruleFolderRelation, mail);
                                //初始化规则
                                initialRules(mail);
                                return ruleNeedsFoldersSize;
                            })
                            .reduce((c1, c2) -> {
                                c1 += c2;
                                return c1;
                            })
                            .get();
                    return createFolderSizeByTopicId;
                })
                .reduce((c1, c2) -> {
                    c1 += c2;
                    return c1;
                })
                .get();

        log.info("创建该邮件list下初始化的文件夹共:{}个,根据类型:{},该初始化只执行一次", createFolderSizeByMailList, ItemActionType.D.getDescription());
    }


    /**
     * 生成folderId后继而保存回对应的folder实体中的folderId unionId中
     *
     * @param ewsFolders
     * @param mail
     * @return
     */
//    @Transactional
//    protected Boolean createRuleNeedsFolders(EwsFoldersEntity ewsFolders, EwsMailEntity mail) {
//        String folderUnionId = ewsFolderService.createFolder(ewsFolders.getFolderName(), WellKnownFolderName.Inbox, mail);
//        log.info("folderUnionId:{}", folderUnionId);
//        //保存folderUnionId到数据库表ews_mail_folders
//        ewsFolders.setFolderId(folderUnionId);
//        return ewsFolderService.saveOrUpdateFolder(ewsFolders);
//    }


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
        for (int i = 0; i < relation.size(); i++) {
            try {
                Boolean foldersSingleCreate = this.createRuleNeedsFoldersSingle(relation.get(i), mailConfig);
                if (foldersSingleCreate) {
                    createSize++;
                }
            } catch (Exception e) {
                //判定如果是已经存在文件夹的异常
                if (e.getMessage().indexOf("A folder with the specified name already exists.") > -1) {
                    log.error("该邮箱:{},该文件夹:{}已经创建.", mailConfig.getEmail(), relation.get(i).getFolderCode());
                    continue;
                }
                e.printStackTrace();
            }
        }
        return createSize;
    }

    private Boolean createRuleNeedsFoldersSingle(EwsMailFolderRelation relation, EwsMailEntity mailConfig) {
        log.info("新生成文件夹:{}",relation.getFolderName());
        String folderUnionId = ewsFolderService.createFolder(relation.getFolderName(), WellKnownFolderName.Inbox, mailConfig);
        log.info("folderUnionId 新生成:{}", folderUnionId);
        if (folderUnionId != null) {
            relation.setFolderId(folderUnionId);
            //fixme 这里在设定主题的时候其实已经设定了mailId, 在保存主题->规则->文件夹 (规则文件夹关联关系时可以传入当前)
            relation.setMailId(mailConfig.getMailId());
            //设定规则中的moveToFolder actions进行更改
//            ewsRuleService.

            return ruleFolderRelationService.saveOrUpdateRelation(relation);
        } else {
            log.warn("创建folder已经存在");
            return false;
        }
    }

    /**
     * 初始化规则
     */
//    @PostConstruct
    @Deprecated
    public void initialRules() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<EwsMailEntity> mailConfigList
                = ewsEmailService.getMailConfigList(EwsMailEntity.builder().build(), ItemActionType.D);
        log.info("邮箱规则初始化:{}", mailConfigList);

        List<Integer> fireRuleCodeList = mailConfigList.stream()
                .filter(mail -> mail.getTopicId() != null)
                .map(mail -> {
                    /**
                     * 一则主题对应多个规则,规则中创建的接收文件夹,可通过list放入pullSubscription来监听
                     */
                    List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mail.getTopicId());
                    log.info("rules in initial:{}", rules);
                    return rules.stream()
                            .map(rull -> {
                                //TOdo 这里可以或调用 fireJustThisOne
                                log.info("rull in init:{}",rull);
                                Integer fireCode = ewsRuleService.ewsRuleFire(rull, mail);
                                if (fireCode > 0) {
                                    log.info("This rule is fire !");
                                    return fireCode;
                                } else {
                                    throw new RuntimeException("no rule to fire");
                                }
                            })
                            .collect(Collectors.toList());
                })
                .reduce((fireOk1, fireOk2) -> {
                    fireOk1.addAll(fireOk2);
                    return fireOk1;
                })
                .get();
        int fireRuleCode = fireRuleCodeList.size();
        log.info("执行规则条数:{}", fireRuleCode);
    }

    /**
     * 初始化规则
     * @param mailConfigList
     */
    public void initialRules(List<EwsMailEntity> mailConfigList) {
        log.info("邮箱规则初始化:{}", mailConfigList);

        List<Integer> fireRuleCodeList = mailConfigList.stream()
                .filter(mail -> mail.getTopicId() != null)
                .map(mail -> {
                    /**
                     * 一则主题对应多个规则,规则中创建的接收文件夹,可通过list放入pullSubscription来监听
                     */
                    List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mail.getTopicId());
                    log.info("rules in initial:{}", rules);
                    return rules.stream()
                            .map(rull -> {
                                //TOdo 这里可以或调用 fireJustThisOne
                                Integer fireCode = ewsRuleService.ewsRuleFire(rull, mail);
                                if (fireCode > 0) {
                                    log.info("This rule is fire !");
                                    return fireCode;
                                } else {
                                    throw new RuntimeException("no rule to fire");
                                }
                            })
                            .collect(Collectors.toList());
                })
                .reduce((fireOk1, fireOk2) -> {
                    fireOk1.addAll(fireOk2);
                    return fireOk1;
                })
                .get();
        int fireRuleCode = fireRuleCodeList.size();
        log.info("执行规则条数:{}", fireRuleCode);
    }


    /**
     * 初始化规则
     * @param mailConfig
     */
    public void initialRules(EwsMailEntity mailConfig) {
        log.info("邮箱规则初始化:{}", mailConfig);

        List<Integer> fireRuleCodeList = Stream.of(mailConfig)
                .filter(mail -> mail.getTopicId() != null)
                .map(mail -> {
                    /**
                     * 一则主题对应多个规则,规则中创建的接收文件夹,可通过list放入pullSubscription来监听
                     */
                    List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mail.getTopicId());
                    log.info("rules in initial:{}", rules);
                    return rules.stream()
                            .map(rull -> {
                                //TOdo 这里可以或调用 fireJustThisOne 可做为接口接受传入值调用 ruleFire 还是 fireJustThisOne
                                //todo 同上,但需要配置是否本次两组规则都保留
                                Integer fireCode = ewsRuleService.ewsRuleFire(rull, mail);
                                if (fireCode > 0) {
                                    log.info("This rule is fire !");
                                    return fireCode;
                                } else {
                                    throw new RuntimeException("no rule to fire");
                                }
                            })
                            .collect(Collectors.toList());
                })
                .reduce((fireOk1, fireOk2) -> {
                    fireOk1.addAll(fireOk2);
                    return fireOk1;
                })
                .get();
        int fireRuleCode = fireRuleCodeList.size();
        log.info("执行规则条数:{}", fireRuleCode);
    }

//
//    /**
//     * 如果有重复的文件夹，则过滤
//     *
//     * @param ewsFolders
//     * @param mail
//     * @return 创建初始化文件夹成功数量
//     */
////    protected Integer createRuleNeedsFolders(List<EwsFoldersEntity> ewsFolders, EwsMailEntity mail) {
////        Set<String> folderNameDistinct = new HashSet<>();
////        List<Boolean> addBool = ewsFolders.stream()
////                .filter(folders -> folders.getFolderName() != null && folders != null && folders.getFolderName().length() > 0)
////                .map(folder -> folderNameDistinct.add(folder.getFolderName()))
////                .collect(Collectors.toList());
////        long addOkCount = addBool.stream()
////                .filter(bool -> bool == true)
////                .count();
////        log.info("created rule needs folders at EWS:{}",addOkCount);
//////        return Integer.parseInt(addOkCount+"");
////    }

}
