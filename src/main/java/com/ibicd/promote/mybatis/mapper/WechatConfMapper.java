package com.ibicd.promote.mybatis.mapper;

import com.ibicd.promote.miniApp.domain.WechatConf;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WechatConfMapper {

    WechatConf findByAppId(String appId);
}
