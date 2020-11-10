package com.ibicd.promote.miniApp.service.analysis;

import com.alibaba.fastjson.JSONObject;
import com.ibicd.promote.miniApp.utils.HttpRequestUtils;
import org.springframework.stereotype.Service;

/**
 * 数据分析
 */
@Service
public class WechatAnalysis {

    /**
     * 获取用户访问小程序数据概况
     *
     * @param acessToken
     * @param beginDate  开始日期。格式为 yyyymmdd
     * @param endDate    结束日期，限定查询1天数据，允许设置的最大值为昨日。格式为 yyyymmdd
     * @return
     */
    public void getDailySummary(String acessToken, String beginDate, String endDate) {

        String url = "https://api.weixin.qq.com/datacube/getweanalysisappiddailysummarytrend?access_token=" + acessToken;

        JSONObject params = new JSONObject();
        params.put("begin_date", beginDate);
        params.put("end_date", endDate);

        String result = HttpRequestUtils.buildPostRequest(url, params);
        System.out.println("获取用户访问小程序数据概况: " + result);
    }
}
