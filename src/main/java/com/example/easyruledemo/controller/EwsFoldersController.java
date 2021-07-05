package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsFoldersEntity;
import com.example.easyruledemo.service.IEwsFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:36
 */
@RequestMapping("/ews/folders")
@RestController
public class EwsFoldersController {

    @Autowired
    private IEwsFolderService ewsFolderService;

    @PostMapping
    public void addFolders(@RequestBody EwsFoldersEntity ewsFolders){

    }

    @PutMapping
    public void updateFolders(@RequestBody EwsFoldersEntity ewsFolders){

    }

    @PostMapping
    public void folderList(@RequestBody EwsFoldersEntity ewsFolders){

    }

    @GetMapping("/{ewsFolderId}")
    public void oneFolder(@PathVariable("ewsFolderId") String ewsFolderId){

    }

    @DeleteMapping("/{ewsFolderId}")
    public void delFolder(@PathVariable("ewsFolderId") String ewsFolderId){

    }
}
