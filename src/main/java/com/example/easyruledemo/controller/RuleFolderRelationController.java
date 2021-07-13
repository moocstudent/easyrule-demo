package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.entity.relation.EwsRuleFolderRelation;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IRuleFolderRelationService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 15:18
 */
@Api("ews规则文件夹关联接口")
@RequestMapping("/ews/ruleFolder")
@RestController
public class RuleFolderRelationController {

    @Autowired
    private IRuleFolderRelationService ruleFolderRelationService;

    @ApiOperation("添加规则文件夹关联")
    @PostMapping
    public Result addRelation(@RequestBody EwsRuleFolderRelation relation){
        return ResultUtil.success(ruleFolderRelationService.saveOrUpdateRelation(relation));
    }

    @ApiOperation("更新规则文件夹关联")
    @PutMapping
    public Result updateRelation(@RequestBody EwsRuleFolderRelation relation){
        return ResultUtil.success(ruleFolderRelationService.saveOrUpdateRelation(relation));
    }

    @ApiOperation("遍历关联")
    @PostMapping("/list")
    public Result relationList(@RequestBody EwsRuleFolderRelation relation){
        return ResultUtil.success(ruleFolderRelationService.listSelective(relation));
    }

    @ApiOperation("根据主键查询一个")
    @GetMapping("/{relationId}")
    public Result oneRelation(@PathVariable("relationId") Long relationId){
        return ResultUtil.success(ruleFolderRelationService.findOne(relationId));
    }

    @ApiOperation("根据主键删除一个")
    @DeleteMapping("/{relationId}")
    public Result delRelation(@PathVariable("relationId") Long relationId){
        return ResultUtil.success(ruleFolderRelationService.delRelation(relationId));
    }


}
