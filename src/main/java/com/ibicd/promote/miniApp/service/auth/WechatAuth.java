package com.ibicd.promote.miniApp.service.auth;

import com.alibaba.fastjson.JSONObject;
import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import com.ibicd.promote.miniApp.domain.WechatConf;
import com.ibicd.promote.mybatis.mapper.WechatConfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权
 */
@Service
public class WechatAuth {

    /**
     * 获取微信OpenID
     *
     * @param jsCode
     * @return
     */
    public String code2Session(String jsCode) {
        WechatConf wechatConf = this.getConf();

        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("grant_type", "authorization_code");
        requestParams.put("appid", wechatConf.getAppId());
        requestParams.put("secret", wechatConf.getAppSecret());
        requestParams.put("js_code", jsCode);

        String result = HttpRequestUtils.buildGetRequest(url, requestParams);
        System.out.println("获取OpenID返回结果：" + result);
        return (String) JSONObject.parseObject(result).get("openid");
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据
     *
     * @return
     */
    public String getAccessToken() {
        WechatConf wechatConf = getConf();
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", wechatConf.getAppId());
        params.put("secret", wechatConf.getAppSecret());

        String result = HttpRequestUtils.buildGetRequest(url, params);
        System.out.println("获取AccessToken返回结果：" + result);
        return result;
    }

    /**
     * 获取微信配置
     *
     * @return
     */
    public WechatConf getConf() {

        WechatConf wechatConf = confMapper.findByAppId("wx5d754a56846fffab");
        if (wechatConf == null) {
            throw new RuntimeException("微信配置不存在！");
        }

        return wechatConf;
    }


    public String getUserInfo(String openid, String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("access_token", accessToken);
        requestParams.put("openid", openid);
        requestParams.put("lang", "zh_CN");
        return HttpRequestUtils.buildGetRequest(url, requestParams);
    }

    @Autowired
    private WechatConfMapper confMapper;

}
