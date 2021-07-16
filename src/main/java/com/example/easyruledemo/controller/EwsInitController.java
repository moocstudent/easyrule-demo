package com.example.easyruledemo.controller;

import com.example.easyruledemo.bo.ItemActionTypeBo;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IEwsInitService;
import com.example.easyruledemo.service.ISubscriptionService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-09 13:56
 */
@Api("ews初始化接口层")
@RequestMapping("/ews/init")
@RestController
public class EwsInitController {

//    public Result init
    @Autowired
    private IEwsInitService ewsInitService;
    @Autowired
    private ISubscriptionService subscriptionService;

//    @ApiOperation("初始化所有邮箱文件夹以及规则,resetRuleCode传1或者2,1为轻量级初始化(之前的取消应用但存在,这次的新加入),2为重量级初始化(之前的删除这次的覆盖)")
//    @GetMapping("/mailFolderRules/{resetRuleCode}")
//    @Deprecated
//    public Result initMailFoldersAndFireRules(@PathVariable("resetRuleCode") Integer resetRuleCode) {
//        return ResultUtil.success(ewsInitService.initMailFoldersAndFireRules(resetRuleCode));
//    }

    //根据邮箱id初始化邮箱的对应文件夹和规则
    @ApiOperation("初始化文件夹以及规则根据邮件id,resetRuleCode传1或者2,1为轻量级初始化(之前的取消应用但存在,这次的新加入),2为重量级初始化(之前的删除这次的覆盖)")
    @GetMapping("/folderRules/{mailId}/{resetRuleCode}")
    public Result initMailFoldersAndFireRules(@PathVariable("mailId") Long mailId,@PathVariable("resetRuleCode") Integer resetRuleCode){
        return ResultUtil.success(ewsInitService.initMailFoldersAndFireRules(mailId,resetRuleCode));
    }

    @ApiOperation("初始化文件夹根据邮箱id")
    @GetMapping("/folder/{mailId}")
    public Result initMailFolder(@PathVariable("mailId") Long mailId){
        return ResultUtil.success(ewsInitService.initMailFolder(mailId));
    }

    @ApiOperation("初始化规则根据邮箱id")
    @GetMapping("/rules/{mailId}/{resetRuleCode}")
    public Result initMailRules(@PathVariable("mailId") Long mailId,@PathVariable("resetRuleCode") Integer resetRuleCode){
        return ResultUtil.success(ewsInitService.initMailRules(mailId,resetRuleCode));
    }



    /**
     * 设定全天邮箱监听以及事件监听的初始化接口
     * @param itemActionTypeBo
     * @return
     */
//    @ApiOperation("初始化邮件订阅")
    @PostMapping("/mailSubscription")
    @Deprecated
    public Result initSubscription(@RequestBody ItemActionTypeBo itemActionTypeBo){
        return ResultUtil.success(subscriptionService.initSubscription(itemActionTypeBo.getItemActionType()));
    }

//    //根据邮箱id初始化邮箱的对应文件夹和规则
//    @ApiOperation("初始化文件夹以及规则根据邮件id")
//    @GetMapping("/folderRules/{mailId}")
//    public Result initMailFoldersAndFireRules(@PathVariable("mailId") Long mailId){
//        return ResultUtil.success(ewsInitService.initMailFoldersAndFireRules(mailId));
//    }

    //初始化单个邮箱的订阅以及事件轮询
    @ApiOperation("初始化邮件订阅根据邮件id")
    @GetMapping("/subscription/{mailId}")
    public Result initSubscription(@PathVariable("mailId") Long mailId){
        return ResultUtil.success(ewsInitService.initSubscriptionAndEventPoll(mailId));
    }
    @ApiOperation("取消邮件订阅根据邮件id")
    @GetMapping("/unSubscription/{mailId}")
    public Result unSubscription(@PathVariable("mailId") Long mailId){
        return ResultUtil.success(subscriptionService.unSubscription(mailId));
    }

}
