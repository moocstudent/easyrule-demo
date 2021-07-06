package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.property.complex.FolderId;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 15:04
 */
public interface IEwsFolderService {

    //testok 创建新的文件夹, 顶级目录下
    String createFolder(String folderName, EwsMailEntity mailConfig);

    //testok 创建文件夹,如果parentFolder为null,则顶级目录下创建,否则建立在parentFolder下
    //可为接口 也可以为启动创建
    String createFolder(String folderName, WellKnownFolderName parentFolder, EwsMailEntity mailConfig);

    //创建文件夹多个
    List<String> createFolder(List<String> folderNames, WellKnownFolderName parentFolder, EwsMailEntity mailConfig);

    //fixme don't understood
    Integer bindFolderTest(String folderId);

    //todo get folderId by email
    //todo 先通过topicId查ruleId,ruleId查folderId 暂不使用这个
    List<FolderId> getWatchingFolderList(List<String> emailList);

    //
    List<FolderId> getWatchingFolderListForTest();

    //str folderId -> FolderId
    List<FolderId> getWatchingFolderByIds(List<String> folderIdsStr);

    //curd
    Integer saveFolder(EwsFoldersEntity ewsFoldersEntity);

    //保存或更新folder
    Boolean saveOrUpdateFolder(EwsFoldersEntity ewsFoldersEntity);

    EwsFoldersEntity getByEmail(String email);

    //查folderId集合根据规则id
//    List<FolderId> listFolderIdByRuleId(String ruleId);
    List<FolderId> listFolderIdByRuleId(Long ruleId);

    //获取folder名称集合根据规则id
//    List<String> listFolderNamesByRuleId(String ruleId);
    List<String> listFolderNamesByRuleId(Long ruleId);

    //查folder实体根据ruleId
//    List<EwsFoldersEntity> listFolderByRuleId(String ruleId);
    List<EwsFoldersEntity> listFolderByRuleId(Long ruleId);

    //生成folder根据主题id
    List<FolderId> listFolderIdByTopicId(String topicId);




}
