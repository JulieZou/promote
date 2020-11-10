package com.ibicd.promote.miniApp.service.wxacode;

import com.alibaba.fastjson.JSONObject;
import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * 小程序码
 */
@Service
public class WechatMiniAppQrCode {

    /**
     * 获取小程序码，适用于需要码数量较少的业务场景，永久有效，受数量限制
     *
     * @param accessToken
     * @return
     */
    public InputStream getLimitedwxCode(String accessToken) {

        return createQrCode("https://api.weixin.qq.com/wxa/getwxacode?access_token=" + accessToken);
    }

    /**
     * 获取小程序码，永久有效，无数量限制
     *
     * @param accessToken
     * @return
     */
    public InputStream getUnlimitedwxCode(String accessToken) {

        return createQrCode("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken);
    }

    /**
     * 生成微信二维码
     *
     * @param url
     * @return
     */
    private InputStream createQrCode(String url) {
        JSONObject requestBody = new JSONObject();
        JSONObject colorObj = new JSONObject();
        colorObj.put("r", 144);
        colorObj.put("g", 238);
        colorObj.put("b", 144);
        requestBody.put("line_color", colorObj);
        requestBody.put("width", 500);
        requestBody.put("is_hyaline", true);

        //扫描小程序打开的页面
        requestBody.put("path", "www.baidu.com");

        HttpURLConnection conn = null;
        OutputStream out = null;
        String requestStr = requestBody.toJSONString();
        try {
            // 获取HttpURLConnection连接并设置参数
            conn = HttpRequestUtils.getHttpConnection(url, "POST", null);
            // 建立HttpURLConnection实际的连接
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = conn.getOutputStream();
            // 发送请求参数
            if (!StringUtils.isEmpty(requestStr)) {
                out.write(requestStr.getBytes("utf-8"));
            }
            out.flush();
            out.close();
            // 返回 定义BufferedReader输入流来读取URL的响应
            return conn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
