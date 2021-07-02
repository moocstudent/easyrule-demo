package com.example.easyruledemo.service;

import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.property.complex.FolderId;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 15:04
 */
public interface IEwsFolderService {

    //testok 创建新的文件夹, 顶级目录下
    String createFolder(String folderName);

    //testok 创建文件夹,如果parentFolder为null,则顶级目录下创建,否则建立在parentFolder下
    String createFolder(String folderName, WellKnownFolderName parentFolder);

    //fixme don't understood
    Integer bindFolderTest(String folderId);

    //todo get folderId by email
    List<FolderId> getWatchingFolderList(List<String> emailList);

    //
    List<FolderId> getWatchingFolderListForTest();




}
