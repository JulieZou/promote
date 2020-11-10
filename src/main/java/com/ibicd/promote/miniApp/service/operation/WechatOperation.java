package com.ibicd.promote.miniApp.service.operation;

import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 运维中心
 */
@Service
public class WechatOperation {


    /**
     * 获取用户反馈列表
     *
     * @param accessToken
     * @return
     */
    public void getFeedback(String accessToken) {
        String url = "https://api.weixin.qq.com/wxaapi/feedback/list?access_token=" + accessToken;
        String result = HttpRequestUtils.buildGetRequest(url, new HashMap<>());
        System.out.println("获取用户反馈结果： " + result);
    }

    /**
     * 获取访问来源
     *
     * @param accessToken
     */
    public void getSceneList(String accessToken) {
        String result = HttpRequestUtils.buildGetRequest(
                "https://api.weixin.qq.com/wxaapi/log/get_scene?access_token=" + accessToken, new HashMap<>());
        System.out.println("获取访问来源： " + result);
    }

    /**
     * 获取客户端版本
     *
     * @param accessToken
     */
    public void getVersionList(String accessToken) {
        String result = HttpRequestUtils.buildGetRequest(
                "https://api.weixin.qq.com/wxaapi/log/get_client_version?access_token=" + accessToken, new HashMap<>());

        System.out.println("获取客户端版本： " + result);
    }

    /**
     * 实时日志查询
     *
     * @param accessToken
     */
    public void userLog(String accessToken) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("date", "20201027");
        params.put("begintime", 1603728000);
        params.put("endtime", 1604073600);

        String result = HttpRequestUtils.buildGetRequest(
                "https://api.weixin.qq.com/wxaapi/userlog/userlog_search?access_token=" + accessToken, params);

        System.out.println("实时日志查询： " + result);
    }
}
