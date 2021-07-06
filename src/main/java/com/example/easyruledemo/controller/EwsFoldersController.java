package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.service.IEwsFolderService;
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
    public void addFolders(@RequestBody EwsFoldersEntity ewsFolders){

    }

    @ApiOperation("修改文件夹")
    @PutMapping
    public void updateFolders(@RequestBody EwsFoldersEntity ewsFolders){

    }

    @ApiOperation("遍历文件夹")
    @PostMapping("/list")
    public void folderList(@RequestBody EwsFoldersEntity ewsFolders){

    }

    @ApiOperation("根据主键获取文件夹")
    @GetMapping("/{ewsFolderId}")
    public void oneFolder(@PathVariable("ewsFolderId") String ewsFolderId){

    }

    @ApiOperation("根据主键删除文件夹")
    @DeleteMapping("/{ewsFolderId}")
    public void delFolder(@PathVariable("ewsFolderId") String ewsFolderId){

    }
}
