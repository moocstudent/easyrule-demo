package com.example.easyruledemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsRuleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 16:39
 */
@Repository
public interface EwsRuleMapper extends BaseMapper<EwsRuleEntity> {

    //根据topicId查询ruleList
    List<EwsRuleEntity> listRuleByTopicId(String topicId);
    List<EwsRuleEntity> listRuleByTopicId(Long topicId);
}
