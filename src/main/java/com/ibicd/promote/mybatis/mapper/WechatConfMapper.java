package com.ibicd.promote.mybatis.mapper;

import com.ibicd.promote.mybatis.domain.WechatConf;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WechatConfMapper {

    void add(WechatConf conf);

    WechatConf findByAppId(String appId);

    void del(Integer id);
}
