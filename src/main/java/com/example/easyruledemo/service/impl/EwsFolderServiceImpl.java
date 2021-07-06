package com.example.easyruledemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.easyruledemo.container.EwsContainer;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.mapper.EwsFoldersMapper;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.service.IEwsRuleService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 15:05
 */
@Slf4j
@Service
public class EwsFolderServiceImpl extends ServiceImpl<EwsFoldersMapper, EwsFoldersEntity>
        implements IEwsFolderService {
    @Autowired
    private IEwsRuleService ewsRuleService;
    @Autowired
    private IEwsFolderService ewsFolderService;

    @Override
    public String createFolder(String folderName, EwsMailEntity mailConfig) {
        return this.createFolder(folderName, null, mailConfig);
    }

    @Override
    public String createFolder(String folderName, WellKnownFolderName parentFolder, EwsMailEntity mailConfig) {
        try {
            Folder folder = new Folder(EwsContainer.getExchangeService(mailConfig.getEmail(), mailConfig.getPassword()));
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
    public List<String> createFolder(List<String> folderNames, WellKnownFolderName parentFolder, EwsMailEntity mailConfig) {
        List<String> folderIds = new ArrayList<>();
        for (String folderName : folderNames) {
            folderIds.add(this.createFolder(folderName, parentFolder, mailConfig));
        }
        return folderIds;
    }

    @Override
    public Integer bindFolderTest(String folderId) {
        // Bind to an existing folder using its unique identifier.
        try {
            System.out.println("alreadyHasFolderName:" + folderId);
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
                .map(em -> {
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

    @Override
    public List<FolderId> getWatchingFolderByIds(List<String> folderIdsStr) {
        List<FolderId> folders = new ArrayList<FolderId>();
        FolderId folderId = null;
        for (String str : folderIdsStr) {
            log.info("folderIdStr:{}", str);
            try {
                //TODO 这里的folderId需要从多个邮箱中拿过来(创建时存入数据库) 做为list
                folderId = new FolderId(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            folders.add(folderId);
        }
        return folders;
    }

    @Override
    public Integer saveFolder(EwsFoldersEntity ewsFoldersEntity) {
        return null;
    }

    @Override
    public Boolean saveOrUpdateFolder(EwsFoldersEntity ewsFoldersEntity) {
        return super.saveOrUpdate(ewsFoldersEntity);
    }

    @Override
    public EwsFoldersEntity getByEmail(String email) {
        //todo 联查email_config拿到id,获取mail_folders数据
        return null;
    }

    @Override
    public List<FolderId> listFolderIdByRuleId(Long ruleId) {
        return this.listFolderByRuleId(ruleId)
                .stream()
                .filter(folder -> folder.getFolderId() != null && folder.getFolderId().length() > 0)
                .map(folder -> {
                    FolderId folderId = null;
                    try {
                        folderId = new FolderId(folder.getFolderId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return folderId;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> listFolderNamesByRuleId(Long ruleId) {
        return this.listFolderByRuleId(ruleId)
                .stream()
                .filter(folder -> folder.getFolderName() != null && folder.getFolderName().length() > 0)
                .map(folder -> folder.getFolderName())
                .collect(Collectors.toList());
    }

    @Override
    public List<EwsFoldersEntity> listFolderByRuleId(Long ruleId) {
        return baseMapper.listFolderByRuleId(ruleId);
    }

    @Override
    public List<FolderId> listFolderIdByTopicId(String topicId) {
        List<EwsRuleEntity> rules = ewsRuleService.getRulesByTopicId(topicId);
        List<FolderId> folderIdList = rules.stream()
                .map(rule -> {
                    List<FolderId> folderIds = this.listFolderIdByRuleId(rule.getRuleId());
                    return folderIds;
                })
                .reduce((x, y) -> {
                    x.addAll(y);
                    return x;
                })
                .filter(collectList -> collectList != null && collectList.size() != 0)
                .orElseThrow(() -> new RuntimeException("需要先初始化文件夹"));

        return folderIdList;
    }

//    @Override
//    public List<FolderId> listFolderIdByRuleId(String ruleId) {
//        return this.listFolderByRuleId(ruleId)
//                .stream()
//                .filter(folder->folder.getFolderId()!=null && folder.getFolderId().length()>0)
//                .map(folder -> {
//                    FolderId folderId = null;
//                    try {
//                        folderId = new FolderId(folder.getFolderId());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return folderId;
//                })
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<String> listFolderNamesByRuleId(String ruleId) {
//        return this.listFolderByRuleId(ruleId)
//                .stream()
//                .filter(folder->folder.getFolderName()!=null && folder.getFolderName().length()>0)
//                .map(folder-> folder.getFolderName())
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<EwsFoldersEntity> listFolderByRuleId(String ruleId) {
//        return baseMapper.listFolderByRuleId(ruleId);
//    }
}
