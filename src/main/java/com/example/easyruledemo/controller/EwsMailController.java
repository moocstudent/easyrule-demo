package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:37
 */
@Api("ews邮箱配置接口层")
@RequestMapping("/ews/mail")
@RestController
public class EwsMailController {

    @Autowired
    private IEwsEmailService ewsEmailService;

    @ApiOperation("添加一个邮箱")
    @PostMapping
    public Result addMail(@RequestBody EwsMailEntity ewsMail){
        return ResultUtil.success(ewsEmailService.saveOrUpdateEmail(ewsMail));
    }

    @ApiOperation("更新一个邮箱")
    @PutMapping
    public Result updateMail(@RequestBody EwsMailEntity ewsMail){
        return ResultUtil.success(ewsEmailService.saveOrUpdateEmail(ewsMail));
    }

    @ApiOperation("动态获取邮箱列表")
    @PostMapping("/list")
    public Result mailList(@RequestBody EwsMailEntity ewsMail){
        return ResultUtil.success(ewsEmailService.listSelective(ewsMail));
    }

    @ApiOperation("根据邮件主键获取一个")
    @GetMapping("/{mailId}")
    public Result oneMail(@PathVariable("mailId") String mailId){
        return ResultUtil.success(ewsEmailService.findOne(mailId));
    }

    @ApiOperation("删除邮箱")
    @DeleteMapping("/{mailId}")
    public Result delMail(@PathVariable("mailId") String mailId){
        return ResultUtil.success(ewsEmailService.delOne(mailId));
    }
}
