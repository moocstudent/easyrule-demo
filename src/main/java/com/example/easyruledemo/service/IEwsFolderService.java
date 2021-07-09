package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.property.complex.FolderId;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 15:04
 */
public interface IEwsFolderService {

    //动态获取list
    List<EwsFoldersEntity> listSelective(EwsFoldersEntity ewsFolders);

    //testok 创建新的文件夹, 顶级目录下
    String createFolder(String folderName, EwsMailEntity mailConfig);

    //testok 创建文件夹,如果parentFolder为null,则顶级目录下创建,否则建立在parentFolder下
    //可为接口 也可以为启动创建
    String createFolder(String folderName, WellKnownFolderName parentFolder, EwsMailEntity mailConfig);

    //创建文件夹多个
    List<String> createFolder(List<String> folderNames, WellKnownFolderName parentFolder, EwsMailEntity mailConfig);

    //fixme don't understood
    Integer bindFolderTest(String folderId);

    //根据主键删除
    Integer delByPriKey(String ewsFolderId);

    //todo get folderId by email
    //todo 先通过topicId查ruleId,ruleId查folderId 暂不使用这个
    List<FolderId> getWatchingFolderList(List<String> emailList);

    //implementsteam@outlook的测试folderId unionId
    List<FolderId> getWatchingFolderListForTest();

    //str folderId -> FolderId
    List<FolderId> getWatchingFolderByIds(List<String> folderIdsStr);

    //curd
    Integer saveFolder(EwsFoldersEntity ewsFoldersEntity);

    //保存或更新folder
    Boolean saveOrUpdateFolder(EwsFoldersEntity ewsFoldersEntity);

    //保存或更新folder根据folderId(unionId)
    @Deprecated
    Boolean saveOrUpdateByFUnionId(EwsFoldersEntity ewsFoldersEntity);

    EwsFoldersEntity getByEmail(String email);

    //根据主键获取文件夹实体
    EwsFoldersEntity getByPriKey(String ewsFolderId);

    //查folderId集合根据规则id
//    List<FolderId> listFolderIdByRuleId(String ruleId);
    List<FolderId> listFolderIdByRuleId(Long ruleId);

    //获取该规则下未执行action的folderId实体
    List<FolderId> listFolderIdByRuleIdJustUnAction(Long ruleId);

    //获取folder名称集合根据规则id
//    List<String> listFolderNamesByRuleId(String ruleId);
    @Deprecated
    List<String> listFolderNamesByRuleId(Long ruleId);

    //查folder实体根据ruleId
//    List<EwsFoldersEntity> listFolderByRuleId(String ruleId);
    List<EwsFoldersEntity> listFolderByRuleId(Long ruleId);

    //生成folder根据主题id
    List<FolderId> listFolderIdByTopicId(String topicId);

    //生成folder根据主题,获取未执行操作的文件夹的folderId实体list用于newEmail事件监听
    List<FolderId> listFolderIdByTopicIdUnAction(String topicId);

    //根据folderCode查找一个文件夹
   List<EwsFoldersEntity> findByFolderCode(String folderCode);

    //根据给定规则idlist,以及对应folderCode,查一个文件夹
    EwsFoldersEntity findInRuleRelation(List<?> ruleIdList,String folderCode);




}
