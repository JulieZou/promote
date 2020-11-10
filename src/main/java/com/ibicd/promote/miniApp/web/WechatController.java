package com.ibicd.promote.miniApp.web;

import com.alibaba.fastjson.JSONObject;
import com.ibicd.promote.miniApp.service.analysis.WechatAnalysis;
import com.ibicd.promote.miniApp.service.auth.WechatAuth;
import com.ibicd.promote.miniApp.service.customerServiceMessage.WechatCustomerSericeMg;
import com.ibicd.promote.miniApp.service.logistics.WechatLogistics;
import com.ibicd.promote.miniApp.service.operation.WechatOperation;
import com.ibicd.promote.miniApp.service.subscribeMessage.WechatSubscribeMsg;
import com.ibicd.promote.miniApp.service.wxacode.WechatMiniAppQrCode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * 小程序开发文档地址：
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/
 */
@RestController
@RequestMapping
public class WechatController {


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    void getUserInfo(@RequestParam String code) {
        String userInfo = wechatAuth.getUserInfo(getOpenId(code), this.getToken());
        System.out.println(userInfo);
    }

    /**
     * 获取Token
     *
     * @return
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    String getAccessToken() {
        return this.getToken();
    }

    /**
     * 发送客服消息
     *
     * @param code
     */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    void sendToUser(@RequestParam String code) {
        String openId = wechatAuth.code2Session(code);

        sericeMg.send(this.getToken(), openId);

    }

    /**
     * 发送微信订阅消息（前提是：用户需要订阅）
     *
     * @param code
     */
    @RequestMapping(value = "/uniform", method = RequestMethod.GET)
    void uniform(@RequestParam String code) {

        String openId = wechatAuth.code2Session(code);

        subscribeMsg.send(this.getToken(), openId);
    }


    /**
     * 获取小程序二维码
     *
     * @throws IOException
     */
    @RequestMapping(value = "/miniCode", method = RequestMethod.GET)
    void generateMiniCode() throws IOException {
        String destPath = "D:\\testpic\\miniCode.jpg";
        InputStream inputStream = appQrCode.getLimitedwxCode(getToken());

        if (inputStream == null) {
            System.out.println("生成码失败！");
            return;
        }

        try {
            if (inputStream.available() < 800) {
                // 定义BufferedReader输入流来读取URL的响应
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                if (!StringUtils.isEmpty(sb.toString())) {
                    System.out.println("生成码失败： " + sb);
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("生成码失败！");
            return;
        }
        File destFile = new File(destPath);
        FileUtils.copyInputStreamToFile(inputStream, destFile);
        System.out.println("小程序码生成完成！");
    }


    /**
     * 获取已支持的配送公司列表接口
     */
    @RequestMapping(value = "/getAllDelivery", method = RequestMethod.GET)
    void getAllDelivery(@RequestParam String code) {
        String openId = this.getOpenId(code);
        System.out.println("Open id: " + openId);
        String token = this.getToken();

        String allDelivery = wechatLogistics.getAllDelivery(token);
        JSONObject jsonObject = JSONObject.parseObject(allDelivery);
        System.out.println("配送公司列表： " + jsonObject.toJSONString());
    }

    /**
     * 获取用户反馈列表
     */
    @RequestMapping(value = "/getFeedback", method = RequestMethod.GET)
    void getFeedback() {
        wechatOperation.getFeedback(this.getToken());
    }

    /**
     * 获取访问来源
     */
    @RequestMapping(value = "/getSceneList", method = RequestMethod.GET)
    void getSceneList() {
        wechatOperation.getSceneList(this.getToken());
    }

    /**
     * 获取客户端版本
     */
    @RequestMapping(value = "/getVersionList", method = RequestMethod.GET)
    void getVersionList() {
        wechatOperation.getVersionList(this.getAccessToken());
    }

    /**
     * 实时日志查询
     */
    @RequestMapping(value = "/userLog", method = RequestMethod.GET)
    void userLog() {
        wechatOperation.userLog(this.getAccessToken());
    }


    /**
     * 获取用户访问小程序数据概况
     *
     * @param beginDate 开始日期。格式为 yyyymmdd(默认传输昨天的日期)
     * @param endDate   结束日期，限定查询1天数据，允许设置的最大值为昨日。格式为 yyyymmdd
     */
    @RequestMapping(value = "/getDailySummary")
    void getDailySummary(@RequestParam String beginDate, @RequestParam String endDate) {

        wechatAnalysis.getDailySummary(this.getAccessToken(), beginDate, endDate);
    }


    @RequestMapping(value = "/getOpenId", method = RequestMethod.GET)
    String getOpenId(@RequestParam String code) {

        return wechatAuth.code2Session(code);
    }


    /**
     * 获取token
     *
     * @return
     */
    private String getToken() {
        String accessToken = (String) JSONObject.parseObject(wechatAuth.getAccessToken()).get("access_token");
        if (accessToken == null || StringUtils.isEmpty(accessToken)) {
            throw new RuntimeException("获取token 失败！");
        }

        return accessToken;
    }


    @Autowired
    private WechatAuth wechatAuth;

    @Autowired
    private WechatCustomerSericeMg sericeMg;

    @Autowired
    private WechatSubscribeMsg subscribeMsg;

    @Autowired
    private WechatMiniAppQrCode appQrCode;

    @Autowired
    private WechatLogistics wechatLogistics;

    @Autowired
    private WechatOperation wechatOperation;

    @Autowired
    private WechatAnalysis wechatAnalysis;

}
