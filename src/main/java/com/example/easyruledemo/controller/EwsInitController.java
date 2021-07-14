package com.example.easyruledemo.controller;

import com.example.easyruledemo.bo.ItemActionTypeBo;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IEwsInitService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 设定符合要求的邮件初始化文件夹和规则
     * @param itemActionTypeBo
     * @return
     */
    @ApiOperation("初始化文件夹以及规则")
    @PostMapping("/mailFolderRules")
    public Result initMailFoldersAndFireRules(@RequestBody ItemActionTypeBo itemActionTypeBo){
        return ResultUtil.success(ewsInitService.initMailFoldersAndFireRules(itemActionTypeBo.getItemActionType()));
    }

    /**
     * 设定全天邮箱监听以及事件监听的初始化接口
     * @param itemActionTypeBo
     * @return
     */
    @ApiOperation("初始化邮件订阅")
    @PostMapping("/mailSubscription")
    public Result initSubscription(@RequestBody ItemActionTypeBo itemActionTypeBo){
        return ResultUtil.success(ewsInitService.initSubscription(itemActionTypeBo.getItemActionType()));
    }

}
