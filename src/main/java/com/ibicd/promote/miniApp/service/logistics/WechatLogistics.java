package com.ibicd.promote.miniApp.service.logistics;

import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 微信物流助手
 */
@Service
public class WechatLogistics {

    public String getAllDelivery(String accessToken) {

        String url = "https://api.weixin.qq.com/cgi-bin/express/business/delivery/getall?access_token=" + accessToken;

        HashMap<String, Object> params = new HashMap<>();
        params.put("scope","snsapi_userinfo");

        String result = HttpRequestUtils.buildGetRequest(url, params);
        return result;
    }
}
