package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsRuleEntity;
import microsoft.exchange.webservices.data.property.complex.Rule;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 13:50
 */
public interface IEwsRuleService {

        //testok 只设定这个规则奏效,其余的进行取消奏效
        Integer ewsRuleFireJustThisOne(EwsRuleEntity ewsRuleEntity,String emailAddr);

        //TODO 执行规则,需要传入参数,这里需要传入数据库传过来的参数,并传入到ews的rule参数中
        Integer ewsRuleFire(EwsRuleEntity ewsRuleEntity);

        //testok 获取某个邮箱的规则名称集合
        List<EwsRuleEntity> getEwsRulesByEmAddr(String emailAddr);

        //testok 删除某个邮箱的所有规则,返回删除数量(全部)
        Integer deleteRuleByEmAddr(String emailAddr);

        //testok 让邮件中的规则disabled不再奏效
        Integer disabledRuleByEmAddr(String emailAddr);

        //将数据库中的ruleEntity转换为可用的ewsRule
        Rule transformRuleEntity(EwsRuleEntity ewsRuleEntity);

        //根据主题id获取旗下rule list
        List<EwsRuleEntity> getRulesByTopicId(String topicId);

}
