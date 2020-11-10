package com.ibicd.promote.miniApp.service.subscribeMessage;

import com.alibaba.fastjson.JSONObject;
import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import org.springframework.stereotype.Service;

/**
 * 订阅消息
 */
@Service
public class WechatSubscribeMsg {

    /**
     * 发送订阅消息给用户
     *
     * @param accessToken
     * @param openId
     */
    public void send(String accessToken, String openId) {

        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        JSONObject requestBody = new JSONObject();
        requestBody.put("touser", openId);
        requestBody.put("template_id", "87nOWLDVCqXlTCCwpUo3bHL2L9ci2LxOjajhkcJbv2Y");
        String data = "{\"character_string1\":{\"value\":\"20201029001\"},\"thing2\":{\"value\":\"家具安装\"},\"name3\":{\"value\":\"张三\"},\"thing4\":{\"value\":\"广州\"}}";
        requestBody.put("data", data);

        String result = HttpRequestUtils.buildPostRequest(url, requestBody);
        System.out.println("发送订阅消息的结果：" + result);

    }

}
