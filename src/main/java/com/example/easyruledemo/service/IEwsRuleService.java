package com.example.easyruledemo.service;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.enums.RuleEnum;
import microsoft.exchange.webservices.data.property.complex.Rule;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-01 13:50
 */
public interface IEwsRuleService {

    //获取service
//    ExchangeService getEwsService(EwsMailEntity ewsMail);

    //testok 只设定这个规则奏效,其余的进行取消奏效
    Integer ewsRuleFireJustThisOne(EwsRuleEntity ewsRuleEntity, String emailAddr);

    //只设定此规则奏效,其余取消
    Integer ewsRuleFireJustThisOne(EwsRuleEntity ewsRuleEntity, EwsMailEntity ewsMail);

    //TODO 执行规则,需要传入参数,这里需要传入数据库传过来的参数,并传入到ews的rule参数中
    Integer ewsRuleFire(EwsRuleEntity ewsRuleEntity);

    //执行规则
    Integer ewsRuleFire(EwsRuleEntity ewsRuleEntity,EwsMailEntity ewsMail);

    //testok 获取某个邮箱的规则名称集合
    List<EwsRuleEntity> getEwsRulesByEmAddr(String emailAddr);

    //testok 删除某个邮箱的所有规则,返回删除数量(全部)
    @Deprecated
    Integer deleteRuleByEmAddr(String emailAddr);

    //
    Integer deleteRuleByEmAddr(EwsMailEntity ewsMail);

    //testok 让邮件中的规则disabled不再奏效
    //现在邮件已经跟topic关联,不再与单个rule关联
    Integer disabledRuleByEmAddr(String emailAddr);

    //disabled the rule by email and ruleId
    Integer disabledRuleByEmAddrAndRuleId(String emailAddr,String ruleId);

    //让邮件中的规则disabled不再奏效
    Integer disabledRuleByEmAddr(EwsMailEntity ewsMail);

    //将数据库中的ruleEntity转换为可用的ewsRule
    Rule transformRuleEntity(EwsRuleEntity ewsRuleEntity);

    //根据主题id获取旗下rule list
    List<EwsRuleEntity> getRulesByTopicId(String topicId);

    //保存一个规则
    Integer saveOne(EwsRuleEntity ewsRule);

    //保存或更新一个rule
    Boolean saveOrUpdateRule(EwsRuleEntity ewsRule);

    //动态获取list
    List<EwsRuleEntity> listSelective(EwsRuleEntity ewsRule);


    List<EwsRuleEntity> listRulesByTopicConfig(String topicConfig);

    //过滤规则根据配置串以及枚举
    List<EwsRuleEntity> filterRuleByConfigEnum(String config,RuleEnum... ruleTypes);
    List<EwsRuleEntity> filterRuleByConfigEnum(String config,List<RuleEnum> ruleEnums);

    //查找一个,根据规则id
    EwsRuleEntity findOne(String ruleId);

    //删除一个规则
    Integer delOne(String ruleId);
}
