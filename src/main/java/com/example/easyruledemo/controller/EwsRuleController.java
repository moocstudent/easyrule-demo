package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsRuleEntity;
import com.example.easyruledemo.entity.mvc.DisabledRuleEntity;
import com.example.easyruledemo.model.Result;
import com.example.easyruledemo.service.IEwsRuleService;
import com.example.easyruledemo.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:36
 */
@Api("ews子规则接口层")
@RequestMapping("/ews/rule")
@RestController
public class EwsRuleController {

    @Autowired
    private IEwsRuleService ewsRuleService;

    @ApiOperation("添加规则")
    @PostMapping
    public Result addRule(@RequestBody EwsRuleEntity ewsRule) {
        return ResultUtil.success(ewsRuleService.saveOrUpdateRule(ewsRule));
    }

    @ApiOperation("更新规则")
    @PutMapping
    public Result updateRule(@RequestBody EwsRuleEntity ewsRule) {
        return ResultUtil.success(ewsRuleService.saveOrUpdateRule(ewsRule));
    }

    @ApiOperation("遍历规则,根据规则名称模糊匹配,是否启用与邮件下载后动作为等于")
    @PostMapping("/list")
    public Result ruleList(@RequestBody EwsRuleEntity ewsRule) {
        return ResultUtil.success(ewsRuleService.listSelective(ewsRule));
    }

    @ApiOperation("根据主键获取规则")
    @GetMapping("/{ruleId}")
    public Result oneRule(@PathVariable("ruleId") String ruleId) {
        return ResultUtil.success(ewsRuleService.findOne(ruleId));
    }

    @ApiOperation("根据主键删除规则")
    @DeleteMapping("/{ruleId}")
    public Result delRule(@PathVariable("ruleId") String ruleId) {
        return ResultUtil.success(ewsRuleService.delOne(ruleId));
    }

    @ApiOperation("根据邮箱和规则主键删除规则")
    @PostMapping("/disable}")
    public Result disableRule(@RequestBody DisabledRuleEntity disabledRule) {
        return ResultUtil.success(
                ewsRuleService.disabledRuleByEmAddrAndRuleId(
                        disabledRule.getEmail(), disabledRule.getRuleId()
                )
        );
    }


}
