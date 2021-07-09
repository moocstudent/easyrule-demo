package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.relation.EwsTopicRuleRelation;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.ITopicRuleRelationService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 15:18
 */
@Api("ews收件主题规则关联接口")
@RequestMapping("/ews/topicRule")
@RestController
public class TopicRuleRelationController {

    @Autowired
    private ITopicRuleRelationService topicRuleRelationService;

    @ApiOperation("添加主题规则关联")
    @PostMapping
    public Result addRelation(@RequestBody EwsTopicRuleRelation relation){
        return ResultUtil.success(topicRuleRelationService.saveOrUpdateRelation(relation));
    }

    @ApiOperation("更新主题规则关联")
    @PutMapping
    public Result updateRelation(@RequestBody EwsTopicRuleRelation relation){
        return ResultUtil.success(topicRuleRelationService.saveOrUpdateRelation(relation));
    }

    @ApiOperation("遍历关联")
    @PostMapping("/list")
    public Result relationList(@RequestBody EwsTopicRuleRelation relation){
        return ResultUtil.success(topicRuleRelationService.listSelective(relation));
    }

    @ApiOperation("根据主键查询一个")
    @GetMapping("/{relationId}")
    public Result oneRelation(@PathVariable("relationId") String relationId){
        return ResultUtil.success(topicRuleRelationService.findOne(relationId));
    }

    @ApiOperation("根据主键删除一个")
    @DeleteMapping("/{relationId}")
    public Result delRelation(@PathVariable("relationId") String relationId){
        return ResultUtil.success(topicRuleRelationService.delRelation(relationId));
    }

}
