package com.ibicd.promote.mybatis.service;

import com.ibicd.promote.mybatis.domain.WechatConf;
import com.ibicd.promote.mybatis.mapper.WechatConfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatConfService {

    public void add(WechatConf conf) {
        confMapper.add(conf);
    }

    public WechatConf findByAppId(String appId) {

        return confMapper.findByAppId(appId);
    }

    public void del(Integer id) {
        confMapper.del(id);
    }

    @Autowired
    private WechatConfMapper confMapper;
}
