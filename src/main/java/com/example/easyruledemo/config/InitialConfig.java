package com.example.easyruledemo.config;

import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.enums.ItemActionType;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.service.IEwsTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 初始化邮件文件夹,全部
     * 也可以根据传入邮件来进行单条生成
     * 这里是默认获取全部的进行生成
     * todo 做成接口单独调用产生文件夹
     */
//    @PostConstruct
    public void initMailFolders() {
        //the all mailConfig valid ItemActionTypes
        List<EwsMailEntity> mailConfigList
                = ewsEmailService.getMailConfigList(EwsMailEntity.builder().build(), ItemActionType.D, ItemActionType.DC);
//        mailConfigList.stream()
//                .filter(mail -> mail.getTopicId() != null && mail.getTopicId().length() > 0)
//                .map(mail -> {
//                    ewsRuleService.
//                })
        //
        List<Integer> createSizeList = mailConfigList.stream()
                .filter(mail -> mail.getTopicId() != null && mail.getTopicId().length() > 0)
                .map(mail -> {
                    /**
                     * 一则主题对应多个规则,规则中创建的接收文件夹,可通过list放入pullSubscription来监听
                     */
                    List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(mail.getTopicId());
                    List<EwsFoldersEntity> ewsFoldersEntities = rules.stream()
                            .map(rule -> {
                                List<EwsFoldersEntity> foldersEntities = ewsFolderService.listFolderByRuleId(rule.getRuleId());
                                return foldersEntities;
                            })
                            .reduce((x, y) -> {
                                x.addAll(y);
                                return x;
                            })
                            .filter(collectList -> collectList != null && collectList.size() != 0)
                            .get();
                    //创建关于该topic下该规则的对应的初始文件夹 返回folderId保存
                    List<Integer> createFolderSize = ewsFoldersEntities.stream()
                            .map(folder -> {
                                Boolean ruleNeedsFolders = null;
                                try {
                                    ruleNeedsFolders = this.createRuleNeedsFolders(folder, mail);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                                log.info("create folder :{} ,the folderId is :{}", ruleNeedsFolders, folder.getFolderId());
                                return 1;
                            })
                            .collect(Collectors.toList());

                    return createFolderSize;
                })
                .reduce((s1, s2) -> {
                    s1.addAll(s2);
                    return s1;
                })
                .orElse(new ArrayList<>());

        long createOkCount = createSizeList.stream()
                .filter(siz -> siz == 1)
                .count();
        log.info("创建该邮件list下初始化的文件夹共:{}个,该初始化只执行一次",createOkCount);
    }

    /**
     * 生成folderId后继而保存回对应的folder实体中的folderId unionId中
     *
     * @param ewsFolders
     * @param mail
     * @return
     */
    @Transactional
    protected Boolean createRuleNeedsFolders(EwsFoldersEntity ewsFolders, EwsMailEntity mail) {
        String folderUnionId = ewsFolderService.createFolder(ewsFolders.getFolderName(), mail);
        log.info("folderUnionId:{}", folderUnionId);
        //保存folderUnionId到数据库表ews_mail_folders
        ewsFolders.setFolderId(folderUnionId);
        return ewsFolderService.saveOrUpdateFolder(ewsFolders);
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
