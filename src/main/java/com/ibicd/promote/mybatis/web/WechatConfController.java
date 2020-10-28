package com.ibicd.promote.mybatis.web;

import com.ibicd.promote.mybatis.domain.WechatConf;
import com.ibicd.promote.mybatis.service.WechatConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conf")
public class WechatConfController {


    @RequestMapping(value = "/finByAppId")
    WechatConf finByAppId(@RequestParam("appId") String appId) {
        WechatConf wechatConf = service.findByAppId(appId);
        return wechatConf;
    }


    @Autowired
    private WechatConfService service;
}
