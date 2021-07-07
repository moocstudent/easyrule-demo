package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IEwsFolderService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:36
 */
@Api("ews文件夹接口层")
@RequestMapping("/ews/folders")
@RestController
public class EwsFoldersController {

    @GetMapping("/test")
    public String test(){
        return "test ok";
    }

    @Autowired
    private IEwsFolderService ewsFolderService;

    @ApiOperation("添加文件夹")
    @PostMapping
    public Result addFolders(@RequestBody EwsFoldersEntity ewsFolders){
        return ResultUtil.success(ewsFolderService.saveOrUpdateFolder(ewsFolders));
    }

    @ApiOperation("修改文件夹")
    @PutMapping
    public Result updateFolders(@RequestBody EwsFoldersEntity ewsFolders){
        return ResultUtil.success(ewsFolderService.saveOrUpdateFolder(ewsFolders));
    }

    @ApiOperation("遍历文件夹,文件名是模糊匹配,其它的是等于")
    @PostMapping("/list")
    public Result folderList(@RequestBody EwsFoldersEntity ewsFolders){
        return ResultUtil.success(ewsFolderService.listSelective(ewsFolders));
    }

    @ApiOperation("根据主键获取文件夹")
    @GetMapping("/{ewsFolderId}")
    public Result oneFolder(@PathVariable("ewsFolderId") String ewsFolderId){
        return ResultUtil.success(ewsFolderService.getByPriKey(ewsFolderId));
    }

    @ApiOperation("根据主键删除文件夹")
    @DeleteMapping("/{ewsFolderId}")
    public Result delFolder(@PathVariable("ewsFolderId") String ewsFolderId){
        return ResultUtil.success(ewsFolderService.delByPriKey(ewsFolderId));
    }
}
