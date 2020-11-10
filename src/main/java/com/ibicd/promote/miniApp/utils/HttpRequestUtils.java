package com.ibicd.promote.miniApp.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpRequestUtils {

    private static final int TIME_OUT = 10000;

    public static String buildGetRequest(String url, Map<String, Object> requestParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        UriComponents buildComp = builder.build();
        RestTemplate template = new RestTemplate();
        return template.getForObject(buildComp.toString(), String.class);
    }

    public static String buildPostRequest(String url, JSONObject requestBody) {
        try {
            Request req = Request.Post(url).connectTimeout(TIME_OUT).socketTimeout(TIME_OUT);
            req.addHeader(new BasicHeader("Content-Type", "application/json"));
            req.body(new StringEntity(requestBody.toJSONString(), "UTF-8"));

            Response rsp = req.execute();

            if (rsp != null) {
                String responseStr = rsp.returnContent().asString();
                System.out.println("POST 调用结果为： " + responseStr);
                return responseStr;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("调用请求 出错！");
        }

        return null;
    }


    /**
     * 不带参数的POST请求
     *
     * @param url
     * @return
     */
    public static String buildPostRequest(String url) {

        try {
            Request req = Request.Post(url).connectTimeout(TIME_OUT).socketTimeout(TIME_OUT);
            req.addHeader(new BasicHeader("Content-Type", "application/json"));

            Response rsp = req.execute();

            if (rsp != null) {
                String responseStr = rsp.returnContent().asString();
                System.out.println("POST 调用结果为： " + responseStr);
                return responseStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("调用请求 出错！");
        }

        return null;
    }

    public static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers)
            throws Exception {
        // 将url转变为URL对象
        URL _url = new URL(url);
        // 打开URL连接
        if (StringUtils.startsWithIgnoreCase(url, "https")) {
            HttpsURLConnection https = (HttpsURLConnection) _url.openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            trustAllHosts(https);
            // 设置请求方式
            https.setRequestMethod(method);
            // 发送POST请求必须设置如下两行
            https.setDoOutput(true);
            https.setDoInput(true);
            https.setRequestProperty("Charset", "UTF-8");
            // 连接时长
            https.setConnectTimeout(20000);
            // 读取时长
            https.setReadTimeout(60000);
            // 设置通用的请求属性
            https.setRequestProperty("accept", "*/*");
            https.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            https.setRequestProperty("Accept-Charset", "utf-8");
            https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    https.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            return https;
        } else {
            HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
            // 设置请求方式
            conn.setRequestMethod(method);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Charset", "UTF-8");
            // 连接时长
            conn.setConnectTimeout(20000);
            // 读取时长
            conn.setReadTimeout(60000);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            return conn;
        }
    }

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 覆盖java默认的证书验证
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }};

    /**
     * 信任所有
     *
     * @param connection
     * @return
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }

}
