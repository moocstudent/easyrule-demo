package com.example.easyruledemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.easyruledemo.entity.EwsFoldersEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 16:37
 */
@Repository
public interface EwsFoldersMapper extends BaseMapper<EwsFoldersEntity> {

    //根据规则id查询需创建文件夹列表
//    List<EwsFoldersEntity> listFolderByRuleId(String ruleId);
    List<EwsFoldersEntity> listFolderByRuleId(Long ruleId);
}
