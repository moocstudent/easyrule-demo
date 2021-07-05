package com.example.easyruledemo.controller;

import com.example.easyruledemo.entity.EwsMailEntity;
import com.example.easyruledemo.service.IEwsEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhangQi
 * @Date: 2021-07-05 13:37
 */
@RequestMapping("/ews/mail")
@RestController
public class EwsMailController {

    @Autowired
    private IEwsEmailService ewsEmailService;

    @PostMapping
    public void addMail(@RequestBody EwsMailEntity ewsMail){

    }

    @PutMapping
    public void updateMail(@RequestBody EwsMailEntity ewsMail){

    }

    @PostMapping
    public void mailList(@RequestBody EwsMailEntity ewsMail){

    }

    @GetMapping("/{mailId}")
    public void oneMail(@PathVariable("mailId") String mailId){

    }

    @DeleteMapping("/{mailId}")
    public void delMail(@PathVariable("mailId") String mailId){

    }
}
