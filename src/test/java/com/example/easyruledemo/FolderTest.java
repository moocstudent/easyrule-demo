package com.example.easyruledemo;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.service.IEwsFolderService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 9:39
 */
class FolderTest extends BaseTest{

    @Autowired
    private IEwsFolderService ewsFolderService;

    @Test
    public void testBind(){
        Integer integer = ewsFolderService
                .bindFolderTest("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq6wAAAA==");
        if(integer>0){
            System.out.println("bind ok");
        }
    }

    /**
     * testok
     * 测试创建重名了会异常
     */
    @Test
    public void testCreateFolder() {
        String folderId = ewsFolderService.createFolder("待下载附件邮件", WellKnownFolderName.Inbox, EwsMailEntity.builder().email("frankimplements@outlook.com").password("zhangqi1112").build());
        String folderId2 = ewsFolderService.createFolder("已下载附件邮件", WellKnownFolderName.Inbox,EwsMailEntity.builder().email("frankimplements@outlook.com").password("zhangqi1112").build());
        System.out.println("folderId:"+folderId);
        System.out.println("folderId2:"+folderId2);
    }

}
