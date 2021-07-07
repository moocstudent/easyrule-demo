package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IEwsTopicService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:35
 */
@Api("ews收件主题接口层")
@RequestMapping("/ews/topic")
@RestController
public class EwsTopicController {

    @Autowired
    private IEwsTopicService ewsTopicService;

    @ApiOperation("添加主题")
    @PostMapping
    public Result addTopic(@RequestBody EwsTopicEntity ewsTopic){
        return ResultUtil.success(ewsTopicService.saveOrUpdateTopic(ewsTopic));
    }

    @ApiOperation("更新主题")
    @PutMapping
    public Result updateTopic(@RequestBody EwsTopicEntity ewsTopic){
        return ResultUtil.success(ewsTopicService.saveOrUpdateTopic(ewsTopic));
    }

    @ApiOperation("遍历规则,根据主题名称/主题简介/主题配置串模糊匹配")
    @PostMapping("/list")
    public Result topicList(@RequestBody EwsTopicEntity ewsTopic){
        return ResultUtil.success(ewsTopicService.listSelective(ewsTopic));
    }

    @ApiOperation("根据主键查询一个")
    @GetMapping("/{topicId}")
    public Result oneTopic(@PathVariable("topicId") String topicId){
        return ResultUtil.success(ewsTopicService.findOne(topicId));
    }

    @ApiOperation("根据主键删除一个")
    @DeleteMapping("/{topicId}")
    public Result delTopic(@PathVariable("topicId") String topicId){
        return ResultUtil.success(ewsTopicService.delOne(topicId));
    }

    @ApiOperation("根据邮件主键获取它的主题")
    @GetMapping("/findTopic/{mailId}")
    public void topicByMailId(@PathVariable("mailId") String mailId){
        EwsTopicEntity topicByMailId = ewsTopicService.getTopicByMailId(mailId);
        System.out.println("find topic by mailId:"+topicByMailId);
    }
}
