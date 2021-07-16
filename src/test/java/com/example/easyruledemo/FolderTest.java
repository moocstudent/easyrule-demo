package com.example.easyruledemo;

import com.example.easyruledemo.container.EwsExContainer;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.service.IEwsRuleService;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.property.complex.Rule;
import microsoft.exchange.webservices.data.property.complex.RuleCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * @Author: zhangQi
 * @Date: 2021-07-02 9:39
 */
@Slf4j
class FolderTest extends BaseTest {

    @Autowired
    private IEwsFolderService ewsFolderService;
    @Autowired
    private IEwsRuleService ewsRuleService;

    @Test
    public void testBind() {
        Integer integer = ewsFolderService
                .bindFolderTest("AQMkADAwATM0MDAAMS0zNjFkLTY1MWEtMDACLTAwCgAuAAADgRcCAFohSUCq+fGuJ055HwEAmSTpLTMl3E+ND/s/c1xWVQAAAWrq6wAAAA==");
        if (integer > 0) {
            System.out.println("bind ok");
        }
    }

    @Test
    public void testFolder() {
        RuleCollection inboxRules = null;
        try {
            inboxRules = EwsExContainer.getExchangeService("implementsteam@163.com", "zhangqi1112")
                    .getInboxRules();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<Rule> iterator = inboxRules.iterator();
        while (iterator.hasNext()) {
            Rule next = iterator.next();
            String uniqueId = next.getActions().getMoveToFolder().getUniqueId();
            log.info("uniqueId:{}", uniqueId);
        }
    }

    @Test
    public void testFolder1() {
        Integer ruleByEmAddr = ewsRuleService.disabledRuleByEmAddr(EwsMailEntity.builder().email("implementsteam@outlook.com").password("zhangqi1112").build());

    }

    /**
     * testok
     * 测试创建重名了会异常
     */
    @Test
    public void testCreateFolder() {
        String folderId = ewsFolderService.createFolder("待下载附件邮件", WellKnownFolderName.Inbox, EwsMailEntity.builder().email("frankimplements@outlook.com").password("zhangqi1112").build());
        String folderId2 = ewsFolderService.createFolder("已下载附件邮件", WellKnownFolderName.Inbox, EwsMailEntity.builder().email("frankimplements@outlook.com").password("zhangqi1112").build());
        System.out.println("folderId:" + folderId);
        System.out.println("folderId2:" + folderId2);
    }

}
