package com.ibicd.promote.miniApp.service.customerServiceMessage;

import com.alibaba.fastjson.JSONObject;
import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import org.springframework.stereotype.Service;

/**
 * 客服消息
 */
@Service
public class WechatCustomerSericeMg {

    /**
     * 发送客服消息
     */
    public void send(String accessToken, String openId) {

        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken;

        JSONObject requestBody = new JSONObject();
        requestBody.put("touser", openId);
        requestBody.put("msgtype", "text");
        requestBody.put("text", "发送内容不要太固定啦，这是什么道理！");
        String result = HttpRequestUtils.buildPostRequest(url, requestBody);
        System.out.println("发送消息的结果：" + result);
    }

}
