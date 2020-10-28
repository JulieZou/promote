package com.ibicd.promote.mybatis.domain;


/**
 * 微信配置
 */
public class WechatConf {

    private Integer id;

    private String appId;

    private String appSecret;

    public WechatConf() {
    }

    public WechatConf(Integer id, String appId, String appSecret) {
        this.id = id;
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
