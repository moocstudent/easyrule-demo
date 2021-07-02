package com.example.easyruledemo.service.impl;

import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.service.IEwsFolderService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 15:05
 */
@Service
public class EwsFolderServiceImpl implements IEwsFolderService {

    @Override
    public String createFolder(String folderName) {
        return this.createFolder(folderName, null);
    }

    @Override
    public String createFolder(String folderName, WellKnownFolderName parentFolder) {
        try {
            Folder folder = new Folder(EwsContainer.defaultExchangeService());
            folder.setDisplayName(folderName);
            if (parentFolder == null) {
                folder.save(WellKnownFolderName.MsgFolderRoot);
            } else {
                folder.save(parentFolder);
            }
            FolderId folderId = folder.getId();

            return folderId.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer bindFolderTest(String folderId) {
        // Bind to an existing folder using its unique identifier.
        try {
            System.out.println("alreadyHasFolderName:"+folderId);
            Folder folder = Folder.bind(EwsContainer.defaultExchangeService(), FolderId.getFolderIdFromString(folderId));
            //testok
//            Folder folder = Folder.bind(EwsContainer.defaultExchangeService(), WellKnownFolderName.Inbox);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public List<FolderId> getWatchingFolderList(List<String> emailList) {
        return emailList.stream()
                .map(em->{
                    //todo 根据email拿到folderId from db
                    //service.getFolderIdByEmail(...);
                    FolderId folderId = null;
                    try {
                        folderId = new FolderId("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return folderId;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FolderId> getWatchingFolderListForTest() {
        List<FolderId> folders = new ArrayList<FolderId>();
        FolderId folderId = null;
        try {
            //TODO 这里的folderId需要从多个邮箱中拿过来(创建时存入数据库) 做为list
            folderId = new FolderId("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq7QAAAA==");
        } catch (Exception e) {
            e.printStackTrace();
        }
        folders.add(folderId);
        return folders;
    }
}
