package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.entity.EwsTopicEntity;
import com.example.easyruledemo.service.IEwsEmailService;
import com.example.easyruledemo.service.IEwsTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:35
 */
@RequestMapping("/ews/topic")
@RestController
public class EwsTopicController {

    @Autowired
    private IEwsTopicService ewsTopicService;

    @PostMapping
    public void addTopic(@RequestBody EwsTopicEntity ewsTopic){

    }

    @PutMapping
    public void updateTopic(@RequestBody EwsTopicEntity ewsTopic){

    }

    @PostMapping
    public void topicList(@RequestBody EwsTopicEntity ewsTopic){

    }

    @GetMapping("/{topicId}")
    public void oneTopic(@PathVariable("topicId") String topicId){

    }

    @DeleteMapping("/{topicId}")
    public void delTopic(@PathVariable("topicId") String topicId){

    }
}
