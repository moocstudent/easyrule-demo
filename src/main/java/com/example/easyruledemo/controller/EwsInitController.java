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

    @ApiOperation("初始化文件夹以及规则")
    @PostMapping("/mailFolderRules")
    public Result initMailFoldersAndFireRules(@RequestBody ItemActionTypeBo itemActionTypeBo){
        return ResultUtil.success(ewsInitService.initMailFoldersAndFireRules(itemActionTypeBo.getItemActionType()));
    }

}
