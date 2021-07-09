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

    //根据给定的folderCode查询folder实体 , 是已经进行过规则类型判定后才可调用
    @Deprecated
    List<EwsFoldersEntity> findFolderByFolderCode(String folderCode);

    //根据给定的ids和folderCode,查找唯一的文件夹实体
    EwsFoldersEntity findFolderByFolderCodeAndRuleIds(List<?> ruleIds,String folderCode);

    //获取规则下未执行的folderId集合,这里是未下载附件 ,未...等文件夹
//    List<EwsFoldersEntity> listFolderByRuleIdUnAction(Long ruleId);
}
